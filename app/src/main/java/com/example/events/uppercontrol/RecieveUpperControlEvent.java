package com.example.events.uppercontrol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.Constants.*;

public class RecieveUpperControlEvent {
    public RecieveUpperControlEvent (String data) {
        Pattern pattern = Pattern.compile(SIGNAL_UPPER_DATA);
        Matcher matcher = pattern.matcher(data);
        if (matcher.find() && "".equals(matcher.group(5))) {

        }
    }
}
