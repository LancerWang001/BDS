package com.example.events.uppercontrol;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.Constants.SIGNAL_UPPER_DATA;
import static com.example.tools.SignalTools.haxToInt;

public class RecieveUpperControlEvent {

    public String cardNum;

    public double latitude;

    public double longitude;

    public String latitudeHem;

    public String longitudeHem;

    public String timestamp;

    public String targetPower;

    public RecieveUpperControlEvent(String data) {
        Pattern pattern = Pattern.compile(SIGNAL_UPPER_DATA);
        Matcher matcher = pattern.matcher(data);
        if (matcher.find()) {
            cardNum = matcher.group(1);
            cardNum = haxToInt(cardNum);

            String hour = matcher.group(2);
            String minute = matcher.group(3);
            String second = matcher.group(4);
            String power = matcher.group(5);

            String ladd = matcher.group(6);
            String lamm1 = matcher.group(7);
            String lamm2 = matcher.group(8);
            String lamm3 = matcher.group(9);
            String lamm4 = matcher.group(10);
            String lamm5 = matcher.group(11);
            String lahem = matcher.group(12);

            String loddd = matcher.group(13);
            String lomm1 = matcher.group(14);
            String lomm2 = matcher.group(15);
            String lomm3 = matcher.group(16);
            String lomm4 = matcher.group(17);
            String lomm5 = matcher.group(18);
            String lohem = matcher.group(19);

            hour = haxToInt(hour);
            minute = haxToInt(minute);
            second = haxToInt(second);
            power = haxToInt(power);

            SimpleDateFormat formatUTC = new SimpleDateFormat("HH:mm:ss");
            formatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat formatGMS = new SimpleDateFormat("HH:mm:ss");
            formatGMS.setTimeZone(TimeZone.getDefault());

//            String time = hour + ":" + minute + ":" + second;
            timestamp = (Integer.parseInt(hour) + 8) + ":" + minute + ":" + second;


            targetPower = power;
            latitudeHem = lahem;
            longitudeHem = lohem;

            if (latitudeHem.equals("4E")) latitudeHem = "N";
            else if (latitudeHem.equals("53")) latitudeHem = "S";

            if (longitudeHem.equals("57")) longitudeHem = "W";
            else if (longitudeHem.equals("45")) longitudeHem = "E";

            StringBuilder laBf = new StringBuilder();
            StringBuilder laBfm = new StringBuilder();
            if (latitudeHem.equals("S")) {
                laBf.append("-");
                laBfm.append("-");
            }
            laBf.append(getPosStringNum(ladd));
            laBfm.append(getPosStringNum(lamm1));
            laBfm.append(".");
            laBfm.append(getPosStringNum(lamm2));
            laBfm.append(getPosStringNum(lamm3));
            laBfm.append(getPosStringNum(lamm4));
            laBfm.append(getPosStringNum(lamm5));
            double latitudem = Double.parseDouble(laBfm.toString());
            latitudem = latitudem / 60;
            latitude = Double.parseDouble(laBf.toString()) + latitudem;

            StringBuilder loBf = new StringBuilder();
            StringBuilder loBfm = new StringBuilder();
            if (longitudeHem.equals("W")) {
                loBf.append("-");
                loBfm.append("-");
            }
            loBf.append(getPosStringNum(loddd));
            loBfm.append(getPosStringNum(lomm1));
            loBfm.append(".");
            loBfm.append(getPosStringNum(lomm2));
            loBfm.append(getPosStringNum(lomm3));
            loBfm.append(getPosStringNum(lomm4));
            loBfm.append(getPosStringNum(lomm5));
            double longitudem = Double.parseDouble(loBfm.toString());
            longitudem = longitudem / 60;
            longitude = Double.parseDouble(loBf.toString()) + longitudem;
        }
    }

    private String getPosStringNum(String hexString) {
        String intString = haxToInt(hexString);
        if (intString.length() < 2) {
            intString = "0" + intString;
        }
        return intString;
    }
}