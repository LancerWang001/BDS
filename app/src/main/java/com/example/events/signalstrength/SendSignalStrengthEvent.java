package com.example.events.signalstrength;

import com.example.events.SendSignalEvent;
import static com.Constants.SIGNAL_READ_STRENGTH;

public class SendSignalStrengthEvent implements SendSignalEvent {
    public String signal = SIGNAL_READ_STRENGTH;
}
