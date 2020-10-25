package com.example.events.systemsleep;

import android.util.Log;

import com.Constants;

import static com.example.tools.SignalTools.calcCustomerVerifyRes;
import static com.example.tools.SignalTools.intToHax;

public class SendSystemSleep {

    public  String signal;

    public SendSystemSleep(String machineCard){
        machineCard = intToHax(machineCard);
        signal = String.format(Constants.SIGNAL_BD_SYS_SLEEP, machineCard);
        signal = calcCustomerVerifyRes(signal);
    }
}
