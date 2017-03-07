package com.example.myapp.dailynews.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapp.dailynews.R;
import com.example.myapp.dailynews.activity.MainActivity;
import com.example.myapp.dailynews.adapter.NewsListAdapter;
import com.example.myapp.dailynews.config.NewsConfig;
import com.example.myapp.dailynews.entity.NewsInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Lenovo on 2017/2/20.
 */

public class NewsListFragment extends Fragment {
    private View view;
    private RecyclerView mRecyclerView;
    private String mType;
    //private TextView tv_httpresult;
    private TextView tv_result;
    private String newsUrl;
    private String jsonData;//网络请求返回的json数据
    //private List<NewsInfo> datas;
    private NewsListAdapter adapter;
    private LinearLayoutManager manager;
    private List<NewsInfo> newsDatas;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_fragment_newslist, null);
        mType = getArguments().getString("type");//FragmentNews类中设置的type获取出来
        initWidget();
        return view;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                newsDatas = (List<NewsInfo>) msg.obj;

                adapter.addDatas(newsDatas);
                mRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
    };

    private void initWidget() {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_newslist);
        manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        //tv_httpresult = (TextView) view.findViewById(tv_httpresult);
        //doHttp();
        adapter = new NewsListAdapter();
        adapter.setOnRCVItemClickListener(new NewsListAdapter.OnRCVItemClickListener() {
            @Override
            public void onItemClick(NewsInfo newsInfo) {
                //新建一个fragment然后跳转过去
                //fragmen---fragment之间的通讯,没有办法直接通讯，但是可以通过activity来间接通讯
                //获取到中间通讯也就是mainactivity（宿主）
                NewsContentFragment f = new NewsContentFragment();
                Bundle bundle = new Bundle();//利用Bundle携带数据过去
                bundle.putParcelable("NewsInfo",newsInfo);
                f.setArguments(bundle);
                //因为不知道谁是宿主，所以需要强转
                ((MainActivity)getActivity()).changFragment(f);
            }
        });
        requestNetwork();
    }


    private void requestNetwork() {
        //要求请求的数据网址
        newsUrl = NewsConfig.getNewsUrl(mType);
        new Thread(new Runnable() {//另起子线程，访问网络必须在子线程中操作
            @Override
            public void run() {
                try {
                    //1.初始化OkHttpClient：客户端
                    OkHttpClient client = new OkHttpClient();
                    //准备request因为下面call里面需要
                    Request request = new Request.Builder()
                            .get()//标明是一个get请求
                            .url(newsUrl)
                            .build();
                    //new一个call对象，需要传入Request请求对象，然后执行这个请求
                    //拿到返回给我们的Response响应消息
                    Log.d("run", "-----------------");
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {//判断是否成功，成功就会进入下一步
                        jsonData = response.body().string();//拿到response的主体,然后转成String类型
                        Log.d("NewsListFragment", jsonData);
                        //拿到数据之后放到集合中
                        List<NewsInfo> datas = jsonParse(jsonData);
                        Log.d("run: ",jsonData );
//                        //遍历集合，拿数据
//                        for (NewsInfo info :datas) {
//                            Log.d("news", "-----------------------"+info.title);
//                        }
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = datas;
                        handler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    //解析jsonData
    private List<NewsInfo> jsonParse(String jsonData) {
        List<NewsInfo> newsDatas = new ArrayList<>();
        try {
            //先获取第一个key，然后通过获取出来的key来判断是否成功返回，如果是，继续往下解析
            JSONObject object1 = new JSONObject(jsonData);
            if (object1.getString("reason").equals("成功的返回")) {
                //分别继续解析
                JSONObject object2 = object1.getJSONObject("result");
                JSONArray array = object2.getJSONArray("data");
                //现在到了实体类中的内容，所以需要循环解析，解析出来以后分别赋值给实体类，并放在集合中
                for (int i = 0; i < array.length(); i++) {
                    NewsInfo mData = new NewsInfo();
                    JSONObject o = array.getJSONObject(i);
                    mData.title = o.getString("title");
                    mData.data = o.getString("date");
                    mData.category = o.getString("category");
                    mData.author_name = o.getString("author_name");
                    mData.url = o.getString("url");
                    mData.thumbnail_pic_s = o.getString("thumbnail_pic_s");
                    //拿到数据后因为不止一个，所以需要添加到集合中
                    newsDatas.add(mData);

                }
                return newsDatas;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
//    private void doHttp(){
//        new Thread(new Runnable() {
//            //通过NewsConfig类的静态方法，获取到通过type拼接好的String类型的网址
//            final String newsUrl = NewsConfig.getNewsUrl(mType);
//            InputStream is = null;
//            HttpURLConnection conn = null;
//            @Override
//            public void run() {
//                try {
//                    //拼接好的网址需要转换成URL类型
//                    URL url = new URL(newsUrl);
//                    //使用封装好的url来调用openConnection开启网络
//                    conn = (HttpURLConnection) url.openConnection();
//                    //因为要从网络中获取内容，所以需要使用GET
//                    conn.setRequestMethod("GET");
//                    conn.setConnectTimeout(5000);//时间超时
//                    conn.setReadTimeout(5000);//读取时间超时的设置
//                    conn.connect();//手动连接，但是不连接也可以，因为在获取流的时候会自动连接
//                    final StringBuffer sb;
//                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                        is = conn.getInputStream();//通过网络连接获取流
//                        //把流封装成BufferedReader进行读取
//                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//                        sb = new StringBuffer();
//                        String line;
//                        //循环读取获取到的流
//                        while ((line = reader.readLine()) != null) {
//                            sb.append(line);
//                        }//通过activity，也就是在主线程中修改控件的内容（把读出来的内容获取出来）
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                tv_httpresult.setText(sb.toString());
//                            }
//                        });
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//                        if (is != null) {
//                            is.close();
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    conn.disconnect();
//                }
//            }
//        }).start();
//    }
}
