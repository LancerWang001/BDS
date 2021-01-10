package com.example.bds;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.events.EmittSocketEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CmntStatus extends RelativeLayout {

    private String TAG = "CmntStatus";

    View bdBulb;
    View dtBulb;

    public CmntStatus(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        };

        View view = LayoutInflater.from(context).inflate(R.layout.layout_cmnt_status, this);
        bdBulb = findViewById(R.id.bd_bulb);
        dtBulb = findViewById(R.id.dt_bulb);

        HomeActivity acct = (HomeActivity) getContext();
        if (acct.bdsService.COMMUNICATE_WAY == R.string.card_cmnt_bd) {
            changeStatus(dtBulb, bdBulb);
        } else {
            changeStatus(bdBulb, dtBulb);
        }
    }

    private void changeStatus (View v1, View v2) {
        HomeActivity acct = (HomeActivity) getContext();
        boolean isServiceAvailable = acct.bdsService.isServiceAvailable();
        stopAni(v1);
        if (isServiceAvailable) {
            finishedAni(v2);
        } else {
            stopAni(v2);
        }
    }

    private void finishedAni(View v) {
        try {
            v.setBackground(getResources().getDrawable(R.drawable.light_bulb_shape));
        } catch (Exception e) {
            Log.d(TAG, "Set bulb animation error.");
        }
    }

    private void connectAni (View v) {
        try {
            v.setBackground(getResources().getDrawable(R.drawable.bulb_animation));
            ((AnimationDrawable) v.getBackground()).start();
        } catch (Exception e) {
            Log.d(TAG, "Set bulb animation error.");
        }
    }

    private void stopAni(View v) {
        try {
            v.setBackground(getResources().getDrawable(R.drawable.dark_bulb_shape));
        } catch (Exception e) {
            Log.d(TAG, "Remove buld animation error.");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onMessge (EmittSocketEvent event) {
        switch (event.status) {
            case "0":
                finishedAni(dtBulb);
                Log.d(TAG, "Success!!!");
                break;
            case "1":
                connectAni(dtBulb);
                Log.d(TAG, "Connecting!!!");
                break;
            case "2":
                stopAni(dtBulb);
                Log.d(TAG, "Failed!!!");
                break;
        }
    }
}
