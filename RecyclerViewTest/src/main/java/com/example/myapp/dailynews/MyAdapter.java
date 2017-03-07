package com.example.myapp.dailynews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Lenovo on 2017/2/20.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<JavaBean> datas;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;

        //构造方法内的参数view指的是RecyclerView内的item的最外层布局
        //一般要根据参数找父布局
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            
        }
    }


    @Override//主要用于初始化viewHolder
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recyclerview, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override//用来设置数据
    public void onBindViewHolder(ViewHolder holder, int position) {
        JavaBean data = datas.get(position);
        holder.imageView.setImageBitmap(data.bitmap);
        holder.textView.setText(data.title);
    }

    @Override//获取
    public int getItemCount() {
        return datas.size();
    }

    public void addDatas(List<JavaBean> datas) {
        this.datas = datas;
    }


}
