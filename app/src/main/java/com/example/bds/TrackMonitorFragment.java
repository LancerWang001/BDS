package com.example.bds;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.events.strobecontrol.SendStrobeControlEvent;
import com.example.events.systemsleep.SendSystemSleep;
import com.example.events.uppercontrol.RecieveUpperControlEvent;
import com.example.events.uppercontrol.SendUpperControlEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RadioGroup deviceGroup;

    public ArrayList<String> deviceCardNumArray = new ArrayList<String>();

    private String checkedCardNum;

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
        this.deviceCardNumArray.add("1");
        this.deviceCardNumArray.add("2");
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

        for (String cardNum : deviceCardNumArray) {
            RadioButton rbtn = (RadioButton)View.inflate(getContext(), R.layout.layout_radio_button, null);
            rbtn.setText("目标" + cardNum);
            rbtn.setOnClickListener((view -> {
                this.checkedCardNum = cardNum;
            }));
            this.deviceGroup.addView(rbtn);
        }
        getActivity().findViewById(R.id.send_sysRest).setOnClickListener(view -> {
            if (null != checkedCardNum) {
                EventBus.getDefault().post(new SendSystemSleep(TrackMonitorFragment.this.checkedCardNum));
            }
        });
        getActivity().findViewById(R.id.strobe_alarm).setOnClickListener(view ->{
            EventBus.getDefault().post(new SendStrobeControlEvent("Y","1","1","1"));
        });
        LinearLayout location = getActivity().findViewById(R.id.canvas_location);
        location.addView(new Location(location.getContext(), this.deviceCardNumArray));
    }

    private void sendUpperControl () {
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
        EventBus.getDefault().post(new SendUpperControlEvent(bd, "60", dt, "2"));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(RecieveUpperControlEvent mess) {
        Log.d("onMeesage cardNum : ", mess.cardNum);
        Log.d("onMessage1: ", Double.toString(mess.latitude));
        Log.d("onMessage2: ", mess.latitudeHem);
        Log.d("onMessage3: ", Double.toString(mess.longitude));
        Log.d("onMessage4: ", mess.longitudeHem);
    }
}