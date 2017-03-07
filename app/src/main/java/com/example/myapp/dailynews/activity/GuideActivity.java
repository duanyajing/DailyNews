package com.example.myapp.dailynews.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.myapp.dailynews.R;
import com.example.myapp.dailynews.adapter.MyGuideAdapter;
import com.example.myapp.dailynews.base.MyBaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GuideActivity extends MyBaseActivity {

    @Bind(R.id.vp_guide)ViewPager viewPager;//绑定viewpager的资源IDid
    //绑定数组的资源ID
    @Bind({R.id.iv1_guide, R.id.iv2_guide, R.id.iv3_guide})
    ImageView[] imageViews = new ImageView[3];
    private MyGuideAdapter myGuideAdapter;
    private LayoutInflater inflater;
    private boolean isFirst;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);//将视图和控件进行绑定，也就是ButterKnife的初始化
        isFirstRunning();
        initWidget();
    }

    private void initWidget() {
        myGuideAdapter = new MyGuideAdapter();
        getViews();
        viewPager.setAdapter(myGuideAdapter);
        viewPager.addOnPageChangeListener(listener);
    }

    private void getViews() {
        inflater = getLayoutInflater();
        View viewOne = inflater.inflate(R.layout.itemone_viewpager_guide, null);
        View viewTwo = inflater.inflate(R.layout.itemtwo_viewpager_guide, null);
        View viewThree = inflater.inflate(R.layout.itemthree_viewpager_guide, null);
        myGuideAdapter.addViewToAdapter(viewOne);
        myGuideAdapter.addViewToAdapter(viewTwo);
        myGuideAdapter.addViewToAdapter(viewThree);
    }

    //第一步：利用Context提供的getSharedPreferences（）方法，获取SharedPreferences对象
    private void isFirstRunning() {
        //Context.MODE_PRIVATE;私有，写入的会把第一次覆盖掉，最常使用
        //Context.MODE_APPEND;检查文件是否存在，如果存在，追加内容
        //Context.MODE_WORLD_READABLE;当前文件可以被其他文件读取
        //Context.MODE_WORLD_WRITEABLE;表示可以被七天应用写入
        //利用sp对象去获取数据，如果没有取到，默认为true
        SharedPreferences sp = getSharedPreferences("isFirstRunning", Context.MODE_PRIVATE);
        //getPreferences(mode);
        isFirst = sp.getBoolean("isFirst", true);
        //调用sp.edit()方法获取一个Editor对象，用于向sp内存入数据
        SharedPreferences.Editor editor = sp.edit();
        //向sp内存入数据
        editor.putBoolean("isFirst", false);
        //提交此次操作
        editor.commit();
        if (!isFirst) {
            startActivity(LaunchActivity.class);
            finish();
        }
    }

    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //遍历图片数组
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[i].setImageResource(R.drawable.adware_style_default);
            }
            //把当前位置的图片设置为被选中的样式
            imageViews[position].setImageResource(R.drawable.adware_style_selected);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case ViewPager.SCROLL_STATE_DRAGGING:
                    flag = false;
                    break;
                case ViewPager.SCROLL_STATE_SETTLING:
                    flag = true;
                    break;
                case ViewPager.SCROLL_STATE_IDLE:
                    if (viewPager.getCurrentItem() == myGuideAdapter.getCount()-1 && !flag){
                        startActivity(LaunchActivity.class);
                        finish();
                    }
                    flag = true;
                    break;
            }
        }
    };
}
