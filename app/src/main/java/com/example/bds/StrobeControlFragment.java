package com.example.bds;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.events.MessageEvent;
import com.example.events.configparams.SendConfigParamsEvent;
import com.example.events.strobecontrol.SendStrobeControlEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StrobeControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StrobeControlFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String strobelAlarm = "";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StrobeControlFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StrobeControlFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StrobeControlFragment newInstance(String param1, String param2) {
        StrobeControlFragment fragment = new StrobeControlFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_strobe_control, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //获得单选框的数据
        RadioGroup radioGroup = getActivity().findViewById(R.id.strobeAlarm);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = getActivity().findViewById(radioGroup.getCheckedRadioButtonId());
                String alarmText = radioButton.getText().toString();
                Log.d("alarm", alarmText);
                if (alarmText.equals("开启")) {
                    strobelAlarm = "Y";
                } else {
                    strobelAlarm = "N";
                }
            }
        });

        //Button button = getActivity().findViewById(R.id.stromAlarm);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("频闪警报", strobelAlarm);
//                EventBus.getDefault().post(strobelAlarm);
//            }
//        });
        Log.d("频闪警报", strobelAlarm);
        EditText twinkletimes = (EditText) getActivity().findViewById(R.id.twinkletimes);
        String twinkletimesValue = String.valueOf(twinkletimes.getText());
        EditText twinkleTimeLength = (EditText) getActivity().findViewById(R.id.twinkletimelength);
        String twinkleTimeLengthValue = String.valueOf(twinkletimes.getText());
        EditText twinkleInterVal = (EditText) getActivity().findViewById(R.id.twinkleinterval);
        String twinkleInterValValue = String.valueOf(twinkletimes.getText());

        Button twinkleBtn = (Button) getActivity().findViewById(R.id.twinklecontrol);
        if (null != twinkleBtn) Log.d("twinkleBtn:", "initial success!");
        else Log.d("twinkleBtn:", "initial fail!");
        twinkleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("频闪控制参数发送", "strobelAlarm============" + strobelAlarm+"twinkletimesValue=====" + twinkletimesValue + "twinkleTimeLengthValue=====" + twinkleTimeLengthValue + "twinkleInterValValue=====" + twinkleInterValValue);
                EventBus.getDefault().post(new SendStrobeControlEvent(strobelAlarm,twinkletimesValue, twinkleTimeLengthValue, twinkleInterValValue));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        // dispatch data
        Log.d("Event Bus: ", event.message);
    }
}