package com.example.bds.layouts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bds.HomeActivity;
import com.example.bds.MainActivity;
import com.example.bds.R;
import com.example.events.ChangeCmntWayEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class TitleBar extends LinearLayout {
    private Context context;
    private TextView textView;
    private AlertDialog dialog;

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.activity_title_bar, this);
        Button titleBack = (Button) findViewById(R.id.title_back);
        titleBack.setTextSize(30);
        Button titleLogout = (Button) view.findViewById(R.id.title_logout);
        titleLogout.setTextSize(30);
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
            this.dialog.show();
        });
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onMessage(ChangeCmntWayEvent way) {
        textView.setText(way.cmntWay);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        this.dialog = createDialog(child);
    }

    private AlertDialog createDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.card_dialog_exit);
        builder.setPositiveButton(R.string.card_dialog_alert_Y, (dialogInterface, i) -> {
            if (context != null) {
                ((HomeActivity) context).bdsService.closeSevice();
            }
            Intent intent = new Intent();
            intent.setClass(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
            Toast.makeText(getContext(), "You clicked Edit button", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton(R.string.card_dialog_alert_N, (dialogInterface, i) -> {

        });
        return builder.create();
    }
}