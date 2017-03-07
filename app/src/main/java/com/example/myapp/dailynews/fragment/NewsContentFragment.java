package com.example.myapp.dailynews.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.example.myapp.dailynews.R;
import com.example.myapp.dailynews.config.NewsCollect;
import com.example.myapp.dailynews.entity.NewsInfo;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Lenovo on 2017/2/22.
 */

public class NewsContentFragment extends Fragment {
    private View view;
    private WebView mWebView;
    private String newsContentUrl;
    private NewsInfo newsInfo;
    private Toolbar mToolbar;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        newsInfo = getArguments().getParcelable("NewsInfo");
        //newsContentUrl = getArguments().getString("newsUrl");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_fragment_newscontent, null);
        ShareSDK.initSDK(getContext());
        initWidget();

//              final View favorite = getActivity().findViewById(R.id.more);
        //放在外面的时候，可以直接用这个方法来实现监听
//        favorite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //favorite.setBackgroundColor(Color.GRAY);
//                NewsCollect.NewsCollectList = new ArrayList<NewsInfo>();
//                NewsCollect.NewsCollectList.add(newsInfo);
//                Toast.makeText(getActivity(), "收藏成功", Toast.LENGTH_SHORT).show();
//            }
//        });

        collectionNews();

        return view;
    }

    //初始化WebView的控件
    private void initWidget() {
        mWebView = (WebView) view.findViewById(R.id.webview_newscontent);
        //设置能不能支持java脚本语言，不支持的话会错乱
        mWebView.getSettings().setJavaScriptEnabled(true);//Script：java脚本语言
        //设置网页在当前的控件上显示，而不是第三方浏览器
        mWebView.setWebViewClient(new WebViewClient());
        //设置谷歌浏览器，跟上面一样，也是有些方法可以重写
        mWebView.setWebChromeClient(new WebChromeClient());
        //设置结束后，加载网页
        mWebView.loadUrl(newsInfo.url);
//        //里面有些方法可以重写
//        mWebView.setWebViewClient(new WebViewClient(){
//            @Override//当在加载资源的时候
//            public void onLoadResource(WebView view, String url) {
//                super.onLoadResource(view, url);
//            }
//        });//客户端
    }

    //收藏的属性改成ifroom的时候需要使用此方法来实现监听，先找到toolbar的控件
    // 通过给toolbar来设置onmenuitem监听来实现
    private void collectionNews() {
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolBar);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.more:
                        if (NewsCollect.newsCollectList.size() <= 0) {
                            NewsCollect.newsCollectList.add(newsInfo);
                            Toast.makeText(getActivity(), "收藏成功", Toast.LENGTH_SHORT).show();
                        } else {
                            boolean b = false;
                            for (NewsInfo info : NewsCollect.newsCollectList) {
                                if (info.url.equals(newsInfo.url)) {
                                    b = true;
                                    Toast.makeText(getActivity(), "已收藏", Toast.LENGTH_SHORT).show();
                                }
                            }
                            if (!b){
                                NewsCollect.newsCollectList.add(newsInfo);
                                Toast.makeText(getActivity(), "收藏成功", Toast.LENGTH_SHORT).show();
                            }

                        }
                    break;
                    case R.id.share:
                        showShare();
                    break;
                }

                return true;
            }
        });
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle(newsInfo.title);
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl(newsInfo.url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(newsInfo.data);
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(newsInfo.thumbnail_pic_s);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(newsInfo.url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(newsInfo.title);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(newsInfo.url);

// 启动分享GUI
        oks.show(getContext());
    }

}
