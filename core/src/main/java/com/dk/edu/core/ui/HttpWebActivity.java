package com.dk.edu.core.ui;

import android.graphics.Bitmap;
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

import com.dk.edu.core.R;
import com.dk.edu.core.util.DeviceUtil;
import com.dk.edu.core.util.Logger;
import com.dk.edu.core.widget.ErrorLayout;


/**
 * 作者：janabo on 2017/2/20 11:56
 */
public class HttpWebActivity extends MyActivity{
    WebView mWebView;
    private ErrorLayout mError;
    private ProgressBar mProgressBar;
//    private TextView mClose;

    @Override
    protected int getLayoutID() {
        return R.layout.core_webview;
    }

    @Override
    protected void initView() {
        super.initView();
//        mClose = (TextView) findViewById(R.id.close_web);
        mWebView = (WebView) findViewById(R.id.webview);
        mError = (ErrorLayout) findViewById(R.id.error_layout);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        setTitle(getIntent().getStringExtra("title"));
        String url = getIntent().getStringExtra("url");
//        int close_web = getIntent().getIntExtra("close_web",1);
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

//        if(-1 == close_web){
//            mClose.setVisibility(View.VISIBLE);
//            findViewById(R.id.back).setVisibility(View.GONE);
//            mClose.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//                    back();
//                }
//            });
//        }
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
}
