package com.example.myapp.dailynews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapp.dailynews.R;
import com.example.myapp.dailynews.activity.MainActivity;
import com.example.myapp.dailynews.adapter.FavoriteAdapter;
import com.example.myapp.dailynews.config.NewsCollect;
import com.example.myapp.dailynews.entity.NewsInfo;

import java.util.List;

/**
 * Created by Lenovo on 2017/2/15.
 */

public class FragmentFavorite extends Fragment {
    private View view;
    private RecyclerView mRecycler;
    private LinearLayoutManager manager;
    private FavoriteAdapter mAdapter;
    private List<NewsInfo> info;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_fragment_favorite, null);
        initWidget();
        return view;
    }
    private void initWidget(){
        mRecycler = (RecyclerView) view.findViewById(R.id.recyclerview_favorite);
        manager = new LinearLayoutManager(getContext());//给控件设置管理者
        info = NewsCollect.newsCollectList;//给集合赋值
        mRecycler.setLayoutManager(manager);
        mAdapter = new FavoriteAdapter();
        mAdapter.setOnRCVItemClickListener(new FavoriteAdapter.OnRCVItemClickListener() {
            @Override
            public void onItemClick(NewsInfo newsInfo) {
                NewsContentFragment fragment = new NewsContentFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("NewsInfo",newsInfo);
                fragment.setArguments(bundle);
                ((MainActivity)getActivity()).changFragment(fragment);
            }
        });
        mAdapter.addDatas(info);
        mRecycler.setAdapter(mAdapter);
    }
}
