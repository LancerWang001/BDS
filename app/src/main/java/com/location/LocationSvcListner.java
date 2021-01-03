package com.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.events.LocationEvent;

import org.greenrobot.eventbus.EventBus;

public class LocationSvcListner implements LocationListener {
    String TAG = "Location";
    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d(TAG, "Latitud: " + location.getLatitude() + "Longitude: " + location.getLongitude());
        EventBus.getDefault().post(new LocationEvent(new double[]{location.getLatitude(), location.getLongitude()}));
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, provider);
    }
    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, provider);
    }
    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, provider);
//        updateShow(null);
    }
}
