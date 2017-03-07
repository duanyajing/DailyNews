package com.example.myapp.dailynews.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapp.dailynews.R;
import com.example.myapp.dailynews.entity.NewsInfo;
import com.example.myapp.dailynews.utils.CommonUtils;
import com.example.myapp.dailynews.utils.ImageLoader;

import java.util.List;

/**
 * Created by Lenovo on 2017/2/22.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private List<NewsInfo> datas;
    private ImageLoader imageLoader;
    private ViewHolder mViewHolder;
    //private AdapterView.OnItemLongClickListener ll;
    private FavoriteAdapter.OnRCVItemClickListener listener;//声明监听器

    //回调接口，用来监听item的监听器
    public interface  OnRCVItemClickListener{
        void onItemClick(NewsInfo newsInfo);//往哪跳，需要将网址传递下去
    }

    public void setOnRCVItemClickListener(FavoriteAdapter.OnRCVItemClickListener l){
        this.listener = l;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fragment_favorite, null);
        imageLoader = new ImageLoader(parent.getContext());
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.newsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    int position = viewHolder.getAdapterPosition();
                    NewsInfo newsInfo = datas.get(position);
                    listener.onItemClick(newsInfo);//设置监听跳过去

                }
            }
        });
//        viewHolder.newsView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                ll.onItemLongClick(v,getItemCount());
//                return true;
//            }
//        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mViewHolder = holder;
        NewsInfo data = datas.get(position);
        holder.tv_title.setText(data.title);
        holder.tv_data.setText(data.data);
        holder.tv_favorite.setText(CommonUtils.formatTime(System.currentTimeMillis()));
        holder.imageView.setImageResource(R.mipmap.ic_launcher);
        Bitmap bitmap = imageLoader.loadImage(data.thumbnail_pic_s, l);
        if (bitmap != null){

            holder.imageView.setImageBitmap(bitmap);
        }
    }

    public ImageLoader.OnLoadImageListener l = new ImageLoader.OnLoadImageListener() {
        @Override
        public void onImageLoadOk(String url, Bitmap bitmap) {
            mViewHolder.imageView.setImageBitmap(bitmap);
        }

        @Override
        public void onImageLoadError(String url) {
            mViewHolder.imageView.setImageResource(R.mipmap.ic_launcher);
            //Toast.makeText(, "加载出错", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public int getItemCount() {
        return datas.size();
    }
    public void addDatas(List<NewsInfo> datas) {
        this.datas = datas;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        View newsView;
        private ImageView imageView;
        private TextView tv_title;
        private TextView tv_data;
        private TextView tv_favorite;
        public ViewHolder(View view) {
            super(view);
            newsView = view;
            imageView = (ImageView) view.findViewById(R.id.iv_item_favoritepic);
            tv_title = (TextView) view.findViewById(R.id.tv_item_favoritetitle);
            tv_data = (TextView) view.findViewById(R.id.tv_item_favoritenewsdata);
            tv_favorite = (TextView) view.findViewById(R.id.tv_item_favoritedata);
        }
    }
}
