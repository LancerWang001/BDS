package com.example.bds;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.location.LocationService;
import com.serialport.SerialPortUtil;
import com.socket.DTSocket;

public class BDSService extends Service {

    private static String TAG = "BDSService";

    int mStartMode;

    IBinder mBinder = new BDSBinder();

    boolean mAllowRebind;

    public static SerialPortUtil serialPortUtil;

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
        /* Handle BD / WIFI service here */
        if (HomeActivity.COMMUNICATE_WAY == R.string.card_cmnt_bd) {
            serialPortUtil = new SerialPortUtil();
            serialPortUtil.openSerialPort();
        } else {
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
        serialPortUtil.sendSerialPort(data);
        Log.d(TAG, data);
        // handle data here
    }

    public String receiveService () {
//        Log.d(TAG, "")
        return null;
    }

    public void startLocationService (Context context) {
        Log.d(TAG, "startLocationService");
        new LocationService(context);
    }

    public void sendByDT (String data) {
        dtSocket.writeData(data);
    }

}
