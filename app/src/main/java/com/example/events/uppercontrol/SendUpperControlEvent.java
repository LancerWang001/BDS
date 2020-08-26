package com.example.events.uppercontrol;

import com.example.events.SendSignalEvent;

import static com.Constants.SIGNAL_PERMIT;
import static com.Constants.SIGNAL_REJECT;
import static com.Constants.SIGNAL_UPPER_CONTROL;
import static com.example.tools.SignalTools.calcCustomerVerifyRes;
import static com.example.tools.SignalTools.intToHax;

public class SendUpperControlEvent implements SendSignalEvent {

    public String signal;

    public SendUpperControlEvent (String bdSymbol, String bdBreak, String dtSymbol, String dtBreak) {
        if ("Y".equals(bdSymbol)) {
            bdSymbol = SIGNAL_PERMIT;
        } else if ("N".equals(bdSymbol)){
            bdSymbol = SIGNAL_REJECT;
        }

        if ("Y".equals(dtSymbol)) {
            dtSymbol = SIGNAL_PERMIT;
        } else if ("N".equals(dtSymbol)){
            dtSymbol = SIGNAL_REJECT;
        }

        bdBreak = intToHax(bdBreak);
        dtBreak = intToHax(dtBreak);

        signal = String.format(SIGNAL_UPPER_CONTROL, bdSymbol, bdBreak, dtSymbol, dtBreak);
        signal = calcCustomerVerifyRes(signal);
    }
}
