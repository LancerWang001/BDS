package com.example.service;

import android.util.Log;

import com.example.events.BDError;
import com.example.events.selfcheck.RecieveSelfControlEvent;
import com.example.events.uppercontrol.RecieveUpperControlEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.Constants.SIGNAL_SHORT_MSG_R;
import static com.Constants.SIGNAL_SHORT_MSG_S;
import static com.Constants.TARGET_CARD_NUM;
import static com.Constants.SIGNAL_BD_SEND_RES;
import static com.example.tools.SignalTools.calcBDVerifyRes;

public class BDTXSignalSvc {
    static String TAG = "BDTXSignalSvc";

    static String getBDTXA(String shortMessage) {
        String bdtxSignal = String.format(SIGNAL_SHORT_MSG_S, TARGET_CARD_NUM, shortMessage);
        return calcBDVerifyRes(bdtxSignal);
    }

    static String getBDTXR(String data) {
        Log.d(TAG, data);
        Pattern pattern = Pattern.compile(SIGNAL_SHORT_MSG_R);
        Matcher matcher = pattern.matcher(data);

        if (matcher.find() && !"".equals(matcher.group(3)) && !"".equals(matcher.group(5))) {
            Log.d(TAG, "Signal match");
            String status = matcher.group(3);
            String shortMessage = matcher.group(5);
            if (status.equals("2")) {
                shortMessage = shortMessage.substring(2);
            }
            Log.d(TAG, "shortMessage : " + shortMessage);
            return shortMessage;
        } else {
            Pattern pattern1 = Pattern.compile(SIGNAL_BD_SEND_RES);
            Matcher matcher1 = pattern1.matcher(data);
            if (matcher1.find() && !"".equals(matcher1.group(3)) && !"".equals(matcher1.group(4))) {
                Log.d(TAG, "BD Send action Failed.");
                String frequency = matcher1.group(3);
                String suppressionId = matcher1.group(4);
                if (frequency.equals("N")) {
                    return ",,00当前频度小于本用户设备的服务频度";
                } else {
                    switch (suppressionId) {
                        case "1":
                            return ",,00接收到系统的抑制指令，发射被抑制";
                        case "2":
                            return ",,00电量不足，发射被抑制";
                        case "3":
                            return ",,00设置为无线电静默，发射被抑制";
                        default:
                            return ",,00未知原因，发射被抑制";
                    }
                }
            } else {
                Log.d(TAG, "Not match BD TXR signals!");
                return data;
            }
        }
    }

    static void handOutSignalEvent(String signal) {
        if (signal.length() < 5) {
            Log.d(TAG, signal);
            return;
        }
        String command = signal.substring(2, 4);
        Log.d(TAG, "signal : " + signal);
        switch (command) {
            case "41":
                EventBus.getDefault().post(new RecieveSelfControlEvent(signal));
                break;
            case "40":
                EventBus.getDefault().post(new RecieveUpperControlEvent(signal));
                break;
            case "00":
                EventBus.getDefault().post(new BDError(signal));
                break;
            default:
                Log.d(TAG, "No cust signal match");
        }
    }
}
