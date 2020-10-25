package com.example.events.selfcheck;

import com.example.events.SendSignalEvent;
import static com.example.tools.SignalTools.calcCustomerVerifyRes;

import static com.Constants.SIGNAL_SELF_CHECK;

public class SendSelfControlEvent implements SendSignalEvent {
    public String signal;
    public SendSelfControlEvent(){
        signal = calcCustomerVerifyRes(SIGNAL_SELF_CHECK);
    }
}
