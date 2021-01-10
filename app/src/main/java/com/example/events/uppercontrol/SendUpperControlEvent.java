package com.example.events.uppercontrol;

import com.example.events.SendSignalEvent;

import static com.Constants.SIGNAL_PERMIT;
import static com.Constants.SIGNAL_REJECT;
import static com.Constants.SIGNAL_UPPER_CONTROL;
import static com.example.tools.SignalTools.calcCustomerVerifyRes;
import static com.example.tools.SignalTools.intToHax;

public class SendUpperControlEvent implements SendSignalEvent {

    public String signal;

    private String bdInterval = "2";

    private String dtInterval = "10";

    public SendUpperControlEvent (String bdSymbol, String bdInterval, String dtSymbol, String dtInterval) {
        this.bdInterval = bdInterval;
        this.dtInterval = dtInterval;

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

        String dtIntervalCalc = intToHax(dtInterval);
        String bdIntervalCalc = intToHax(bdInterval);

        signal = String.format(SIGNAL_UPPER_CONTROL, bdSymbol, bdIntervalCalc, dtSymbol, dtIntervalCalc);
        signal = calcCustomerVerifyRes(signal);
    }
}
