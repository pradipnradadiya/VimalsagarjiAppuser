package com.vimalsagarji.vimalsagarjiapp;

import android.app.Application;
import android.content.Context;

import com.vimalsagarji.vimalsagarjiapp.common.Sharedpreferance;

public class MyApplication extends Application {

    private static MyApplication mInstance;
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }


}
