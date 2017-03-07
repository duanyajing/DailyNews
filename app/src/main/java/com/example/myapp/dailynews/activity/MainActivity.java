package com.example.myapp.dailynews.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.dailynews.R;
import com.example.myapp.dailynews.base.MyBaseActivity;
import com.example.myapp.dailynews.fragment.FragmentComment;
import com.example.myapp.dailynews.fragment.FragmentFavorite;
import com.example.myapp.dailynews.fragment.FragmentLocal;
import com.example.myapp.dailynews.fragment.FragmentNews;
import com.example.myapp.dailynews.fragment.FragmentPhoto;
import com.example.myapp.dailynews.fragment.NewsContentFragment;
import com.example.myapp.dailynews.utils.CameraAlbumUtil;
import com.example.myapp.dailynews.utils.ImageLoader;
import com.example.myapp.dailynews.utils.PermissionUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends MyBaseActivity {
    //private FragmentDefault fragmentDefault;
    private FragmentNews fragmentNews;
    private FragmentFavorite fragmentFavorite;
    private FragmentLocal fragmentLocal;
    private FragmentComment fragmentComment;
    private FragmentPhoto fragmentPhoto;
    private ImageView iv_headPic;
    private Uri headPicUri;
    private CameraAlbumUtil cameraAlbumUtil;
    private String UserName;
    private String res;
    private ImageLoader imageLoader;
    private Bitmap bitmap;
    private View headerLayout;
    private TextView tv_name;
    @Bind(R.id.toolBar)
    Toolbar toolbar;
    //策划菜单的使用：
    //DrawerLayout:抽屉布局
    //NavigationMenuView:侧滑菜单，与抽屉布局一起使用，等于把侧滑菜单放在抽屉布局中使用
    //需要俩个东西--menu + headerLayout
    //找侧滑菜单的对象,绑定
    @Bind(R.id.activity_main)
    DrawerLayout drawerLayout;
    @Bind(R.id.nav)
    NavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);//需要先把toolbar的属性设置好然后再使用他


        cameraAlbumUtil = new CameraAlbumUtil(this);


        SharedPreferences sp = getSharedPreferences("isFirstRunnings", Context.MODE_PRIVATE);
        UserName = sp.getString("UserName", "");
        res = sp.getString("res", "");
        getinformation();

        initToolBar();
        initNavigation();
        intiHeaderLayout();
        changFragment(new FragmentNews());
    }

    public void initToolBar() {
        setTitle("");//设置文本为空的
        setSupportActionBar(toolbar);//调用此方法之后可以使用actionbar的所有功能
        ActionBar actionBar = getSupportActionBar();//通过上面设置的actionbar获取出来
        actionBar.setDisplayHomeAsUpEnabled(true);//Display：展示显示 HomeAsUp：左边的按钮 此条属性就是设置左边按钮能否被显示
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);//设置HomeAsUp的图片
    }

    public void initNavigation() {
        nav.setCheckedItem(R.id.news);//头条设置的灰色，一旦进去之后自己就默认为灰色
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.news:
                        if (fragmentNews == null) {
                            fragmentNews = new FragmentNews();
                        }
                        changFragment(fragmentNews);
                        drawerLayout.closeDrawers();//一旦点击以后就会关闭菜单栏
                        break;
                    case R.id.favorite:
                        if (fragmentFavorite == null) {
                            fragmentFavorite = new FragmentFavorite();
                        }
                        changFragment(fragmentFavorite);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.local:
                        if (fragmentLocal == null) {
                            fragmentLocal = new FragmentLocal();
                        }
                        drawerLayout.closeDrawers();
                        changFragment(fragmentLocal);
                        break;
                    case R.id.comment:
                        if (fragmentComment == null) {
                            fragmentComment = new FragmentComment();
                        }
                        drawerLayout.closeDrawers();
                        changFragment(fragmentComment);
                        break;
                    case R.id.photo:
                        if (fragmentPhoto == null) {
                            fragmentPhoto = new FragmentPhoto();
                        }
                        drawerLayout.closeDrawers();
                        changFragment(fragmentPhoto);
                        break;
                }
                return true;
            }
        });
    }


    @Override//当选项菜单被创建的时候
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);//把toolbarxml文件加载
        return true;//让菜单显示
    }

    @Override//当选项菜单具体某个item被选中的时候
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();
                break;
//            case R.id.more://已经在新闻类中设置
//                Toast.makeText(this, "more", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.help:
//                Toast.makeText(this, "help", Toast.LENGTH_SHORT).show();
//                break;
            case R.id.comment:
                Toast.makeText(this, "comment", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home://android下面的键的名字，一直是这个名字，但是要用android点
                drawerLayout.openDrawer(Gravity.LEFT);//打开抽屉布局,因为xml是gravity属性，所以需要设置Gravity下面的属性
                break;

        }
        return true;
    }

    public void changFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        //添加到返回栈
        if (fragment instanceof NewsContentFragment) {
            //把事务添加到返回栈
            transaction.addToBackStack(null);
        }
        transaction.replace(R.id.fl_commcontent_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //初始化侧滑菜单的头布局
    private void intiHeaderLayout() {
        //先找到头布局，利用NavigationView
        headerLayout = nav.getHeaderView(0);
        //再在头布局内找到对应控件
        iv_headPic = (ImageView) headerLayout.findViewById(R.id.iv_headPic);
        iv_headPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("isFirstRunnings", Context.MODE_PRIVATE);
                String name = sp.getString("UserName","");
                String image = sp.getString("res", "");

                chooseDailog();
            }
        });

    }

    private void chooseDailog() {
        new AlertDialog.Builder(this).setTitle("选择头像")
        .setPositiveButton("相机", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PermissionUtil.requestPermissions(MainActivity.this, 111, new String[]{Manifest.permission.CAMERA}, new PermissionUtil.OnRequestPermissionListener() {
                    @Override
                    public void onRequestGranted() {
                        cameraAlbumUtil.takePhoto();
                    }
                    @Override
                    public void onRequestDenied() {
                        Toast.makeText(MainActivity.this, "需要获取所有权限", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        })
        .setNegativeButton("相册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PermissionUtil.requestPermissions(MainActivity.this, 111, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionUtil.OnRequestPermissionListener() {
                    @Override
                    public void onRequestGranted() {
                        cameraAlbumUtil.openAlbum();
                    }

                    @Override
                    public void onRequestDenied() {
                        Toast.makeText(MainActivity.this, "权限被拒绝", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        })
        .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public void getinformation() {
        imageLoader = new ImageLoader(this);
        bitmap = imageLoader.loadImage(res, new ImageLoader.OnLoadImageListener() {
            @Override
            public void onImageLoadOk(String url, Bitmap bitmap) {
                headerLayout = nav.getHeaderView(0);
                iv_headPic = (CircleImageView) headerLayout.findViewById(R.id.iv_headPic);
                iv_headPic.setImageBitmap(bitmap);
                tv_name = (TextView) headerLayout.findViewById(R.id.tv_name);
                tv_name.setText(UserName);
            }

            @Override
            public void onImageLoadError(String url) {

            }
        } );
        headerLayout = nav.getHeaderView(0);
        iv_headPic = (CircleImageView) headerLayout.findViewById(R.id.iv_headPic);
        iv_headPic.setImageBitmap(bitmap);
        tv_name = (TextView) headerLayout.findViewById(R.id.tv_name);
        tv_name.setText(UserName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        bitmap = CameraAlbumUtil.onActivityResult(requestCode, resultCode, data);
        if (bitmap != null){
            iv_headPic.setImageBitmap(bitmap);
        }
//        super.onActivityResult(requestCode.resultCode,data);
//        switch(requestCode){
//            case CameraAlbumUtil.TAKE_PHOTO:
//                if (resultCode == RESULT_OK){
//                    cameraAlbumUtil.cutImageByCamera();//裁剪图片
//                }
//                break;
//            case CameraAlbumUtil.ACTIVITY_RESULT_CAMERA:
//                    Bitmap bitmap = cameraAlbumUtil.displayBitmap();
//                    iv_headPic.setImageBitmap(bitmap);
//                break;
//        }
    }


}
