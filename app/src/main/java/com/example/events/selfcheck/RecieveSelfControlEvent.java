package com.example.events.selfcheck;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.Constants.SIGNAL_SELF_CHECK_RGEX;
import static com.example.tools.SignalTools.haxToInt;

public class RecieveSelfControlEvent {

    public String batteryVoltage;

    public String targetCardId;

    public RecieveSelfControlEvent(String data) {
        Pattern pattern = Pattern.compile(SIGNAL_SELF_CHECK_RGEX);
        Matcher matcher = pattern.matcher(data);
        boolean isFind = matcher.find();
        String cardId = matcher.group(1);
        String voltage = matcher.group(2);
        if (isFind && !"".equals(cardId) && !"".equals(voltage)) {
            Log.d("voltage", voltage);
            batteryVoltage = haxToInt(voltage);
            targetCardId = haxToInt(cardId);
        }
    }
}
