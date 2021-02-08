package com.example.bds.layouts;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.Constants;
import com.example.bds.HomeActivity;
import com.example.bds.R;
import com.example.beans.CmntIntervalBean;
import com.example.events.BDSendPermitEvent;
import com.example.events.ChangeCmntWayEvent;
import com.example.events.uppercontrol.SendUpperControlEvent;
import com.example.service.BDSService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CmntWay extends LinearLayout {

    private AlertDialog dialog;

    private RadioGroup cmntGroup;

    private Button sendCmntway;

    private int checkedId;

    private SharedPreferences preferences = ((HomeActivity) getContext()).bdsService.preferences;

    public CmntWay(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_cmnt_way, this);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        EventBus.getDefault().register(this);
        this.dialog = createDialog(child);
        this.cmntGroup = child.findViewById(R.id.cmnt_way_rg);
        sendCmntway = child.findViewById(R.id.send_cmntway);
        sendCmntway.setOnClickListener((OnClickListener) view -> {
            CmntWay.this.checkedId = cmntGroup.getCheckedRadioButtonId();
            CmntWay.this.dialog.show();
        });
        initCmntWay();
    }

    private AlertDialog createDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.card_dialog_alert);
        builder.setPositiveButton(R.string.card_dialog_alert_Y, (dialogInterface, i) -> {
            RadioButton radioGroupButton = view.findViewById(cmntGroup.getCheckedRadioButtonId());
            String cmtWay = radioGroupButton.getText().toString();
            Log.d("way", cmtWay);
            if (cmtWay.equals("电台通信")) {
                cmtWay = "DT";
            } else if (cmtWay.equals("北斗通信")) {
                cmtWay = "BD";
            }
            changeCmntWay(this.checkedId);
        });
        builder.setNegativeButton(R.string.card_dialog_alert_N, (dialogInterface, i) -> {

        });
        return builder.create();
    }

    private void changeCmntWay(int checkedId) {
        int cmntWay = R.string.card_cmnt_dt;
        String symbolBD = "";
        String sumbolDT = "";
        switch (checkedId) {
            case R.id.card_cmnt_bd:
                cmntWay = R.string.card_cmnt_bd;
                symbolBD = "Y";
                sumbolDT = "N";
                break;
            case R.id.card_cmnt_dt:
                cmntWay = R.string.card_cmnt_dt;
                symbolBD = "N";
                sumbolDT = "Y";
                break;
        }
        CmntIntervalBean cmntInterval = ((HomeActivity) getContext()).intervals;
        HomeActivity home = (HomeActivity) getContext();
        BDSService service = home.bdsService;
        int COMMUNICATE_WAY = service.COMMUNICATE_WAY;
        if (checkedId == COMMUNICATE_WAY) return;
        try {
            EventBus.getDefault().post(
                    new SendUpperControlEvent(symbolBD, cmntInterval.getBdInterval(), sumbolDT, cmntInterval.getDtInterval())
            );
        } catch (Exception e) {
//            Log.
        }
        EventBus.getDefault().post(new ChangeCmntWayEvent(cmntWay));
    }

    private void initCmntWay() {
        try {
            String ComntWay = preferences.getString(Constants.ComntWay, "");
            if (ComntWay.equals("BD")) {
                cmntGroup.check(R.id.card_cmnt_bd);
                changeCmntWay(R.id.card_cmnt_bd);
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "预设通信方式初始化失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
        Log.d("CmntWay", "removed!!!");
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onBDSendPermitEvent(BDSendPermitEvent event) {
        if (!event.isPermission()) {
            sendCmntway.setEnabled(false);
        } else {
            sendCmntway.setEnabled(true);
        }
    }
}
