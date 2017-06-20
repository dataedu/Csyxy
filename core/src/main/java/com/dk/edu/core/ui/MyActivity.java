package com.dk.edu.core.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.dk.edu.core.R;
import com.dk.edu.core.util.CoreSharedPreferencesHelper;
import com.dk.edu.core.util.SnackBarUtil;

/**
 * 作者：janabo on 2017/6/7 13:48
 */
public abstract class MyActivity extends AppCompatActivity{
    public Context mContext;
    public CoreSharedPreferencesHelper preference;

    /**
     * @return 界面布局
     */
    protected abstract
    @LayoutRes
    int getLayoutID();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView ( getLayoutID ( ) );
        mContext = this;
        initView();
        initialize();
    }

    protected void initView() {

    }

    protected void initialize() {

    }

    /**
     * 初始化皮肤.
     * @param title 标题栏文字
     */
    public void setTitle(int title) {
        setTitle(getReString(title));
    }


    /**
     * 获取string
     * @param string R.string.x
     * @return  ""
     */
    public String getReString(int string) {
        return getResources().getString(string);
    }

    /**
     * @param title 标题栏文字
     */
    public void setTitle(String title) {
        TextView textView = (TextView) findViewById(R.id.title);
        textView.setText(title);
    }

    /**
     * 返回
     */
    public void back() {
        onBackPressed();
    }

    public void showErrorMsg(View v, String msg){
        SnackBarUtil.showShort(v,msg);
    }

    public CoreSharedPreferencesHelper getSharedPreferences() {
        if (preference == null){
            preference = new CoreSharedPreferencesHelper(this);
        }
        return preference;
    }

}
