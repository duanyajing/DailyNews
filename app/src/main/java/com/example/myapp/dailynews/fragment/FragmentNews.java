package com.example.myapp.dailynews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapp.dailynews.R;
import com.example.myapp.dailynews.adapter.NewsFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 2017/2/15.
 */

public class FragmentNews extends Fragment {
    private View view;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<String> mTitles;//tab标签内要显示的问题，也就是导航栏的内容
    private List<Fragment> mFragments;//viewpager内fragment里面要显示的内容
    private String[] mTypes = {"top", "shehui", "guonei", "guoji", "yule", "tiyu", "junshi", "keji", "caijing", "shishang",};//新闻类别
    private NewsFragmentAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_fragment_news, null);
        initWidget();
        initData();
        return view;
    }

    //初始化控件
    private void initWidget() {
        mTabLayout = (TabLayout) view.findViewById(R.id.tablayout_fragmentnews);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager_fragmentnews);
    }

    private void initData() {
        mTitles = new ArrayList<>();
        mTitles.add("头条");
        mTitles.add("社会");
        mTitles.add("国内");
        mTitles.add("国际");
        mTitles.add("娱乐");
        mTitles.add("体育");
        mTitles.add("军事");
        mTitles.add("科技");
        mTitles.add("财经");
        mTitles.add("时尚");

        mFragments = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            NewsListFragment fragment = new NewsListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", mTypes[i]);
            fragment.setArguments(bundle);
            mFragments.add(fragment);
        }
        mAdapter = new NewsFragmentAdapter(getFragmentManager());//如果是在activity中需要v4包下面
        mAdapter.addFragments(mFragments);//添加适配器之前添加数据
        mAdapter.addTitles(mTitles);//添加适配器之前添加数据
        mViewPager.setAdapter(mAdapter);
        //上面的导航和下面的fragment关联的方法
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
