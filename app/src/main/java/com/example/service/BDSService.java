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
import com.example.events.MessageEvent;
import com.example.events.SendSignalEvent;
import com.example.events.configparams.SendConfigParamsEvent;
import com.location.LocationService;
import com.serialport.SerialPortUtil;
import com.socket.DTSocket;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.service.BDTXSignalSvc.getBDTXA;
import static com.example.service.BDTXSignalSvc.getBDTXR;
import static com.example.service.BDTXSignalSvc.handOutSignalEvent;

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
        EventBus.getDefault().register(this);
        /* Handle BD / WIFI service here */
        if (HomeActivity.COMMUNICATE_WAY == R.string.card_cmnt_dt) {
            dtSocket = new DTSocket();
            dtSocket.connect();
        } else if (HomeActivity.COMMUNICATE_WAY == R.string.card_cmnt_bd) {
            serialPortUtil = new SerialPortUtil();
            serialPortUtil.openSerialPort();
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
        EventBus.getDefault().unregister(this);
        serialPortUtil.closeSerialPort();
        dtSocket.close();
        Log.i(TAG, "Service is invoke Destroyed");
    }

    // 订阅发送信令事件，进行发送
    @Subscribe()
    public void onSendSignalEvent (SendConfigParamsEvent signalEvent) {
        String bdtxaSignal = getBDTXA(signalEvent.signal);
        sendService(bdtxaSignal);
    }

    // 订阅接收信令事件，进行分发
    @Subscribe()
    public void onMessageEvent (MessageEvent messageEvent) {
        String signal = getBDTXR(messageEvent.message);
        handOutSignalEvent(signal);
    }

    @NonNull
    private void sendService (final String data) {
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

    public void startLocationService (Context context) {
        Log.d(TAG, "startLocationService");
        new LocationService(context);
    }
}
