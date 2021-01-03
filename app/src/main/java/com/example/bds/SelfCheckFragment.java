package com.example.bds;

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

import com.example.beans.Status;
import com.example.events.selfcheck.RecieveSelfControlEvent;
import com.example.events.selfcheck.SendSelfControlEvent;
import com.example.service.BDSService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelfCheckFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelfCheckFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private BDSService bdsService;
    private TextView batteryVoltageText;

    public SelfCheckFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelfCheckFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelfCheckFragment newInstance(String param1, String param2) {
        SelfCheckFragment fragment = new SelfCheckFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // 1 获得通讯方式，控制指示灯的显示
        bdsService = ((HomeActivity) getActivity()).bdsService;
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
        Button selfCheckButton = getActivity().findViewById(R.id.syscheck);
        selfCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 系统自检按钮的发送
                Log.d("=======", "Voltage snd");
                EventBus.getDefault().post(new SendSelfControlEvent());
            }
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

    private void flushCheckStatus () {
        TableLayout tablelayout = getActivity().findViewById(R.id.selfcheck_table);
        HashMap<String, Status> targetDevices = ((HomeActivity) getActivity()).getTargetDevices();
        Set<String> keySet = targetDevices.keySet();
        tablelayout.removeAllViews();
        for (String deviceId : keySet) {
            Status deviceStatus = targetDevices.get(deviceId);
            TableRow tableRow = (TableRow) View.inflate(getContext(), R.layout.layout_self_check_status, null);
            TextView textView = (TextView) tableRow.getChildAt(0);
            String power = deviceStatus.getPower();
            textView.setText("设备号：" + deviceId + " ----- 电量" + power);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}