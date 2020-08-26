package com.example.events;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.Constants.SIGNAL_SHORT_MSG_R;

public class RecieveShotMSGEvent {
    public String shortMsg;

    public RecieveShotMSGEvent(String data) {
        Pattern pattern = Pattern.compile(SIGNAL_SHORT_MSG_R);
        Matcher matcher = pattern.matcher(data);
        if (matcher.find() && !"".equals(matcher.group(5))) {
            shortMsg = matcher.group(5);
        }
    }
}
