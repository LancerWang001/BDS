package com.example.events.readcard;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.Constants.SIGNAL_READ_CARD_RGEX;

public class RecieveReadCardEvent {
    public String cardNum;

    public RecieveReadCardEvent(String data) {
        Pattern pattern = Pattern.compile(SIGNAL_READ_CARD_RGEX);
        Matcher matcher = pattern.matcher(data);
        if (matcher.find() && !"".equals(matcher.group(1))) {
            cardNum = matcher.group(1);
        }
    }
}
