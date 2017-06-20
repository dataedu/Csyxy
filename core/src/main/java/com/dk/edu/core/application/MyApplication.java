package com.dk.edu.core.application;

import android.app.Application;
import android.content.Context;

import com.dk.edu.core.util.ImagePipelineConfigFactory;
import com.facebook.drawee.backends.pipeline.Fresco;

import io.realm.Realm;

/**
 * 作者：janabo on 2017/6/7 13:45
 */
public class MyApplication extends Application {

    private static Application mApplication;

    public static Context getContext ( ) {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        Realm.init(this);
        //初始化图片加载框架
        Fresco.initialize(this, ImagePipelineConfigFactory.getImagePipelineConfig(this));
    }
}
