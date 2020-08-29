package com.example.service;

import android.util.Log;

import com.example.events.BDError;
import com.example.events.selfcheck.RecieveSelfControlEvent;
import com.example.events.uppercontrol.RecieveUpperControlEvent;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Constructor;
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
        Pattern pattern = Pattern.compile(SIGNAL_SHORT_MSG_R);
        Matcher matcher = pattern.matcher(data);

        if (matcher.find() && !"".equals(matcher.group(3)) && !"".equals(matcher.group(5))) {
            String status = matcher.group(3);
            String shortMessage = matcher.group(5);
            if (status == "2") {
                shortMessage = shortMessage.substring(2);
            }
            return shortMessage;
        } else {
            Pattern pattern1 = Pattern.compile(SIGNAL_BD_SEND_RES);
            Matcher matcher1 = pattern1.matcher(data);
            if (matcher1.find() && !"".equals(matcher1.group(3)) && !"".equals(matcher1.group(4))) {
                Log.d(TAG, "BD Send action Failed.");
                String frequency = matcher1.group(3);
                String suppressionId = matcher1.group(4);
                if (frequency == "N") {
                    return ",,00当前频度小于本用户设备的服务频度";
                } else {
                    switch (suppressionId) {
                        case "1": return ",,00接收到系统的抑制指令，发射被抑制";
                        case "2": return ",,00电量不足，发射被抑制";
                        case "3": return ",,00设置为无线电静默，发射被抑制";
                        default: return ",,00未知原因，发射被抑制";
                    }
                }
            } else {
                throw new RuntimeException("Not match BD TXR signals!");
            }
        }
    }

    static void handOutSignalEvent (String signal) {
        String command = signal.substring(2, 4);
        Class clazz = null;
        switch(command){
            case "41":
                clazz = RecieveSelfControlEvent.class;
                break;
            case "40":
                clazz = RecieveUpperControlEvent.class;
                break;
            case "00":
                clazz = BDError.class;
                break;
        }
        if (null != clazz) {
            try {
                Constructor con = clazz.getConstructor(String.class);
                con.setAccessible(true);
                EventBus.getDefault().post(con.newInstance(signal));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
