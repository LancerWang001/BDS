package com.example.beans;

public class Status {
    private String deviceId;
    private String power;
    private StrobeState strobeState = new StrobeState();

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public StrobeState getStrobeState() {
        return strobeState;
    }

    public void setStrobeState(StrobeState strobeState) {
        this.strobeState = strobeState;
    }
}
