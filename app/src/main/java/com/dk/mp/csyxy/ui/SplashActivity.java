package com.dk.mp.csyxy.ui;

import android.content.Intent;
import android.os.Handler;

import com.dk.mp.core.ui.MyActivity;
import com.dk.mp.csyxy.MainActivity;
import com.dk.mp.csyxy.R;

/**
 * 作者：janabo on 2017/6/7 13:51
 */
public class SplashActivity extends MyActivity {

    @Override
    protected int getLayoutID() {
        return R.layout.splash;
    }

    @Override
    protected void initView() {
        super.initView();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }
}
