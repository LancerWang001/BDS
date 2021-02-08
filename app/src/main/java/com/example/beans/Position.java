package com.example.beans;

public class Position {
    private String cardNum;
    private double distance;
    private double xdistance;
    private double ydistance;
    private double latitude;
    private double longitude;
    private String latHem;
    private String longHem;
    private int xRange;
    private int yRange;

    public String getLatHem() {
        return latHem;
    }

    public void setLatHem(String latHem) {
        this.latHem = latHem;
    }

    public String getLongHem() {
        return longHem;
    }

    public void setLongHem(String longHem) {
        this.longHem = longHem;
    }

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

}
