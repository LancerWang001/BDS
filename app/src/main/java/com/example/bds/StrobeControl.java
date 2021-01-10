package com.example.bds;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.beans.Status;
import com.example.beans.StrobeState;
import com.example.events.strobecontrol.SendStrobeControlEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class StrobeControl extends FrameLayout {
    public List<String> selectedCards = new ArrayList<String>();
    private NumberalEdit twinkletimes;
    private EditText twinkleTimeLength;
    private EditText twinkleInterVal;

    public StrobeControl(@NonNull Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.layout_strobe_control, this);
    }

    public StrobeControl(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_strobe_control, this);
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        HashMap<String, Status> targetDevices = ((HomeActivity) Objects.requireNonNull(getContext())).getTargetDevices();
        //获得单选框的数据
        RadioGroup radioGroup = this.findViewById(R.id.strobeAlarm);
        Spinner cardIdSpinner = this.findViewById(R.id.card_id_spinner);
        twinkletimes = (NumberalEdit) this.findViewById(R.id.twinkletimes);
        twinkleTimeLength = (EditText) this.findViewById(R.id.twinkletimelength);
        twinkleInterVal = (EditText) this.findViewById(R.id.twinkleinterval);

        setStrobeConfigs(new StrobeState());

        Button twinkleBtn = (Button) this.findViewById(R.id.twinklecontrol);
        twinkleBtn.setOnClickListener((view -> {
            for (String cardId : this.selectedCards) {
                String strobelAlarm = (String) ((RadioButton) this.findViewById(radioGroup.getCheckedRadioButtonId())).getText();
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Objects.requireNonNull(getContext()), R.layout.layout_spinner, cardIdList);
        adapter.setDropDownViewResource(R.layout.layout_spinner_dropdown);
        cardIdSpinner.setAdapter(adapter);
        cardIdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = adapterView.getItemAtPosition(i).toString();
                Log.d("Selected item:", text);
                StrobeControl.this.selectedCards = new ArrayList<String>();
                if (text.equals("所有设备")) {
                    StrobeControl.this.selectedCards = new ArrayList<String>(keyset);
                    setStrobeConfigs(new StrobeState());
                } else {
                    StrobeControl.this.selectedCards.add(text);
                    HashMap<String, Status> targetDevices = ((HomeActivity) Objects.requireNonNull(getContext())).getTargetDevices();
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
}
