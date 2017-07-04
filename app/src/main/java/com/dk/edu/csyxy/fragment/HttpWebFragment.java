package com.dk.edu.csyxy.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.dk.edu.core.ui.BaseFragment;
import com.dk.edu.core.util.BroadcastUtil;
import com.dk.edu.core.util.DeviceUtil;
import com.dk.edu.core.util.Logger;
import com.dk.edu.core.widget.ErrorLayout;
import com.dk.edu.csyxy.R;

import static com.dk.edu.core.http.HttpUtil.mContext;

/**
 * 加载网页fragment
 * 作者：janabo on 2017/6/14 13:58
 */
public class HttpWebFragment extends BaseFragment{
    WebView mWebView;
    private ErrorLayout mError;
    private ProgressBar mProgressBar;
    private String mUrl;

    public static HttpWebFragment newInstance(String mUrl) {
        Bundle args = new Bundle();
        args.putString("MURL",mUrl);
        HttpWebFragment fragment = new HttpWebFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.core_webview2;
    }

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);

        mUrl = getArguments().getString("MURL");
        mWebView = findView(R.id.webview);
        mError = findView(R.id.error_layout);
        mProgressBar = findView(R.id.progressbar);
        mError.setErrorType(ErrorLayout.LOADDATA);

        if(DeviceUtil.checkNet()){
            setMUrl(mUrl);
        }else{
            mError.setErrorType(ErrorLayout.NETWORK_ERROR);
        }

        BroadcastUtil.registerReceiver(getContext(), mRefreshBroadcastReceiver, new String[]{"checknetwork_true","checknetwork_false"});
    }

    private BroadcastReceiver mRefreshBroadcastReceiver = new BroadcastReceiver() {
        @SuppressLint("NewApi") @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("checknetwork_true")) {
                setMUrl(mUrl);
            }
//            if(action.equals("checknetwork_false")){
//                mError.setErrorType(ErrorLayout.NETWORK_ERROR);
//            }

        }
    };


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

//    @Override
//    public void onPause() {
//        super.onPause();
//        mWebView.onPause();
//    }
}
