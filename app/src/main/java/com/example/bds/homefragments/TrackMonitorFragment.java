package com.example.bds.homefragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bds.HomeActivity;
import com.example.bds.R;
import com.example.beans.Position;
import com.example.beans.TimerClock;
import com.example.events.BDSendPermitEvent;
import com.example.events.UpdateTrackMessage;
import com.example.events.systemsleep.SendSystemSleep;
import com.example.events.timer.TargetEvent;
import com.example.events.uppercontrol.RecieveUpperControlEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrackMonitorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackMonitorFragment extends Fragment {
    private RadioGroup deviceGroup;
    private String checkedCardNum;
    private Location locationInstance;
    private HashMap<String, TimerClock> timerHashMap = new HashMap<>();
    private HashMap<String, String> powerMap = new HashMap<>();
    private TableLayout timerLayout;
    private Button systemSleepBtn;

    public TrackMonitorFragment() {
        // Required empty public constructor
    }

    public static TrackMonitorFragment newInstance(String param1, String param2) {
        TrackMonitorFragment fragment = new TrackMonitorFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_track_monitor, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.deviceGroup = Objects.requireNonNull(getActivity()).findViewById(R.id.sys_rest);
        HomeActivity home = (HomeActivity) getActivity();
        home.bdsService.startLocationService(getContext());

        systemSleepBtn = getActivity().findViewById(R.id.send_sysRest);
        TableLayout tableLayout = (TableLayout) getActivity().findViewById(R.id.track_param);
        timerLayout = getActivity().findViewById(R.id.track_clock);
        TableRow textRow = (TableRow) View.inflate(getContext(), R.layout.layout_track_title, null);
        TextView text = (TextView) textRow.getChildAt(0);
        text.setText("暂无数据");
        text.setGravity(Gravity.CENTER);
        tableLayout.addView(textRow);

        systemSleepBtn.setOnClickListener(view -> {
            if (null != checkedCardNum) {
                EventBus.getDefault().post(new SendSystemSleep(TrackMonitorFragment.this.checkedCardNum));
            }
        });

        LinearLayout location = getActivity().findViewById(R.id.canvas_location);
        locationInstance = new Location(location.getContext());
        location.addView(locationInstance);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (null != locationInstance.timer) {
            locationInstance.timer.cancel();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onBDSendPermitEvent(BDSendPermitEvent event) {
        if (!event.isPermission()) {
            systemSleepBtn.setEnabled(false);
        } else {
            systemSleepBtn.setEnabled(true);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onMessage(UpdateTrackMessage mess) {

        if (Integer.parseInt(mess.targetPower) < 20) {
            CharSequence charSequence = mess.cardNum + "目标电量不足，请及时充电。";
            Toast.makeText(getContext(), charSequence, Toast.LENGTH_LONG).show();
        }

        TableRow tableRowTitle = Objects.requireNonNull(getActivity()).findViewById(R.id.track_topTitle);
        tableRowTitle.removeAllViews();
        powerMap.put(mess.cardNum, mess.targetPower);
        Set<String> powerKeySet = powerMap.keySet();
        for (String cardNum : powerKeySet) {
            String power = powerMap.get(cardNum);
            TableRow titleView = (TableRow) View.inflate(getContext(), R.layout.layout_track_title, null);
            TextView titleText = (TextView) titleView.getChildAt(0);
            titleText.setText(cardNum + " : " + power + "%");
            if (Integer.parseInt(mess.targetPower) < 20) {
                titleText.setTextColor(Color.RED);
            }
            tableRowTitle.addView(titleView);
        }

        TableLayout tableLayout = (TableLayout) getActivity().findViewById(R.id.track_param);
        tableLayout.removeAllViews();
        this.deviceGroup.removeAllViews();

        Log.d("onMeesage cardNum : ", mess.cardNum);
        Set<String> keySet = mess.targetMap.keySet();
        for (String cardNum : keySet) {
            RadioButton rbtn = (RadioButton) View.inflate(getContext(), R.layout.layout_radio_button, null);
            rbtn.setText("目标" + cardNum);
            rbtn.setOnClickListener((view -> {
                this.checkedCardNum = cardNum;
            }));
            this.deviceGroup.addView(rbtn);
            Position pos = locationInstance.targetMap.get(cardNum);
            if (null != pos) {
                String distance = String.valueOf(pos.getDistance());

                LinearLayout paramLayout = (LinearLayout) View.inflate(getContext(), R.layout.layout_track_param, null);
                TextView textViewDis = (TextView) paramLayout.getChildAt(0);
                if (Double.parseDouble(distance) > 1000) {
                    distance = Double.toString(Double.parseDouble(distance) / 1000);
                    textViewDis.setText(cardNum + "距离: " + distance + " km");
                } else {
                    textViewDis.setText(cardNum + "距离: " + distance + " m");
                }
                textViewDis.setBackgroundColor(Color.WHITE);
                TextView textViewDir = (TextView) paramLayout.getChildAt(1);
                String latdd = Integer.toString((int) Math.floor(pos.getLatitude()));
                String latmm = Integer.toString((int) ((pos.getLatitude() - Math.floor(pos.getLatitude())) * 60));
                String longdd = Integer.toString((int) Math.floor(pos.getLongitude()));
                String longmm = Integer.toString((int) ((pos.getLongitude() - Math.floor(pos.getLongitude())) * 60));
                String latHem = "北纬";
                String longHem = "东经";

                if (pos.getLatHem().equals("S")) {
                    latHem = "南纬";
                }
                if (pos.getLongHem().equals("W")) {
                    longHem = "西经";
                }

                StringBuilder posText = new StringBuilder();
                posText.append(latHem);
                posText.append(" ");
                posText.append(latdd);
                posText.append("°");
                posText.append(latmm);
                posText.append("', ");
                posText.append(longHem);
                posText.append(" ");
                posText.append(longdd);
                posText.append("°");
                posText.append(longmm);
                posText.append("'");

                textViewDir.setText(posText.toString());
                tableLayout.addView(paramLayout);
            }
        }
        TableRow titleTime = (TableRow) View.inflate(getContext(), R.layout.layout_track_title, null);
        TextView timeText = (TextView) titleTime.getChildAt(0);
        timeText.setText("时间:" + mess.timestamp);
        timeText.setGravity(Gravity.RIGHT);
        tableRowTitle.addView(titleTime);
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onRecieveUpperControlEvent(RecieveUpperControlEvent event) {
        if (timerHashMap.get(event.cardNum) != null) {
            TimerClock clock = timerHashMap.get(event.cardNum);
            assert clock != null;
            clock.stop();
        }
        TimerClock newClock = new TimerClock(event.cardNum);
        timerHashMap.put(event.cardNum, newClock);
        newClock.start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onTargetEvent(TargetEvent event) {
        if (timerLayout.getVisibility() != View.VISIBLE) {
            timerLayout.setVisibility(View.VISIBLE);
        }
        timerLayout.removeAllViews();
        Set<String> keySet = timerHashMap.keySet();
        for (String cardNum : keySet) {
            TimerClock timerClock = timerHashMap.get(cardNum);
            assert timerClock != null;
            int time = timerClock.time;
            LinearLayout paramLayout = (LinearLayout) View.inflate(getContext(), R.layout.layout_track_param, null);
            TextView nameView = (TextView) paramLayout.getChildAt(0);
            TextView valueView = (TextView) paramLayout.getChildAt(1);
            nameView.setText("目标" + cardNum + "发送剩余时间");
            valueView.setText(time + "s");
            valueView.setBackgroundColor(Color.WHITE);
            timerLayout.addView(paramLayout);
        }
    }
}