package com.example.searchmoviesomdb;

import androidx.multidex.MultiDexApplication;

public class MyApplication extends MultiDexApplication {

    private static MyApplication singleton;

    public static MyApplication getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }
}
