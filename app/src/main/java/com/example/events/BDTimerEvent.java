package com.example.events;

public class BDTimerEvent {
    private int remainSec;
    public BDTimerEvent (int remainSec) {
        this.remainSec = remainSec;
    }

    public int getRemainSec() {
        return remainSec;
    }
}
