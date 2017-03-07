package com.example.myapp.dailynews;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Lenovo on 2017/2/15.
 */

public class FragmentLeft extends Fragment {
    private Button btn_1, btn_2, btn_3;
    public  OnButtonClickListenr click;

    //当fragment和activity依附的时候
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //看某个类有没有实现某个接口
        if (context instanceof OnButtonClickListenr){
            click = (OnButtonClickListenr) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment_left,null);
        btn_1 = (Button) view.findViewById(R.id.button);
        btn_1.setOnClickListener(l);
        btn_2 = (Button) view.findViewById(R.id.button2);
        btn_2.setOnClickListener(l);
        btn_3 = (Button) view.findViewById(R.id.button3);
        btn_3.setOnClickListener(l);
        return view;
    }
    private View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button:
                    if (click != null) {
                        click.onChanged(btn_1);
                    }
                    break;
                case R.id.button2:
                    if (click != null) {
                        click.onChanged(btn_2);
                    }
                    break;
                case R.id.button3:
                    if (click != null) {
                        click.onChanged(btn_3);
                    }
                    break;
            }
        }

    };
//接口回调模式
    public interface OnButtonClickListenr{
      public void onChanged(View view);
    };
}
