package com.example.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.bds.R;
import com.example.events.ChangeCmntWayEvent;
import com.example.events.MessageEvent;
import com.example.events.configparams.SendConfigParamsEvent;
import com.example.events.readcard.SendReadCardEvent;
import com.example.events.selfcheck.SendSelfControlEvent;
import com.example.events.strobecontrol.SendStrobeControlEvent;
import com.example.events.systemsleep.SendSystemSleep;
import com.example.events.uppercontrol.SendUpperControlEvent;
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
import static com.example.tools.SignalTools.calcBDVerifyRes;

public class BDSService extends Service {
    private static String TAG = "BDSService";
    int mStartMode;
    IBinder mBinder = new BDSBinder();
    boolean mAllowRebind;
    ExecutorService executorService;
    DTSocket dtSocket = new DTSocket();

    public static SerialPortUtil serialPortUtil = new SerialPortUtil();

    public SharedPreferences preferences;

    //电台
    public int COMMUNICATE_WAY = R.string.card_cmnt_dt;

    //北斗
//    public int COMMUNICATE_WAY = R.string.card_cmnt_bd;

    private String emissionStatus = "0";

    public BDSService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service is invoke Created!");
        executorService = Executors.newCachedThreadPool();
        EventBus.getDefault().register(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "Service is invoke onBind");
        /* Handle BD / WIFI service here */
        if (COMMUNICATE_WAY == R.string.card_cmnt_dt) {
            dtSocket.connect();
        } else if (COMMUNICATE_WAY == R.string.card_cmnt_bd) {
            serialPortUtil.openSerialPort();
        }
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
    public void onSendConfigParamsEvent(SendConfigParamsEvent signalEvent) {
        sendShortMessage(signalEvent.signal);
    }

    @Subscribe()
    public void onSendSelfControlEvent(SendSelfControlEvent signalEvent) {
        sendShortMessage(signalEvent.signal);
    }

    @Subscribe()
    public void onSendStrobeControlEvent(SendStrobeControlEvent signalEvent) {
        sendShortMessage(signalEvent.signal);
    }

    @Subscribe()
    public void onSendUpperControlEvent(SendUpperControlEvent signalEvent) {
        sendShortMessage(signalEvent.signal);
    }

    @Subscribe()
    public void onSendReadCardEvent(SendReadCardEvent event) {
        sendShortMessage(calcBDVerifyRes(event.signal));
    }

    @Subscribe()
    public void onSendSystemSleepEvent(SendSystemSleep event){
        sendShortMessage(event.signal);
    }

    // 订阅接收信令事件，进行分发
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEvent(MessageEvent messageEvent) {
        if (COMMUNICATE_WAY == R.string.card_cmnt_dt) {
            handOutSignalEvent(messageEvent.message);
        } else {
            String signal = getBDTXR(messageEvent.message);
            handOutSignalEvent(signal);
        }
    }

    private void sendShortMessage (String signal) {
        if (COMMUNICATE_WAY == R.string.card_cmnt_dt) {
            sendService(signal);
        } else {
            String bdtxaSignal = getBDTXA(signal);
            sendService(bdtxaSignal);
        }
    }

    @Subscribe()
    public void onChangeCmntWay(ChangeCmntWayEvent event) {
        if (event.cmntWay == R.string.card_cmnt_dt) {
            // 关闭串口，连接电台
            serialPortUtil.closeSerialPort();
            dtSocket.connect();
        } else {
            // 连接电台，关闭串口
            dtSocket.close();
            serialPortUtil.openSerialPort();
        }
        this.COMMUNICATE_WAY = event.cmntWay;
    }

    @NonNull
    private void sendService(final String data) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, data);
                if (COMMUNICATE_WAY == R.string.card_cmnt_bd) {
                    serialPortUtil.sendSerialPort(data);
                } else {
                    dtSocket.writeData(data);
                }
            }
        });
    }

    public void startLocationService(Context context) {
        Log.d(TAG, "startLocationService");
        new LocationService(context);
    }

    public class BDSBinder extends Binder {
        public BDSService getService() {
            return BDSService.this;
        }
    }
}
