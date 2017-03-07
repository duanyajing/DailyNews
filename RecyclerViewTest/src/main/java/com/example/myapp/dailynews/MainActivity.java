package com.example.myapp.dailynews;

import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MyAdapter adapter;
    private List<JavaBean> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        //以下三个步骤是设置横向还是纵向
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);

        adapter = new MyAdapter();
        initData();
        adapter.addDatas(datas);
        mRecyclerView.setAdapter(adapter);

    }
    private void initData(){
        datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            JavaBean data = new JavaBean();
            data.bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
            data.title = "xq";
            datas.add(data);
        }
    }
}
