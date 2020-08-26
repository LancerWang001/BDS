package com.example.events.configparams;

import static com.Constants.SIGNAL_CONFIG_PARAMS;
import static com.example.tools.SignalTools.calcCustomerVerifyRes;
import static com.example.tools.SignalTools.intToHax;

public class SendConfigParamsEvent {

    public String signal;

    public SendConfigParamsEvent(String acceleration, String waterTime, String attitudDeterminationTime) {
        acceleration = intToHax(acceleration);
        waterTime = intToHax(waterTime);
        attitudDeterminationTime = intToHax(attitudDeterminationTime);
        signal = String.format(SIGNAL_CONFIG_PARAMS, acceleration, waterTime, attitudDeterminationTime);
        signal = calcCustomerVerifyRes(signal);
    }
}
