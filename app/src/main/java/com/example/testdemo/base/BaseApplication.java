package com.example.testdemo.base;

import android.app.Application;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static BaseApplication application;

    public static BaseApplication getApplication() {
        return  application;
    }

}
