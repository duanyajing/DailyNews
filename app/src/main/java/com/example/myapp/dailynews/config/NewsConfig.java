package com.example.myapp.dailynews.config;

/**
 * Created by Lenovo on 2017/2/20.
 */


public class NewsConfig {
    public static String getNewsUrl(String newsType){
        //通过StringBuffer把要获取的网址进行拼接
        StringBuffer sb = new StringBuffer();
        sb.append("http://v.juhe.cn/toutiao/index?type=");
        sb.append(newsType);
        sb.append("&key=e6400393dcfa32d175508add327a698c");
        return sb.toString();
    }
}
