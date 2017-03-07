package com.example.myapp.dailynews.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.myapp.dailynews.R;
import com.example.myapp.dailynews.utils.ShareSDKUtils;

import cn.sharesdk.tencent.qzone.QZone;


/**
 * Created by Administrator on 2017-2-24.
 */

public class LoadingActivity extends AppCompatActivity {
    Button wenxin, QQ, regist;
    RelativeLayout more;
    LinearLayout moremenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading);
        setView();
    }

    private void setView() {
        QQ = (Button) findViewById(R.id.QQ);
        wenxin = (Button) findViewById(R.id.wenxin);
        more = (RelativeLayout) findViewById(R.id.more);
        moremenu = (LinearLayout) findViewById(R.id.moremenu);
        regist = (Button) findViewById(R.id.regist);
        regist.setOnClickListener(l);
        more.setOnClickListener(l);
        QQ.setOnClickListener(l);
    }

    View.OnClickListener l = new View.OnClickListener() {
        boolean b = true;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.QQ:
                    ShareSDKUtils.Login(QZone.NAME, LoadingActivity.this);
                    finish();
                    break;
                case R.id.more:
                    if (b == true) {
                        b = false;
                        moremenu.setVisibility(View.VISIBLE);
                    } else {
                        b = true;
                        moremenu.setVisibility(View.GONE);
                    }
                    break;
                case R.id.regist:
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);// 应用过滤条件
                    intent.setData(Uri.parse("https://ssl.zc.qq.com/chs/index.html"));
                    startActivity(intent);// 现在只能跳转到APP2的入口界面
                    finish();
                    break;
            }

        }
    };
}
