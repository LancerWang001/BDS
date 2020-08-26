package com.example.events.strobecontrol;

import static com.Constants.SIGNAL_STROBE_CONTROL;
import static com.Constants.SIGNAL_PERMIT;
import static com.Constants.SIGNAL_REJECT;
import static com.example.tools.SignalTools.calcCustomerVerifyRes;
import static com.example.tools.SignalTools.intToHax;

public class SendStrobeControlEvent {
    public String signal;

    public SendStrobeControlEvent(String symbol, String times, String timeLong, String timeBreak) {
        if ("Y".equals(symbol)) {
            symbol = SIGNAL_PERMIT;
        } else if ("N".equals(symbol)) {
            symbol = SIGNAL_REJECT;
        }
        times = intToHax(times);
        timeLong = intToHax(timeLong);
        timeBreak = intToHax(timeBreak);
        signal = String.format(SIGNAL_STROBE_CONTROL, symbol, times, timeLong, timeBreak);
        signal = calcCustomerVerifyRes(signal);
    }
}
