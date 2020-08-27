package com.example.events.selfcheck;

import com.example.events.SendSignalEvent;

import static com.Constants.SIGNAL_SELF_CHECK;

public class SendSelfControlEvent implements SendSignalEvent {
    public String signal;
    public SendSelfControlEvent(){
        signal = SIGNAL_SELF_CHECK;
    }
}
