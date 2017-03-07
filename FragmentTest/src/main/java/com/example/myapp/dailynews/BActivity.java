package com.example.myapp.dailynews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class BActivity extends AppCompatActivity implements FragmentLeft.OnButtonClickListenr {
    private FragmentManager fm;
    private FragmentTransaction transaction;
    private Fragment fragmentA, fragmentB, fragmentC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
        fragmentA = new FragmentA();
        fragmentB = new FragmentB();
        fragmentC = new FragmentC();
        showDefaultFragment();

    }

    private void showDefaultFragment(){
        fm = getSupportFragmentManager();
        transaction = fm.beginTransaction();
        transaction.replace(R.id.framelayout_left, new FragmentLeft());
        transaction.replace(R.id.framelayout_right, fragmentA);
        transaction.commit();
    }
    private void changeFragment(Fragment fragment){
        FragmentManager ffm = getSupportFragmentManager();
        FragmentTransaction ft = ffm.beginTransaction();
        ft.replace(R.id.framelayout_right, fragment);
        ft.addToBackStack(null);//添加到返回栈，就不用再一按返回键就直接退回程序
        ft.commit();
    }

    @Override
    public void onChanged(View view) {
        switch (view.getId()) {
            case R.id.button:
                changeFragment(fragmentA);
                break;
            case R.id.button2:
                changeFragment(fragmentB);
                break;
            case R.id.button3:
                changeFragment(fragmentC);
                break;
        }
    }
}

