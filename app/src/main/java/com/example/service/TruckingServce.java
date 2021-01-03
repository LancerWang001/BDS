package com.example.service;

public class TruckingServce {
    private static double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180;
    }

    static public double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 1000);
        return s;
    }

    static public double getSigleDistance(double pos1, double pos2) {
        double posLat1 = rad(pos1);
        double posLat2 = rad(pos2);
        double dist = posLat1 - posLat2;
        dist = dist * EARTH_RADIUS;
        dist = Math.round(dist * 1000);
        return dist;
    }
}
