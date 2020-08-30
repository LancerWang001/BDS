package com.example.events.selfcheck;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.Constants.SIGNAL_SELF_CHECK_RGEX;
import static com.example.tools.SignalTools.haxToInt;

public class RecieveSelfControlEvent {

    public String batteryVoltage;

    public RecieveSelfControlEvent(String data) {
        Pattern pattern = Pattern.compile(SIGNAL_SELF_CHECK_RGEX);
        Matcher matcher = pattern.matcher(data);

        if (matcher.find() && !"".equals(matcher.group(1))) {
            batteryVoltage = haxToInt(matcher.group(1));
        }
    }
}
