package com.example.bds.homefragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.bds.HomeActivity;
import com.example.bds.R;
import com.example.beans.Status;
import com.example.beans.TimerClock;
import com.example.events.selfcheck.RecieveSelfControlEvent;
import com.example.events.selfcheck.SendSelfControlEvent;
import com.example.events.timer.TargetEvent;
import com.example.events.uppercontrol.RecieveUpperControlEvent;
import com.example.service.BDSService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class SelfCheckFragment extends Fragment {
    private TimerClock timerClock;
    private Button selfCheckButton;

    public SelfCheckFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        // 1 获得通讯方式，控制指示灯的显示
        // TODO: Rename and change types of parameters
        BDSService bdsService = ((HomeActivity) Objects.requireNonNull(getActivity())).bdsService;
        int way = bdsService.COMMUNICATE_WAY;
        if (way == R.string.card_cmnt_dt) {
            Log.d("==========", "初始化电台指示灯");
        } else {
            Log.d("==========", "初始化北斗指示灯");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        selfCheckButton = Objects.requireNonNull(getActivity()).findViewById(R.id.syscheck);
        selfCheckButton.setOnClickListener(view -> {
            // 系统自检按钮的发送
            Log.d("=======", "Voltage snd");
            EventBus.getDefault().post(new SendSelfControlEvent());
        });
        flushCheckStatus();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_self_check, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void flushCheckStatus() {
        TableLayout tablelayout = getActivity().findViewById(R.id.selfcheck_table);
        HashMap<String, Status> targetDevices = ((HomeActivity) getActivity()).getTargetDevices();
        Set<String> keySet = targetDevices.keySet();
        tablelayout.removeAllViews();
        for (String deviceId : keySet) {
            Status deviceStatus = targetDevices.get(deviceId);
            TableRow tableRow = (TableRow) View.inflate(getContext(), R.layout.layout_self_check_status, null);
            TextView textView = (TextView) tableRow.getChildAt(0);
            String power = deviceStatus.getPower();
            textView.setText("设备号：" + deviceId + " ----- 电量" + power + " %");
            tablelayout.addView(tableRow);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onRecieveSelfControlEvent(RecieveSelfControlEvent event) {
        Log.d("SelfControl=======", event.toString());
        String batV = event.batteryVoltage;
        Log.d("Voltage receive=======", batV);

        Status status = new Status();
        status.setDeviceId(event.targetCardId);
        status.setPower(event.batteryVoltage);
        HashMap<String, Status> targetDevices = ((HomeActivity) Objects.requireNonNull(getActivity())).getTargetDevices();
        targetDevices.put(status.getDeviceId(), status);

        flushCheckStatus();
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onRecieveUpperControlEvent(RecieveUpperControlEvent event) {
        selfCheckButton.setEnabled(true);
        if (timerClock != null) {
            timerClock.stop();
        }
        timerClock = new TimerClock(event.cardNum);
        timerClock.start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onTargetEvent(TargetEvent event) {
        if (event.time.equals("0")) {
            selfCheckButton.setEnabled(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}