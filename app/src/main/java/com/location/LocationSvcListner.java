package com.location;

import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.tools.LocationEvent;

import org.greenrobot.eventbus.EventBus;

public class LocationSvcListner implements LocationListener {
    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d("Location", "Latitud: " + location.getLatitude() + "Longitude: " + location.getLongitude());
        EventBus.getDefault().post(new LocationEvent(new double[]{location.getLatitude(), location.getLongitude()}));
    }
}
