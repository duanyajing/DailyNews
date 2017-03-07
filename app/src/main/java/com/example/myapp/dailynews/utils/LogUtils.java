package com.example.myapp.dailynews.utils;

import android.util.Log;

/**
 * Created by Lenovo on 2017/2/13.
 */

public class LogUtils {
    private static boolean debag = true;
    public static void d(Object obj, String msg){
        if (debag) {
            Log.d(obj.getClass().getSimpleName(), msg);
        }
    }
}
