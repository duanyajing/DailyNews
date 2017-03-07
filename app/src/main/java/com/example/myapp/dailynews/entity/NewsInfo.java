package com.example.myapp.dailynews.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Lenovo on 2017/2/20.
 */

public class NewsInfo implements Parcelable{
    public String data;//新闻时间
    public String title;//新闻标题
    public String thumbnail_pic_s;//新闻图片的网址
    public String category;//新闻类别，比如是新闻，头条，国际，娱乐之类的
    public String author_name;//作者
    public String url;//新闻内容的网址


    public static final Creator<NewsInfo> CREATOR = new Creator<NewsInfo>() {
        @Override
        public NewsInfo createFromParcel(Parcel in) {
            NewsInfo info = new NewsInfo();
            info.data = in.readString();
            info.title = in.readString();
            info.thumbnail_pic_s = in.readString();
            info.category = in.readString();
            info.author_name = in.readString();
            info.url = in.readString();
            return info;
        }

        @Override
        public NewsInfo[] newArray(int size) {
            return new NewsInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(data);
        dest.writeString(title);
        dest.writeString(thumbnail_pic_s);
        dest.writeString(category);
        dest.writeString(author_name);
        dest.writeString(url);
    }
}
