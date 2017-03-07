package com.example.myapp.dailynews.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.example.myapp.dailynews.R;
import com.example.myapp.dailynews.base.MyBaseActivity;
import com.mob.tools.utils.UIHandler;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;


public class DisembarkActivity extends MyBaseActivity implements Callback, OnClickListener, PlatformActionListener {
    private static final int MSG_USERID_FOUND = 1;
    private static final int MSG_LOGIN = 2;
    private static final int MSG_AUTH_CANCEL = 3;
    private static final int MSG_AUTH_ERROR = 4;
    private static final int MSG_AUTH_COMPLETE = 5;
    private boolean isRegister;
    static Platform platform;
    HashMap<String, Object> res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(this);
        setContentView(R.layout.activity_disembark);
        findViewById(R.id.wechat).setOnClickListener(this);
        findViewById(R.id.tvQq).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        ShareSDK.stopSDK(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wechat: {
                authorize(new Wechat(this));
            }
            break;
            case R.id.tvQq: {
                authorize(new QQ(this));
            }
            break;
        }
    }

    private void authorize(Platform plat) {
        if (plat.isValid()) {
            String userId = plat.getDb().getUserId();
            if (!TextUtils.isEmpty(userId)) {
                UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
                login(plat.getName(), userId, null);
                return;
            }
        }
        plat.setPlatformActionListener(this);
        plat.SSOSetting(false);
        plat.showUser(null);
    }

    @Override
    public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
        DisembarkActivity.platform = platform;
        this.res = res;
 //       Log.d(TAG, String.valueOf(res));
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE, this);
            login(platform.getName(), platform.getDb().getUserId(), res);
        }
    }

    @Override
    public void onError(Platform platform, int action, Throwable t) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
        }
        t.printStackTrace();
    }

    @Override
    public void onCancel(Platform platform, int action) {
        if (action == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
        }
    }

    private void login(String plat, String userId, HashMap<String, Object> userInfo) {
        Message msg = new Message();
        msg.what = MSG_LOGIN;
        msg.obj = userInfo;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_USERID_FOUND: {
                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                SharedPreferences sp = getSharedPreferences("isFirstRunning", Context.MODE_PRIVATE);
                //添加进Boolean值
                isRegister = sp.getBoolean("isRegister", true);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("isRegister", false);
                //提交
                editor.commit();
                if (!isRegister) {
                    startActivity(MainActivity.class);
                    DisembarkActivity.this.finish();
                }
            }
            break;
            case MSG_LOGIN: {
  //              String text = getString("使用%s帐号登录中…", msg.obj);
                Toast.makeText(this, "使用%s帐号登录中…", Toast.LENGTH_SHORT).show();
                startActivity(MainActivity.class);
                DisembarkActivity.this.finish();
            }
            break;
            case MSG_AUTH_CANCEL: {
                Toast.makeText(this, "授权操作已取消", Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_AUTH_ERROR: {
                Toast.makeText(this, "授权操作遇到错误，请阅读Logcat输出", Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_AUTH_COMPLETE: {
                Toast.makeText(this,"授权成功，正在跳转登录操作…", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = getSharedPreferences("isFirstRunnings", Context.MODE_PRIVATE).edit();
                Log.d("aaaaaaaaaaaaaaaaaaa", String.valueOf(platform));
                editor.putString("UserName", (String) res.get("nickname"));
                editor.putString("res", (String) res.get("figureurl_qq_1"));
                editor.apply();
                startActivity(MainActivity.class);
                DisembarkActivity.this.finish();
            }
            break;
        }
        return false;
    }
}