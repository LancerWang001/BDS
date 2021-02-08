package com.socket;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.utils.BDHandler;

public class NetworkReceiver extends BroadcastReceiver {

    BDHandler handler;

    public NetworkReceiver(BDHandler handler) {
        this.handler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        this.handler.handle();
    }
}
