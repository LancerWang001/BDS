package com.example.service;

import com.example.events.selfcheck.RecieveSelfControlEvent;
import com.example.events.uppercontrol.RecieveUpperControlEvent;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Constructor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.Constants.SIGNAL_SHORT_MSG_R;
import static com.Constants.SIGNAL_SHORT_MSG_S;
import static com.Constants.TARGET_CARD_NUM;
import static com.example.tools.SignalTools.calcBDVerifyRes;

public class BDTXSignalSvc {
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
            throw new RuntimeException("Not match BD TXR signals!");
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
