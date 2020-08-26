package com.example.bds;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class titleBar extends LinearLayout {

    public titleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.activity_title_bar, this);
//        Button titleBack = (Button) findViewById(R.id.title_back);
        Button titleLogout = (Button) findViewById(R.id.title_logout);
        HomeActivity a = (HomeActivity) context;
//        titleBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("返回上一级");
//
//            }
//        });
        titleLogout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("退出");
                HomeActivity a = (HomeActivity) titleBar.this.getContext();
                Intent intent = new Intent(a, MainActivity.class);
                Toast.makeText(getContext(), "You clicked Edit button", Toast.LENGTH_SHORT).show();
            }
        });
    }
}