package com.example.beans;

public class Position {
    private String cardNum;
    private double distance;
    private double xdistance;
    private double ydistance;
    private double latitude;
    private double longitude;
    private int xRange;
    private int yRange;

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getxRange() {
        return xRange;
    }

    public int getyRange() {
        return yRange;
    }

    public void setxRange(int xRange) {
        this.xRange = xRange;
    }

    public void setyRange(int yRange) {
        this.yRange = yRange;
    }

    public double getXdistance() {
        return xdistance;
    }

    public double getYdistance() {
        return ydistance;
    }

    public void setXdistance(double xdistance) {
        this.xdistance = xdistance;
    }

    public void setYdistance(double ydistance) {
        this.ydistance = ydistance;
    }

    public static class StrobeState {
        private String strobelAlarm = "Y"; // 频闪开启
        private String twinkletimesValue = "7"; // 闪烁次数
        private String twinkleTimeLengthValue = "2"; //
        private String twinkleInterValValue = "60";

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
}
