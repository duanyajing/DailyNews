package com.example.myapp.dailynews.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.myapp.dailynews.utils.LogUtils;

import java.util.ArrayList;

/**
 * Created by Lenovo on 2017/2/13.
 */

public class MyBaseActivity extends AppCompatActivity {
    //创建一个集合用于存放所有的activity
    private static ArrayList<MyBaseActivity> activities = new ArrayList<MyBaseActivity>();

    //一键退出
    public void finishAll() {
        for (int i = 0; i < activities.size(); i++) {
            activities.get(i).finish();
        }
    }


    //--------------activity的跳转--------------
    protected void startActivity(Class<?> targetClass){
        Intent intent = new Intent(this,targetClass);
        startActivity(intent);
    }

    protected void startActivity(Class<?> targetClass,Bundle bundle){
        Intent intent = new Intent(this,targetClass);
        //Bundle bundle = new Bundle();
        //bundle.putString("key","aaa");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    protected void startActivity(Class<?> targetClass,int enterAnim,int exitAnim){
        Intent intent = new Intent(this,targetClass);
        startActivity(intent);
        overridePendingTransition(enterAnim, exitAnim);//需要写在这个后面
    }
    protected void startActivity(Class<?> targetClass,Bundle bundle,int enterAnim,int exitAnim){
        Intent intent = new Intent(this,targetClass);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(enterAnim, exitAnim);
    }



    //--------------管理声明周期--------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ButterKnife.bind(this);
        activities.add(this);
        LogUtils.d(getClass().getSimpleName(), "----------onCreate");
    }
    protected void onStart() {
        super.onStart();
        LogUtils.d(this, "-----onStart");
    }
    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d(this, "------onResume");
    }
    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d(this, "-----onPause");
    }
    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.d(this, "-----onStop");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        activities.remove(this);
        LogUtils.d(this, "------onDestroy");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.d(this, "------onRestart");
    }
}
