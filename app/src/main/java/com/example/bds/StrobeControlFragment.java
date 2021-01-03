package com.example.bds;

import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.beans.Status;
import com.example.beans.StrobeState;
import com.example.events.MessageEvent;
import com.example.events.strobecontrol.SendStrobeControlEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StrobeControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StrobeControlFragment extends Fragment {
    private static String strobelAlarm = "Y";
    public List<String> selectedCards = new ArrayList<String>();
    private EditText twinkletimes;
    private EditText twinkleTimeLength;
    private EditText twinkleInterVal;

    public StrobeControlFragment() {
        // Required empty public constructor
    }

    public static StrobeControlFragment newInstance(String param1, String param2) {
        StrobeControlFragment fragment = new StrobeControlFragment();
        new Bundle();
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
        return inflater.inflate(R.layout.fragment_strobe_control, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        HashMap<String, Status> targetDevices = ((HomeActivity) Objects.requireNonNull(getActivity())).getTargetDevices();
        //获得单选框的数据
        RadioGroup radioGroup = getActivity().findViewById(R.id.strobeAlarm);

        Log.d("频闪警报", strobelAlarm);
        Spinner cardIdSpinner = Objects.requireNonNull(getActivity()).findViewById(R.id.card_id_spinner);
        twinkletimes = (EditText) getActivity().findViewById(R.id.twinkletimes);
        twinkleTimeLength = (EditText) getActivity().findViewById(R.id.twinkletimelength);
        twinkleInterVal = (EditText) getActivity().findViewById(R.id.twinkleinterval);

        Button twinkleBtn = (Button) getActivity().findViewById(R.id.twinklecontrol);
        twinkleBtn.setOnClickListener((view -> {
            for (String cardId : StrobeControlFragment.this.selectedCards) {
                String strobelAlarm = (String) ((RadioButton) getActivity().findViewById(radioGroup.getCheckedRadioButtonId())).getText();
                Log.d("strobelAlarm", strobelAlarm);
                if (strobelAlarm.equals("开启")) strobelAlarm = "Y";
                else strobelAlarm = "N";
                String twinkletimesValue = String.valueOf(twinkletimes.getText());
                String twinkleTimeLengthValue = String.valueOf(twinkleTimeLength.getText());
                String twinkleInterValValue = String.valueOf(twinkleInterVal.getText());
                if (twinkletimesValue.equals("") || twinkleTimeLengthValue.equals("") || twinkleInterValValue.equals("")) return;
                SendStrobeControlEvent sendStrobeControlEvent = new SendStrobeControlEvent(
                        cardId, strobelAlarm, twinkletimesValue, twinkleTimeLengthValue, twinkleInterValValue);
                EventBus.getDefault().post(sendStrobeControlEvent);
                StrobeState strobeState = new StrobeState();
                strobeState.setStrobelAlarm(strobelAlarm);
                strobeState.setTwinkleTimeLengthValue(twinkleTimeLengthValue);
                strobeState.setTwinkleInterValValue(twinkleInterValValue);
                Status status = targetDevices.get(cardId);
                if (status != null) status.setStrobeState(strobeState);
                targetDevices.put(cardId, status);
            }
        }));

        // add deviceCard dropdown list
        Set<String> keyset = targetDevices.keySet();
        ArrayList<String> cardIdList = new ArrayList<String>(keyset);
        cardIdList.add(0, "所有设备");
        if (keyset.size() == 0) return;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Objects.requireNonNull(getActivity()), R.layout.layout_spinner, cardIdList);
        adapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
        cardIdSpinner.setAdapter(adapter);
        cardIdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = adapterView.getItemAtPosition(i).toString();
                Log.d("Selected item:", text);
                StrobeControlFragment.this.selectedCards = new ArrayList<String>();
                if (text.equals("所有设备")) {
                    StrobeControlFragment.this.selectedCards = new ArrayList<String>(keyset);
                    setStrobeConfigs();
                } else {
                    StrobeControlFragment.this.selectedCards.add(text);
                    HashMap<String, Status> targetDevices = ((HomeActivity) Objects.requireNonNull(getActivity())).getTargetDevices();
                    StrobeState strobeState = Objects.requireNonNull(targetDevices.get(text)).getStrobeState();
                    setStrobeConfigs(strobeState);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // do nothing
            }
        });
    }

    private void setStrobeConfigs(StrobeState strobeState) {
        twinkletimes.setText(strobeState.getTwinkletimesValue());
        twinkleTimeLength.setText(strobeState.getTwinkleTimeLengthValue());
        twinkleInterVal.setText(strobeState.getTwinkleInterValValue());
    }

    private void setStrobeConfigs() {
        twinkletimes.setText("");
        twinkleTimeLength.setText("");
        twinkleInterVal.setText("");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        // dispatch data
        Log.d("Event Bus: ", event.message);
    }
}