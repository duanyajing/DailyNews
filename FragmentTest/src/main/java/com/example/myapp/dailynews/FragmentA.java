package com.example.myapp.dailynews;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Lenovo on 2017/2/15.
 */
//Fragment有两个包，一个app下面，一个是V4包下面，只是V4的可以兼容以下版本
public class FragmentA extends Fragment {
    public final String TAG = "tag";
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: fragmentA被创建");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: fragmentA");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: fragmentA");
        //把布局加载进来，并把视图返回出去
        View view = inflater.inflate(R.layout.layout_fragmenta,null);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated: fragmentA");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: fragmentA");
    }
//以后就处于活动期
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: fragmentA");
    }
//失去焦点
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: fragmentA");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: fragmentA");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: fragmentA");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: fragmentA");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: fragmentA");
    }
}
