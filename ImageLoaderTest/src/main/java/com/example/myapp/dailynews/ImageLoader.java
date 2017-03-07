package com.example.myapp.dailynews;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.LruCache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Lenovo on 2017/2/23.
 */

//图片加载及缓存的工具类
//url----图片----存到内存，存到文件
public class ImageLoader {
    Context context;
    private List<String> permissionList = new ArrayList<>();

    //key:根据什么去取缓存的东西,一般是使用网址
    //value：要存的东西
    private static LruCache<String, Bitmap> lruCache;//公用一个空间，静态
    private File path;

    public ImageLoader(Context context) {
        this.context = context;
        requestPermission();
        //运行内存的算法，一般是给这个设置八分之一的，或者直接写4*1024*1024不过一般都是按下面的算法
        int size = (int) (Runtime.getRuntime().maxMemory() / 8);
        lruCache = new LruCache<String, Bitmap>(size) {
            @Override//用来测量每张图片的大小
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();//获取出来图片的大小然后返回去
                //获取一行的字节数然后乘以高，不过API版本更低（每一行的字节数*行数）
                //return value.getRowBytes()*value.getHeight();
            }
        };
        //后面的key和value写出来，后面是写缓存内存大小
        //获得sd卡的内存目录或者getCacheDir手机内存
        //获取缓存目录不需要权限//android/data/包名
        path = context.getExternalCacheDir();
        if (!path.exists()) {//如果文件夹的路径不存在
            path.mkdirs();//创建文件夹
        }
    }

    public interface OnLoadImageListener {
        void onImageLoadOk(String url, Bitmap bitmap);

        void onImageLoadError(String url);
    }

    public Bitmap loadImage(String url, OnLoadImageListener l) {
        Bitmap bitmap = null;
        //内存取
        bitmap = getFromCache(url);
        if (bitmap != null) {
            return bitmap;
        }

        //内存取没有的话从文件取
        bitmap = getFromFile(url);
        if (bitmap != null) {
            return bitmap;
        }

        //文件取没有的话网络下载
        getFromNet(url, l);
        return null;
    }

    //把图片存到内存去
    private void savaToCache(String url, Bitmap bitmap) {
        lruCache.put(url, bitmap);
    }

    //从内存获取图片
    private Bitmap getFromCache(String url) {
        return lruCache.get(url);
    }

    //把图片放到文件去
    private void savaToFile(String url, Bitmap bitmap) {
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        OutputStream os = null;
        try {
            os = new FileOutputStream(new File(path, fileName));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);//把图片压缩到某个位置去
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //从文件去取
    private Bitmap getFromFile(String url) {
        String fileName = url.substring(url.lastIndexOf('/') + 1);//根据文件名字来拿
        File file = new File(path, fileName);
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());//获取出来绝对完整的路径
            if (bitmap != null) {
                savaToCache(url, bitmap);//既然能进这个方法，说明没在内存中拿到，所以还需要再存一份到内存中去
                return bitmap;
            }
        }
        return null;
    }

    private void getFromNet(String url, OnLoadImageListener l) {
        MyAsyncTask task = new MyAsyncTask(l);
        task.execute(url);
    }

    //参数1：params：参数，执行该任务需要传入什么参数，比如要下载图片，需要传网址
    //参数2：progress：进度，执行该任务是否需要在界面显示进度，如果需要，传入进度的数据类型
    //参数3：result：结果，执行该任务需要返回的数据
    private class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private String newsUrl;
        private OnLoadImageListener l;

        public MyAsyncTask(OnLoadImageListener l) {
            this.l = l;
        }


        @Override//默认在子线程运行，用于执行后台任务，不用再new线程
        protected Bitmap doInBackground(String... params) {
            newsUrl = params[0];
            Bitmap bitmap = doNetwork();
            return bitmap;//获取到之后被传递到onPostExecute方法中
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                savaToCache(newsUrl, bitmap);//能走这个方法是因为内存和文件中都没有
                savaToFile(newsUrl, bitmap);
                l.onImageLoadOk(newsUrl, bitmap);
            } else {
                l.onImageLoadError(newsUrl);
            }
        }

        private Bitmap doNetwork() {
            InputStream is = null;
            try {
                URL url = new URL(newsUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(20 * 1000);
                conn.setReadTimeout(20 * 1000);
                is = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                if (bitmap != null) {
                    return bitmap;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    }

    // 权限获取
    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission
                .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission
                .READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission
                .READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }
}
