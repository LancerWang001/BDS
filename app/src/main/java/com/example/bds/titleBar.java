package com.example.bds;

import android.app.Activity;
import android.content.Intent;
import android.util.AttributeSet;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class titleBar extends LinearLayout {
    private Context context;

    public titleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view =LayoutInflater.from(context).inflate(R.layout.activity_title_bar,this);
        Button titleBack = (Button) findViewById(R.id.title_back);
        Button titleLogout = (Button) view.findViewById(R.id.title_logout);
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("返回上一级");
                Intent intent= new Intent();
                intent.setClass(context, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        titleLogout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("退出");
                Intent intent= new Intent();
                intent.setClass(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                Toast.makeText(getContext(), "You clicked Edit button", Toast.LENGTH_SHORT).show();
            }
        });
    }
}