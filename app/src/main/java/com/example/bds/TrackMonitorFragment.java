package com.example.bds;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beans.Position;
import com.example.events.UpdateTrackMessage;
import com.example.events.strobecontrol.SendStrobeControlEvent;
import com.example.events.systemsleep.SendSystemSleep;
import com.example.events.uppercontrol.SendUpperControlEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Timer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrackMonitorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackMonitorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public ArrayList<String> deviceCardNumArray = new ArrayList<String>();
    TextView time1;
    Timer t = new Timer();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RadioGroup deviceGroup;
    private String checkedCardNum;
    private Location locationInstance;
    private TextView disA;
    private TextView disB;
    private TextView dirA;
    private TextView dirB;


    public TrackMonitorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrackMonitorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrackMonitorFragment newInstance(String param1, String param2) {
        TrackMonitorFragment fragment = new TrackMonitorFragment();
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
        this.sendUpperControl();
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
        this.deviceGroup = getActivity().findViewById(R.id.sys_rest);
        HomeActivity home = (HomeActivity) getActivity();
        home.bdsService.startLocationService(getContext());

        TableLayout tableLayout = (TableLayout) getActivity().findViewById(R.id.track_param);
        TextView text = (TextView) View.inflate(getContext(), R.layout.layout_track_title, null);
        text.setText("暂无数据");
        text.setGravity(Gravity.CENTER);
        tableLayout.addView(text);

        getActivity().findViewById(R.id.send_sysRest).setOnClickListener(view -> {
            if (null != checkedCardNum) {
                EventBus.getDefault().post(new SendSystemSleep(TrackMonitorFragment.this.checkedCardNum));
            }
        });
//        getActivity().findViewById(R.id.strobe_alarm).setOnClickListener(view -> {
//            EventBus.getDefault().post(new SendStrobeControlEvent("Y", "1", "1", "1"));
//        });

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.saved_high_score_key), 1232131231);
        editor.commit();

        LinearLayout location = getActivity().findViewById(R.id.canvas_location);
        locationInstance = new Location(location.getContext());
        location.addView(locationInstance);
    }

    private void sendUpperControl() {
        //取变量
        HomeActivity activity = (HomeActivity) getActivity();
        int way = activity.bdsService.COMMUNICATE_WAY;
        String dt;
        String bd;
        if (R.string.card_cmnt_dt == way) {
            dt = "Y";
        } else {
            dt = "N";
        }
        if (R.id.card_cmnt_bd == way) {
            bd = "Y";
        } else {
            bd = "N";
        }
        Log.d("dt=====", dt + " bd=======" + bd);
        Log.d("way=====", String.valueOf(way));
        EventBus.getDefault().post(new SendUpperControlEvent(bd, dt));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != locationInstance.timer) {
            locationInstance.timer.cancel();
        }
        t.cancel();
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onMessage(UpdateTrackMessage mess) {

        if (Integer.parseInt(mess.targetPower) < 20) {
            CharSequence charSequence = mess.cardNum + "目标电量不足，请及时充电。";
            Toast.makeText(getContext(), charSequence, Toast.LENGTH_LONG).show();
        }

        TableRow tableRowTitle = (TableRow) getActivity().findViewById(R.id.track_title);
        tableRowTitle.removeAllViews();
        TableLayout tableLayout = (TableLayout) getActivity().findViewById(R.id.track_param);
        tableLayout.removeAllViews();

        Log.d("onMeesage cardNum : ", mess.cardNum);
        if (!deviceCardNumArray.contains(mess.cardNum)) {
            deviceCardNumArray.add(mess.cardNum);
        }
        for (String cardNum : deviceCardNumArray) {
            RadioButton rbtn = (RadioButton) View.inflate(getContext(), R.layout.layout_radio_button, null);
            rbtn.setText("目标" + cardNum);
            rbtn.setOnClickListener((view -> {
                this.checkedCardNum = cardNum;
            }));
            this.deviceGroup.addView(rbtn);
            TextView titleView = (TextView) View.inflate(getContext(), R.layout.layout_track_title, null);
            titleView.setText(cardNum + " : " + mess.targetPower + "%");
            if (Integer.parseInt(mess.targetPower) < 20) {
                titleView.setTextColor(Color.RED);
            }
            tableRowTitle.addView(titleView);

            Position pos = locationInstance.targetMap.get(cardNum);
            if (null != pos) {
                String distance = String.valueOf(pos.getDistance());

                LinearLayout paramLayout = (LinearLayout) View.inflate(getContext(), R.layout.layout_track_param, null);
                TextView textViewDis = (TextView) paramLayout.getChildAt(0);
                textViewDis.setText(cardNum + "距离:" + distance + "m");
                TextView textViewDir = (TextView) paramLayout.getChildAt(1);
                textViewDir.setText(
                        cardNum + "坐标:" + Math.round(pos.getLatitude() * 1000) / 1000 +
                                "," + Math.round(pos.getLongitude() * 1000 / 1000)
                );
                tableLayout.addView(paramLayout);
            }
        }
        TextView titleView = (TextView) View.inflate(getContext(), R.layout.layout_track_title, null);
        titleView.setText("时间:" + mess.timestamp);
        titleView.setGravity(Gravity.RIGHT);
        tableRowTitle.addView(titleView);
    }
}