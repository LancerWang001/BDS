package com.example.bds;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.events.ChangeCmntWayEvent;

import org.greenrobot.eventbus.EventBus;

public class CmntWay extends LinearLayout {

    private AlertDialog dialog;

    private int checkedId;

    public CmntWay(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_cmnt_way, this);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        this.dialog = createDialog(child);
        child.findViewById(R.id.send_cmntway).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RadioGroup rg = (RadioGroup) child.findViewById(R.id.cmnt_way_rg);
                CmntWay.this.checkedId = rg.getCheckedRadioButtonId();
                CmntWay.this.dialog.show();
            }
        });
    }

    private AlertDialog createDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.card_dialog_alert);
        builder.setPositiveButton(R.string.card_dialog_alert_Y, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int cmntWay = R.string.card_cmnt_dt;
                switch (CmntWay.this.checkedId) {
                    case R.id.card_cmnt_bd:
                        cmntWay = R.string.card_cmnt_bd;
                        break;
                    case R.id.card_cmnt_dt:
                        cmntWay = R.string.card_cmnt_dt;
                }
                RadioGroup radioGroup = view.findViewById(R.id.cmnt_way_rg);
                RadioButton radioGroupButton = view.findViewById(radioGroup.getCheckedRadioButtonId());
                String cmtWay = radioGroupButton.getText().toString();
                Log.d("way", cmtWay);
                if (cmtWay.equals("电台通信")) {
                    cmtWay = "DT";
                } else if (cmtWay.equals("北斗通信")) {
                    cmtWay = "BD";
                }
                EventBus.getDefault().post(new ChangeCmntWayEvent(cmntWay));
            }
        });
        builder.setNegativeButton(R.string.card_dialog_alert_N, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        return builder.create();
    }
}
