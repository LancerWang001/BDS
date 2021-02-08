package com.example.bds.layouts;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.bds.HomeActivity;
import com.example.bds.R;
import com.example.events.ChangeCmntWayEvent;
import com.example.events.ServiceEvent;
import com.example.events.signalstrength.ReceiveSignalStrengthEvent;
import com.example.events.signalstrength.SendSignalStrengthEvent;
import com.example.service.BDSService;
import com.socket.NetworkReceiver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class CmntStatus extends View {
    private String TAG = "CmntStatus";
    private Context mContext;
    private NetworkReceiver netWorkStateReceiver;
    private boolean isBDcmntway;

    public CmntStatus(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        View.inflate(context, R.layout.layout_cmnt_status, null);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void bindNetworkLayout() {
        if (netWorkStateReceiver == null) {
            netWorkStateReceiver = new NetworkReceiver(() -> {
                int rssi = CmntStatus.this.getWifiRssi();
                if (isBDcmntway) return;
                Log.d(TAG, String.valueOf(rssi));
                showAni(rssi);
            });
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mContext.registerReceiver(netWorkStateReceiver, filter);
    }

    private void removeNetworkLayout() {
        if (netWorkStateReceiver != null) {
            mContext.unregisterReceiver(netWorkStateReceiver);
            netWorkStateReceiver = null;
        }
    }

    private void showAni(int rssi) {
        int bgColor = R.drawable.dark_bulb_shape;
        if (rssi == 0) {
            bgColor = R.drawable.deepred_bulb_shape;
        } else if (rssi == 1) {
            bgColor = R.drawable.red_bulb_shape;
        } else if (rssi == 2) {
            bgColor = R.drawable.orange_bulb_shape;
        } else if (rssi == 3) {
            bgColor = R.drawable.yellow_bulb_shape;
        } else if (rssi == 4) {
            bgColor = R.drawable.light_bulb_shape;
        } else if (rssi == -1) {
            bgColor = R.drawable.dark_bulb_shape;
        }
        this.setBackground(getResources().getDrawable(bgColor));
    }

    public int getWifiRssi() {
        HomeActivity acct = (HomeActivity) getContext();
        BDSService service = acct.bdsService;
        if (!service.isServiceAvailable()) {
            return -1;
        }
        WifiManager mWifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        int wifi = mWifiInfo.getRssi();//获取wifi信号强度
        Log.d(TAG, Integer.toString(wifi));
        int rssi = 0;
        if (wifi > -50 && wifi < 0) {//最强
            rssi = 4;
        } else if (wifi > -70 && wifi < -50) {//较强
            rssi = 3;
        } else if (wifi > -80 && wifi < -70) {//较弱
            rssi = 2;
        } else if (wifi > -100 && wifi < -80) {//微弱
            rssi = 1;
        }
        return rssi;
    }

    public int getSeriRssi(ArrayList<String> rssiArr) {
        HomeActivity acct = (HomeActivity) getContext();
        BDSService service = acct.bdsService;
        if (!service.isServiceAvailable()) {
            return -1;
        }
        int rssi = 0;
        if (!rssiArr.contains("3") && !rssiArr.contains("4")) rssi = 1;
        else if (rssiArr.contains("3") && !rssiArr.contains("4")) {
            if (rssiArr.indexOf("3") == rssiArr.lastIndexOf("3")) {
                rssi = 2;
            } else {
                rssi = 3;
            }
        } else if (rssiArr.contains("4")) {
            rssi = 4;
        }
        return rssi;
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onServiceReady(ServiceEvent evt) {
        if (evt.getCmntway().equals("DT")) {
            bindNetworkLayout();
        } else if (evt.getCmntway().equals("BD")) {
            removeNetworkLayout();
            EventBus.getDefault().post(new SendSignalStrengthEvent());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onServiceDrop(ServiceEvent evt) {
        if (evt.isDrop()) {
            showAni(-1);
            HomeActivity acct = (HomeActivity) getContext();
            BDSService service = acct.bdsService;
            removeNetworkLayout();
            if (evt.getCmntway().equals("DT") && !isBDcmntway && !service.isServiceAvailable()) {
                Log.d(TAG, "re-connecting...");
                service.dtSocket.connect();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onChangeCmntwayMessage(ChangeCmntWayEvent event) {
        isBDcmntway = event.cmntWay == R.string.card_cmnt_bd;
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onReceiveSignalStrengthEvent(ReceiveSignalStrengthEvent event) {
        showAni(getSeriRssi(event.getSignals()));
    }
}
