package com.dk.mp.csyxy;

import com.dk.mp.core.application.MyApplication;

import cn.jpush.android.api.JPushInterface;

/**
 * 作者：janabo on 2017/11/6 16:11
 */
public class MpApplication extends MyApplication{
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
