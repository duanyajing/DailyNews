package com.example.myapp.dailynews;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button btn;
    private ImageView imageView;
    private String url;
    private ImageLoader imageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url = "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png";

        imageLoader = new ImageLoader(this);
        btn = (Button) findViewById(R.id.button);
        imageView = (ImageView) findViewById(R.id.imageView);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = imageLoader.loadImage(url, l);
                imageView.setImageBitmap(bitmap);//如果不进网络下载的时候直接从文件拿了设置
            }
        });

    }
    private ImageLoader.OnLoadImageListener l = new ImageLoader.OnLoadImageListener() {
        @Override
        public void onImageLoadOk(String url, Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }

        @Override
        public void onImageLoadError(String url) {
            Toast.makeText(MainActivity.this, "加载出错", Toast.LENGTH_SHORT).show();
        }
    };
}
