package com.example.myapp.dailynews.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.example.myapp.dailynews.R;

public class TestActivity extends AppCompatActivity {
    private Snackbar snackbar;
    private Button btn;
    private String str = "wo";
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btn = (Button) findViewById(R.id.btn_snacke);
    //snackbar.setAction(btn, );
        switch(str){
            case "wo":
            break;
            default:
            break;
        }
    }
}
