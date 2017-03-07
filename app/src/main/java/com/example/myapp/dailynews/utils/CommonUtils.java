package com.example.myapp.dailynews.utils;

import android.annotation.SuppressLint;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Lenovo on 2017/2/23.
 */

public class CommonUtils {
    //准备格式化小数的类，给定一个格式化规则
    //.00取两位小数点，几个0就是几位小数，不够的补零，#指整数位
    //要使用静态方法，所以需要先把format静态掉
    private static DecimalFormat format = new DecimalFormat("#.00");
    //因为数据需要后面加上kb之类的内容，所以需要拼字符串，此处的返回值为string
    public static String getFileSize(long fileSize){
        if (fileSize < 1024) {
            return fileSize + "B";
        }else if (fileSize < 1024 * 1024) {
            //整数之间相除还是整数，所以需要强装成double，格式化才有用
            return format.format((double)fileSize / 1024) + "KB";
        }else if (fileSize < 1024 * 1024 * 1024) {
            return format.format((double)fileSize / 1024 / 1024) + "MB";
        }else {
            return format.format((double)fileSize / 1024 / 1024 / 1024) + "GB";
        }
    }
    @SuppressLint("SimpleDateFormat")
    public static String formatTime(long time){
        //简单的日期格式化工具
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(time));
    }
}
