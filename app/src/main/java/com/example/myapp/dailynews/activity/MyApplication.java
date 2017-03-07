package com.example.myapp.dailynews.activity;

import android.app.Application;
import android.content.Context;

/**
 * Created by Lenovo on 2017/2/17.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        super.onCreate();
    }

    public static Context getContext(){
        return context;
    }
}
