package com.example.events.signalstrength;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.Constants.SIGNAL_READ_STRENGTH_RGEX;

public class ReceiveSignalStrengthEvent {
    private ArrayList<String> signals;
    public ReceiveSignalStrengthEvent (String signal) {
        Pattern pattern = Pattern.compile(SIGNAL_READ_STRENGTH_RGEX);
        Matcher matcher = pattern.matcher(signal);
        if (matcher.find() && !Objects.equals(matcher.group(3), "")) {
            String signalsStr = matcher.group(3);
            String[] signalsArr = signalsStr.split(",");
            signals = (ArrayList<String>) Arrays.asList(signalsArr);
        }
    }

    public ArrayList<String> getSignals() {
        return signals;
    }
}
