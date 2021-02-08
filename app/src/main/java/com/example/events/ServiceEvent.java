package com.example.events;

public class ServiceEvent {
    private String cmntway;
    private boolean isDrop;
    public ServiceEvent (String cmntway) {
        this.cmntway = cmntway;
    }

    public ServiceEvent (String cmntway, boolean isDrop) {
        this.cmntway = cmntway;
        this.isDrop = isDrop;
    }

    public String getCmntway() {
        return cmntway;
    }

    public boolean isDrop() {
        return isDrop;
    }
}
