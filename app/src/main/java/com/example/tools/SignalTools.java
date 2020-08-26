package com.example.tools;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignalTools {

    private static String TAG = "SignalTools";

    private static char[] HAX_ARR = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static Pattern PATTERN = Pattern.compile("\\$(.*?)\\*");

    public static String intToHax (String numStr) {
        int num = Integer.parseInt(numStr, 10);
        StringBuffer stf = new StringBuffer();
        String res;
        while (num != 0) {
            stf = stf.append(HAX_ARR[num % 16]);
            num = num / 16;
        }
        res = stf.reverse().toString();
        if (res.length() < 2) {
            res = "0" + res;
        }
        return res;
    }

    // 北斗信令 asc
    public static String calcBDVerifyRes (String signal) {
        Matcher matcher = PATTERN.matcher(signal);

        String calcStr = "";
        String verifiStr = "";

        if (matcher.find(0)) {
            Log.d(TAG, matcher.group(1));
            calcStr = matcher.group(1);
        } else {
            throw new RuntimeException("Not match signal pattern!");
        }
        char[] chars = calcStr.toCharArray();

        int verifinum = (int) chars[0];

        for (int i = 1; i < chars.length; i ++) {
            verifinum ^= (int) chars[i];
        }

        verifiStr = Integer.toHexString(verifinum).toUpperCase();
        if (verifiStr.length() < 2) {
            verifiStr = "0" + verifiStr;
        }

        Log.d(TAG, verifiStr);

        return signal + verifiStr;
    }

    //自定义指令 hax
    public static String calcCustomerVerifyRes (String haxSignal) {
        String[] haxArr = haxSignal.split("H+");
        int verifiNum = Integer.parseInt(haxArr[0], 16);
        for (int i = 1; i < haxArr.length; i ++) {
            if (haxArr[i] != "") {
                verifiNum ^= Integer.parseInt(haxArr[i], 16);
            }
        }
        String verifiStr = Integer.toHexString(verifiNum);
        verifiStr = verifiStr.toUpperCase() + "H";
        if (verifiStr.length() < 3) {
            verifiStr = "0" + verifiStr;
        }
        return haxSignal + verifiStr;
    }
}
