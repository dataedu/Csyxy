package com.dk.mp.csyxy.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.core.util.DeviceUtil;
import com.dk.mp.core.util.Logger;
import com.dk.mp.core.util.StringUtils;
import com.dk.mp.core.view.edittext.AutoAjustSizeTextView;
import com.dk.mp.core.view.scroll.ObservableScrollView;
import com.dk.mp.core.view.scroll.ScrollViewListener;
import com.dk.mp.core.widget.ErrorLayout;
import com.dk.mp.csyxy.R;
import com.dk.mp.csyxy.entity.News;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


/**
 * 学校新闻详情
 * 作者：janabo on 2016/12/20 15:01
 */
public class NewsDetailActivity extends MyActivity implements View.OnClickListener{
    Context context = NewsDetailActivity.this;
    ImageView mImageViewTop;
    ProgressBar mProgressBar;
    WebView mWebView;
    News news;
    Toolbar mToolbar;
    ErrorLayout layout;
//    private ErrorLayout mError;

    String mType = "";
    String dType = "";

    private ObservableScrollView scroll;
    private RelativeLayout top;
    private LinearLayout top2;
    private TextView top_title;
    private TextView bottom_title;

    @Override
    protected int getLayoutID() {
        return R.layout.app_news_detail;
    }

    @Override
    protected void initialize() {
        super.initialize();
        mImageViewTop = (ImageView) findViewById(R.id.iv_new_detail_top);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_new_detail);
        mWebView = (WebView) findViewById(R.id.webview_new_detail);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        scroll = (ObservableScrollView) findViewById(R.id.scroll);
        top = (RelativeLayout) findViewById(R.id.top);
        top2 = (LinearLayout) findViewById(R.id.top2);
        top_title = (TextView) findViewById(R.id.top_title);
        bottom_title = (TextView) findViewById(R.id.bottom_title);

//        mError = (ErrorLayout) findViewById(R.id.error_layout);
//        ViewCompat.setTransitionName(mImageViewTop, "detail_element");
        initData();
    }

    public void initData(){
        news = (News) getIntent().getSerializableExtra("news");
        mType = getIntent().getStringExtra("mType");
        dType = getIntent().getStringExtra("dType");
        if (mType !=null){
            int img = 0;
            if (mType.equals("xw")){
//                mImageViewTop.setImageResource(R.mipmap.zjcy_a);
                img = R.mipmap.zjcy_a;
            }else if (mType.equals("gg")){
//                mImageViewTop.setImageResource(R.mipmap.gg);
                img = R.mipmap.gg;
            }else if (mType.equals("rcyj")){
//                mImageViewTop.setImageResource(R.mipmap.rcyj);
                img = R.mipmap.rcyj;
            }else if (mType.equals("ybxx")){
//                mImageViewTop.setImageResource(R.mipmap.ybxx);
                img = R.mipmap.ybxx;
            }else if (mType.equals("dzjs")){
//                mImageViewTop.setImageResource(R.mipmap.dzjs);
                img = R.mipmap.dzjs;
            }
            Glide.with(mContext).load(img).into(mImageViewTop);

        } else if(dType != null && dType.equals("zjcy")){
            String name = news.getName();
            if (name != null){
                int img = 0;
                if (name.equals("学校领导")){
//                    mImageViewTop.setImageResource(R.mipmap.zjcy_xxld);
                    img = R.mipmap.zjcy_xxld;
                }else if (name.equals("长医概况")){
//                    mImageViewTop.setImageResource(R.mipmap.zjcy_xxgk);
                    img = R.mipmap.zjcy_xxgk;
                }else if (name.equals("校长寄语")){
//                    mImageViewTop.setImageResource(R.mipmap.zjcy_xzjy);
                    img = R.mipmap.zjcy_xzjy;
                }else if (name.equals("学校章程")){
//                    mImageViewTop.setImageResource(R.mipmap.zjcy_xxzc);
                    img = R.mipmap.zjcy_xxzc;
                }
                Glide.with(mContext).load(img).into(mImageViewTop);
            }
        }else {
            int img = 0;
            if (news.getId() != null && news.getId().equals("a")){
//                mImageViewTop.setImageResource(R.mipmap.zjcy_a);
                img = R.mipmap.zjcy_a;
            }else if (news.getId() != null && news.getId().equals("b")){
//                mImageViewTop.setImageResource(R.mipmap.zjcy_b);
                img = R.mipmap.zjcy_b;
            }else  if (news.getId() != null && news.getId().equals("c")){
//                mImageViewTop.setImageResource(R.mipmap.zjcy_c);
                img = R.mipmap.zjcy_c;
            }else  if (news.getId() != null && news.getId().equals("d")){
//                mImageViewTop.setImageResource(R.mipmap.zjcy_d);
                img = R.mipmap.zjcy_d;
            }else {
                Glide.with(mContext).load(news.getImage()).fitCenter().into(mImageViewTop);
            }
            Glide.with(mContext).load(img).into(mImageViewTop);
        }
        setWebView ( );

        //标题显示
        intTitle();

        layout = (ErrorLayout)findViewById(R.id.errorlayout);
        if(DeviceUtil.checkNet()) {//检查网络
            if (StringUtils.isNotEmpty(news.getUrl())) {
                String url = getUrl(news.getUrl());
                layout.setVisibility(View.VISIBLE);
                mWebView.setVisibility(View.GONE);
                mWebView.loadUrl(url);

            }else if(news.getContent() == null){
                layout.setVisibility(View.VISIBLE);
                mWebView.setVisibility(View.GONE);
                layout.setErrorType(ErrorLayout.NODATA);
            }else{
                layout.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
                mWebView.loadDataWithBaseURL(null, news.getContent(), "text/html", "utf-8", null);
            }
        }else{
            layout.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
            layout.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
    }

    public static Bitmap readBitMap(Context context, int resId){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is,null,opt);
    }

    private void intTitle() {

        top_title.setText(news.getName());

        bottom_title.setText(news.getName());

        //自动调整字体大小
//        Log.e("大小-----------",bottom_title.getTextSize()+"");
        bottom_title.post(new Runnable() {
            @Override
            public void run() {
//                Log.e("行数-----------",bottom_title.getLineCount()+"");
                sizt();

            }
        });

        scroll.setScrollViewListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {

                if (y<20){
                    top2.setVisibility(View.VISIBLE);
                    top.setVisibility(View.GONE);
                }else {
                    top.setVisibility(View.VISIBLE);
                    top2.setVisibility(View.GONE);
                }

                //260-48=212=424
                if (y>300){
                    top_title.setVisibility(View.VISIBLE);
                    bottom_title.setVisibility(View.GONE);
                }else {
                    top_title.setVisibility(View.GONE);
                    bottom_title.setVisibility(View.VISIBLE);
                }

//                Log.e("y------------------",y+"");

            }
        });
    }

    private void sizt(){
        while (bottom_title.getLineCount()>2){
            int size = (int) bottom_title.getTextSize()/2;
            size = size -1;
            bottom_title.setTextSize(size);
//            Log.e("大小后-----------",bottom_title.getTextSize()+"");

            bottom_title.post(new Runnable() {
                @Override
                public void run() {
                    if (bottom_title.getTextSize()>8){
                        sizt();
                    }else {
                        bottom_title.setTextSize(8);
                        bottom_title.setMaxLines(2);
                        bottom_title.setEllipsize(TextUtils.TruncateAt.END);
                        return;
                    }
                }
            });
        }
    }

    public void titleback(View v){
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            onBackPressed ( );
        }
    }

    private void setNavigationClick ( ) {
        mToolbar.setNavigationOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    onBackPressed ( );
                }
            }
        } );
    }

    private void setWebView ( ) {
//        mError.setErrorType(ErrorLayout.LOADDATA);
        WebSettings settings = mWebView.getSettings ( );
        mWebView.setWebViewClient ( new MyWebViewClient ( mProgressBar ) );
        mWebView.setWebChromeClient ( new MyWebChromeClient ( mProgressBar ) );
        settings.setJavaScriptEnabled ( true );    //启用JS脚本
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom ( true );          //支持缩放
        settings.setBlockNetworkImage ( false );  //设置图片最后加载
        settings.setDatabaseEnabled ( true );
        settings.setCacheMode ( WebSettings.LOAD_CACHE_ELSE_NETWORK );
        settings.setAppCacheEnabled ( true );
        settings.setUseWideViewPort(true);
        //自适应屏幕
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
    }

    @Override
    public void onClick(View view) {

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
//            mError.setErrorType(ErrorLayout.HIDE_LAYOUT);
            layout.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
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
                layout.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
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
            return context.getString(R.string.rootUrl)+url;
        }
    }


    @Override
    protected void onPause ( ) {
        super.onPause ( );
        mWebView.onPause ();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        } else {
            onBackPressed ( );
        }
        return super.onKeyDown(keyCode, event);
    }

}
