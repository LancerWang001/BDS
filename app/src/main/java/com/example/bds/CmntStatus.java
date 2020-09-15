package com.example.bds;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class CmntStatus extends RelativeLayout {

    private String TAG = "CmntStatus";

    View bdBulb;
    View dtBulb;

    public CmntStatus(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_cmnt_status, this);
        bdBulb = findViewById(R.id.bd_bulb);
        dtBulb = findViewById(R.id.dt_bulb);

        HomeActivity acct = (HomeActivity) getContext();
        if (acct.bdsService.COMMUNICATE_WAY == R.string.card_cmnt_bd) {
            stopAni(dtBulb);
            startAni(bdBulb);
        } else {
            stopAni(bdBulb);
            startAni(dtBulb);
        }
    }

    private void startAni(View v) {
        try {
            v.setBackground(getResources().getDrawable(R.drawable.bulb_animation));
            ((AnimationDrawable) v.getBackground()).start();
        } catch (Exception e) {
            Log.d(TAG, "Set bulb animation error.");
        }
    }

    private void stopAni(View v) {
        try {
            ((AnimationDrawable) v.getBackground()).stop();
            v.setBackground(getResources().getDrawable(R.drawable.dark_bulb_shape));
        } catch (Exception e) {
            Log.d(TAG, "Remove buld animation error.");
        }
    }
}
