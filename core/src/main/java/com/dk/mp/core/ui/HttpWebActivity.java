package com.dk.mp.core.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dk.mp.core.R;
import com.dk.mp.core.http.HttpUtil;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.FileUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.widget.ErrorLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

import okhttp3.Call;
import okhttp3.Response;


/**
 * 作者：janabo on 2017/2/20 11:56
 */
public class HttpWebActivity extends MyActivity{
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    WebView mWebView;
    private ErrorLayout mError;
    private ProgressBar mProgressBar;
    private TextView mClose;
    private String  filename;
    private String mFilepath = Environment.getExternalStorageDirectory() + "/mobileschool/cache/";

    @Override
    protected int getLayoutID() {
        return R.layout.core_webview;
    }

    @Override
    protected void initView() {
        super.initView();
        mClose = (TextView) findViewById(R.id.close_web);
        mWebView = (WebView) findViewById(R.id.webview);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        setTitle(getIntent().getStringExtra("title"));
        String url = getIntent().getStringExtra("url");
        int close_web = getIntent().getIntExtra("close_web",1);
        mError.setErrorType(ErrorLayout.LOADDATA);
        if(DeviceUtil.checkNet()){
            setMUrl(url);
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
        try {
            Button back = (Button) findViewById(R.id.back);
            back.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    } else {
                        finish();
                    }
                }
            });
        } catch (Exception e) {
        }

        if(-1 == close_web){
            mClose.setVisibility(View.VISIBLE);
            mClose.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    back();
                }
            });
        }
    }

    public void setMUrl(String url){
        setWebView();
        url = getUrl(url);
        Logger.info("##########murl="+url);
        mWebView.removeAllViews();
        mWebView.clearCache(true);
        mWebView.loadUrl (url);
    }

    private void setWebView ( ) {
        WebSettings settings = mWebView.getSettings ( );
        mWebView.setWebViewClient ( new MyWebViewClient ( mProgressBar ) );
        mWebView.setWebChromeClient ( new MyWebChromeClient ( mProgressBar ) );
        settings.setSupportZoom ( true );          //支持缩放
        settings.setBlockNetworkImage ( false );  //设置图片最后加载
        settings.setDatabaseEnabled ( true );
        settings.setCacheMode ( WebSettings.LOAD_NO_CACHE );
        settings.setJavaScriptEnabled ( true );    //启用JS脚本
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                // 监听下载功能，当用户点击下载链接的时候，直接调用系统的浏览器来下载
//                Uri uri = Uri.parse(url);
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);

                verifyStoragePermissions(url);
            }
        });
    }


    public class MyWebViewClient extends WebViewClient {
        ProgressBar mProgressBar;
        public MyWebViewClient ( ProgressBar progressBar ) {
            super ( );
            mProgressBar = progressBar;
        }

        @Override
        public void onPageStarted ( WebView view, String url, Bitmap favicon ) {
            super.onPageStarted ( view, url, favicon );
            mProgressBar.setVisibility ( View.VISIBLE );
        }

        @Override
        public void onPageFinished ( WebView webView, String url ) {
            super.onPageFinished ( webView, url );
            mProgressBar.setVisibility ( View.INVISIBLE );
            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
        }
    }

    public class MyWebChromeClient extends WebChromeClient {
        ProgressBar mWebProgressBar;

        public MyWebChromeClient ( ProgressBar mWebProgressBar ) {
            this.mWebProgressBar = mWebProgressBar;
        }

        @Override
        public void onProgressChanged ( WebView view, int newProgress ) {
            mWebProgressBar.setProgress ( newProgress );
            Logger.info("##########newProgress="+newProgress);
            if(newProgress>=100){
                mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
            }
        }

        @Override
        public void onReceivedTitle ( WebView view, String title ) {
            super.onReceivedTitle ( view, title );
        }
    }


    /**
     * 处理url
     * @param url
     * @return
     */
    private String getUrl(String url) {
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return url;
        } else {
            return mContext.getString(R.string.rootUrl)+url;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }



    /**
     * 文件下载
     */
    public void filename(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL u = new URL(url);
                    HttpURLConnection httpConnection = (HttpURLConnection) u.openConnection();
                    String str = httpConnection.getHeaderField("Content-Disposition");
                    filename = str.split("filename=")[1].replace("\"", "");
                    filename = mFilepath + URLDecoder.decode(filename, "UTF-8");
                    Logger.info("filename==="+filename);
                    Message msg = new Message();
                    msg.what=1;
                    msg.obj=url;
                    Logger.info("url==="+url);
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    // 适配器
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    String url=msg.obj.toString();
                    download(url, filename);
                    break;
                case 2:

                    break;
                default:
                    break;
            }
        };
    };


    private void download(String url, final String path) {
        Logger.info("path==="+path);
        if (new File(path).exists()) {
            Logger.info(path+"exists============   ");
            mContext.startActivity(FileUtil.openFile(path));
        }else{
            HttpUtil.getInstance().downloadFile(url,new okhttp3.Callback(){
                @Override
                public void onFailure(Call call, IOException e) {
                    call.cancel();// 上传失败取消请求释放内存
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    download(response,path);
                    call.cancel();// 上传失败取消请求释放内存
                }
            });
        }
    }

    public void download(Response response, String path){
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            File file = new File(path);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            mContext.startActivity(FileUtil.openFile(filename));
            Logger.info("下载成功");
        }catch (Exception e){
            e.printStackTrace();
            Logger.info("下载失败");
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                Logger.info("下载失败");
            }
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
            }
        }
    }


    /**
     * 请求读写权限
     * @param
     */
    public void verifyStoragePermissions(String url) {
        int permission = ActivityCompat.checkSelfPermission(HttpWebActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int rePermission = ActivityCompat.checkSelfPermission(HttpWebActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED || rePermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HttpWebActivity.this, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }else{
            filename(url);
        }
    }
}
