package com.example.events;

public class EmittSocketEvent {
    public String status; // 0: success 1: connecting 2: failed
    public EmittSocketEvent (String status) {
        this.status = status;
    }
}
