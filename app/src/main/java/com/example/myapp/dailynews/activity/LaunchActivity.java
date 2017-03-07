package com.example.myapp.dailynews.activity;

import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapp.dailynews.R;
import com.example.myapp.dailynews.base.MyBaseActivity;

import java.io.FileDescriptor;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 启动页面的activity
 */
public class LaunchActivity extends MyBaseActivity {
    //MediaPlayer相当于播放器一样，利用他就可以播放视频
    //1、播放什么东西--数据源（格式，视频只支持mp4,3gp等最基本的格式，如果想要其他的视频需要解码）
    //2、到哪播放---SurfaceView（缺点：不能对视频进行一些操作，平移，旋转，缩放）、TextureView（带缓冲的View，相当于一个view），相当于一个载体
    @Bind(R.id.textureView) TextureView textureView;//已经做了findviewbyid的事情了
    @Bind(R.id.btn_signin_launch) Button btn_signin_launch;
    @Bind(R.id.btn_signup_launch) Button btn_signup_launch;
    private MediaPlayer player;
    private int pausePosition;//暂停时的位置
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ButterKnife.bind(this);
        textureView.setSurfaceTextureListener(l);//给textureView设置监听
    }

    //给登录控件设置点击事件
    @OnClick(R.id.btn_signin_launch)
    public void login(){
        startActivity(DisembarkActivity.class);
        LaunchActivity.this.finish();
    }
    //给注册控件设置点击事件
    @OnClick(R.id.btn_signup_launch)
    public void sigup(){
        Toast.makeText(this, "你点击了注册按钮", Toast.LENGTH_SHORT).show();
    }
    private TextureView.SurfaceTextureListener l = new TextureView.SurfaceTextureListener() {

        @Override//SurfaceTextureListener准备好了可用的时候使用的方法
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
            try {
                player = new MediaPlayer();
                //设置要播放的数据，也就是播放源
                //拿到Assets下的数据资源，并返回一个AssetFileDescriptor类型的对象
                AssetFileDescriptor afd = getAssets().openFd("JySuisJamaisAlle.mp4");//使用这个方法可以直接打开需要的文件类型
                FileDescriptor fd = afd.getFileDescriptor();//把AssetFileDescriptor转换成FileDescriptor
                //afd.getStartOffset()是偏移量，从什么时候开始，afd.getLength()的长度，都要有，否则播放的时候报错
                player.setDataSource(fd,afd.getStartOffset(),afd.getLength());//如果放音频不需要载体
                Surface surface = new Surface(surfaceTexture);
                player.setSurface(surface);//表面缓冲作用
                player.setLooping(true);//设置循环播放
                player.prepareAsync();//异步准备，可能会准备10分钟，准备异步了就不需要再等了
                player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        //准备好了之后要做的事情
                        player.start();//调用start方法启用
                    }
                });//当他准备好了的监听器，主播好了做一些事情

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override//SurfaceTextureListener大小改变的时候
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override//SurfaceTextureListener销毁的时候
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override//SurfaceTextureListener更新的时候
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };
    @Override
    protected void onStop() {
        super.onStop();
        if (player.isPlaying()) {//如果正在播放，暂停
            //当视频不可见的时候，也就是在后台运行的时候暂停运行
            player.pause();
            pausePosition = player.getCurrentPosition();//获取当前的位置
        }
    }
    @Override//后台恢复到前台以后继续播放
    protected void onRestart() {
        super.onRestart();
        if (!player.isPlaying()) {//如果没有在播放
            player.seekTo(pausePosition);//重启的时候把上一次的位置给到他，再继续
            player.start();
        }
    }
    @Override//被销毁的时候立即停止
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.stop();
            player.release();//资源回收，释放资源
            player = null;
        }
    }
}
