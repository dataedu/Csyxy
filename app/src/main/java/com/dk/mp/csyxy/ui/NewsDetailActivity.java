package com.dk.mp.csyxy.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.Toolbar;
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

    private ObservableScrollView scroll;
    private RelativeLayout top;
    private LinearLayout top2;
    private TextView top_title;
    private AutoAjustSizeTextView bottom_title;

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
        bottom_title = (AutoAjustSizeTextView) findViewById(R.id.bottom_title);

//        mError = (ErrorLayout) findViewById(R.id.error_layout);
//        ViewCompat.setTransitionName(mImageViewTop, "detail_element");
        initData();
    }

    public void initData(){
        news = (News) getIntent().getSerializableExtra("news");
        mType = getIntent().getStringExtra("mType");
//        Log.e("vvvvvvvvvvvvvvvvvv",mType+");
        if (mType !=null){
            if (mType.equals("zjcy")){
                mImageViewTop.setImageResource(R.mipmap.zjcy);
            }else if (mType.equals("xw")){
                mImageViewTop.setImageResource(R.mipmap.xw);
            }else if (mType.equals("gg")){
                mImageViewTop.setImageResource(R.mipmap.gg);
            }else if (mType.equals("rcyj")){
                mImageViewTop.setImageResource(R.mipmap.rcyj);
            }else if (mType.equals("ybxx")){
                mImageViewTop.setImageResource(R.mipmap.ybxx);
            }else if (mType.equals("dzjs")){
                mImageViewTop.setImageResource(R.mipmap.dzjs);
            }
        } else {
            Glide.with(mContext).load(news.getImage()).fitCenter().into(mImageViewTop);

        }
//        Glide.with(mContext).load(news.getImage()).fitCenter().into(mImageViewTop);
        setWebView ( );

        //标题显示
        intTitle();

        layout = (ErrorLayout)findViewById(R.id.errorlayout);
        if(DeviceUtil.checkNet()) {//检查网络
            if (StringUtils.isNotEmpty(news.getContent())) {
                layout.setVisibility(View.GONE);
                mWebView.setVisibility(View.VISIBLE);
                mWebView.loadDataWithBaseURL(null, news.getContent(), "text/html", "utf-8", null);
            }else if(news.getUrl() == null){
                layout.setVisibility(View.VISIBLE);
                mWebView.setVisibility(View.GONE);
                layout.setErrorType(ErrorLayout.NODATA);
            }else{
                String url = getUrl(news.getUrl());
                layout.setVisibility(View.VISIBLE);
                mWebView.setVisibility(View.GONE);
                mWebView.loadUrl(url);
            }
        }else{
            layout.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
            layout.setErrorType(ErrorLayout.NETWORK_ERROR);
        }
    }

    private void intTitle() {

        top_title.setText(news.getName());
        bottom_title.setText(news.getName());

        scroll.setScrollViewListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {

                if (y<96){
                    top2.setVisibility(View.VISIBLE);
                    top.setVisibility(View.GONE);
                }else {
                    top.setVisibility(View.VISIBLE);
                    top2.setVisibility(View.GONE);
                }

                if (y<460){
                    top_title.setVisibility(View.GONE);
                    bottom_title.setVisibility(View.VISIBLE);
                }else {
                    top_title.setVisibility(View.VISIBLE);
                    bottom_title.setVisibility(View.GONE);
                }

            }
        });
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
