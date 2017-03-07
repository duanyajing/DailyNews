package com.example.myapp.dailynews.base;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by Lenovo on 2017/2/15.
 */

public class MyBaseFragement extends Fragment {
    public View rootView;
    @Override
    public void onDestroyView() {
        //v4包的BUG，需移除当前视图，防止重复加载相同视图使得程序闪退
        //((ViewGroup)rootView.getParent()).removeView(rootView);
        super.onDestroyView();
    }
}
