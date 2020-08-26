package com.example.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.bds.HomeActivity;
import com.example.bds.R;
import com.location.LocationService;
import com.serialport.SerialPortUtil;
import com.socket.DTSocket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BDSService extends Service {

    private static String TAG = "BDSService";

    int mStartMode;

    IBinder mBinder = new BDSBinder();

    boolean mAllowRebind;

    public static SerialPortUtil serialPortUtil;

    ExecutorService executorService;

    DTSocket dtSocket;

    public BDSService() {
    }

    public class BDSBinder extends Binder {
        public BDSService getService () {
            return BDSService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service is invoke Created!");
        executorService = Executors.newCachedThreadPool();
        serialPortUtil = new SerialPortUtil();
        serialPortUtil.openSerialPort();

        /* Handle BD / WIFI service here */
        if (HomeActivity.COMMUNICATE_WAY == R.string.card_cmnt_dt) {
            dtSocket = new DTSocket();
            dtSocket.connect();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return mStartMode;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "Service is invoke onUnbind");
        return mAllowRebind;
    }

    @Override
    public void onRebind(Intent intent) {
    }

    @Override
    public void onDestroy() {
        serialPortUtil.closeSerialPort();
        dtSocket.close();
        Log.i(TAG, "Service is invoke Destroyed");
    }

    @NonNull
    public void sendService (final String data) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, data);
                if (HomeActivity.COMMUNICATE_WAY == R.string.card_cmnt_bd) {
                    serialPortUtil.sendSerialPort(data);
                } else {
                    dtSocket.writeData(data);
                }
            }
        });
    }

    public String receiveService () {
//        Log.d(TAG, "")
        return null;
    }

    public void startLocationService (Context context) {
        Log.d(TAG, "startLocationService");
        new LocationService(context);
    }
}
