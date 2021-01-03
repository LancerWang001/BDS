package com.example.bds;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.events.ChangeCmntWayEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class TitleBar extends LinearLayout {
    private Context context;
    private TextView textView;

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.activity_title_bar, this);
        Button titleBack = (Button) findViewById(R.id.title_back);
        Button titleLogout = (Button) view.findViewById(R.id.title_logout);
        textView = (TextView) view.findViewById(R.id.title_text);
        // init title bar with dt cmnt way
        textView.setText(R.string.card_cmnt_dt);

        titleBack.setOnClickListener(v -> {
            System.out.println("返回上一级");
            Intent intent = new Intent();
            intent.setClass(context, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        });
        titleLogout.setOnClickListener(v -> {
            System.out.println("退出");
            Intent intent = new Intent();
            intent.setClass(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
            Toast.makeText(getContext(), "You clicked Edit button", Toast.LENGTH_SHORT).show();
        });
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onMessage(ChangeCmntWayEvent way) {
        textView.setText(way.cmntWay);
    }
}