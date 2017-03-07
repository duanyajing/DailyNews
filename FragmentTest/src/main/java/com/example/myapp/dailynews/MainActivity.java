package com.example.myapp.dailynews;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "TAG";
    private FragmentManager fm;
    private FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Log.d(TAG, "MainActivity-------");
        //拿到FragmentManager的对象
        FragmentManager fm = getSupportFragmentManager();
        //让管理者开始一个事务
        FragmentTransaction transaction = fm.beginTransaction();
        //把fragmentA这个碎片添加到容器中，也就是在activity中添加的一个放置fragment的容器
        transaction.add(R.id.framelayout,new FragmentA());
        //提交事务
        transaction.commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "MainActivity-------onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "MainActivity-------onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "MainActivity-------onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "MainActivity-------onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MainActivity-------onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "MainActivity-------onRestart");
    }
}
