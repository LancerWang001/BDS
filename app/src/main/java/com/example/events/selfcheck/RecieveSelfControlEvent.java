package com.example.events.selfcheck;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.Constants.SIGNAL_SELF_CHECK_RGEX;
import static com.example.tools.SignalTools.haxToInt;

public class RecieveSelfControlEvent {

    public String batteryVoltage;

    public RecieveSelfControlEvent(String data) {
        Pattern pattern = Pattern.compile(SIGNAL_SELF_CHECK_RGEX);
        Matcher matcher = pattern.matcher(data);
        boolean isFind = matcher.find();
        String voltage = matcher.group(1);
        if (isFind && !"".equals(voltage)) {
            Log.d("voltage", voltage);
            batteryVoltage = haxToInt(voltage);
        }
    }
}
