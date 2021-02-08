package com.example.beans;

public class StrobeState {
    private String strobelAlarm = "Y"; // 频闪开启
    private String twinkletimesValue = "5"; // 闪烁次数
    private String twinkleTimeLengthValue = "2"; // 闪烁时常
    private String twinkleInterValValue = "60"; // 闪烁间隔

    public String getStrobelAlarm() {
        return strobelAlarm;
    }

    public void setStrobelAlarm(String strobelAlarm) {
        this.strobelAlarm = strobelAlarm;
    }

    public String getTwinkletimesValue() {
        return twinkletimesValue;
    }

    public void setTwinkletimesValue(String twinkletimesValue) {
        this.twinkletimesValue = twinkletimesValue;
    }

    public String getTwinkleTimeLengthValue() {
        return twinkleTimeLengthValue;
    }

    public void setTwinkleTimeLengthValue(String twinkleTimeLengthValue) {
        this.twinkleTimeLengthValue = twinkleTimeLengthValue;
    }

    public String getTwinkleInterValValue() {
        return twinkleInterValValue;
    }

    public void setTwinkleInterValValue(String twinkleInterValValue) {
        this.twinkleInterValValue = twinkleInterValValue;
    }
}
