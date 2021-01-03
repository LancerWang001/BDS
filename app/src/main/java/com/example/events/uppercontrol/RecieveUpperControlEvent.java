package com.example.events.uppercontrol;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.util.Log;

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

            String time = hour + ":" + minute + ":" + second;
            try {
                Date upperDate = formatUTC.parse(time);
                timestamp = formatGMS.format(upperDate);
            } catch (Exception e) {
                timestamp = (Integer.parseInt(hour) + 8) + ":" + minute + ":" + second;
                Log.d("upperDate parse error:", time);
            }

            targetPower = power;
            latitudeHem = lahem;
            longitudeHem = lohem;

            if (latitudeHem.equals("4E")) latitudeHem = "N";
            else if (latitudeHem.equals("53")) latitudeHem = "S";

            if (longitudeHem.equals("57")) longitudeHem = "W";
            else if (longitudeHem.equals("45")) longitudeHem = "E";

            StringBuilder laBf = new StringBuilder();
            if (latitudeHem.equals("S")) {
                laBf.append("-");
            }
            laBf.append(haxToInt(ladd));
            laBf.append(".");
            laBf.append(haxToInt(lamm1));
            laBf.append(haxToInt(lamm2));
            laBf.append(haxToInt(lamm3));
            laBf.append(haxToInt(lamm4));
            laBf.append(haxToInt(lamm5));
            latitude = Double.parseDouble(laBf.toString());

            StringBuilder loBf = new StringBuilder();
            if (longitudeHem.equals("W")) {
                loBf.append("-");
            }
            loBf.append(haxToInt(loddd));
            loBf.append(".");
            loBf.append(haxToInt(lomm1));
            loBf.append(haxToInt(lomm2));
            loBf.append(haxToInt(lomm3));
            loBf.append(haxToInt(lomm4));
            loBf.append(haxToInt(lomm5));
            longitude = Double.parseDouble(loBf.toString());
        }
    }
}