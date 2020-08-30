package com.example.events.uppercontrol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.Constants.SIGNAL_UPPER_DATA;
import static com.example.tools.SignalTools.haxToInt;

public class RecieveUpperControlEvent {

    public String latitude;

    public String longitude;

    public String latitudeHem;

    public String longitudeHem;

    public RecieveUpperControlEvent(String data) {
        Pattern pattern = Pattern.compile(SIGNAL_UPPER_DATA);
        Matcher matcher = pattern.matcher(data);
        if (matcher.find() && !"".equals(matcher.group(1)) && !"".equals(matcher.group(2)) &&
                !"".equals(matcher.group(3)) && !"".equals(matcher.group(4)) && !"".equals(matcher.group(5)) &&
                !"".equals(matcher.group(6)) && !"".equals(matcher.group(7)) && !"".equals(matcher.group(8)) &&
                !"".equals(matcher.group(9)) && !"".equals(matcher.group(10)) && !"".equals(matcher.group(11)) &&
                !"".equals(matcher.group(12)) && !"".equals(matcher.group(13)) && !"".equals(matcher.group(14))) {
            String ladd = matcher.group(1);
            String lamm1 = matcher.group(2);
            String lamm2 = matcher.group(3);
            String lamm3 = matcher.group(4);
            String lamm4 = matcher.group(5);
            String lamm5 = matcher.group(6);
            String lahem = matcher.group(7);
            latitudeHem = lahem;

            String loddd = matcher.group(8);
            String lomm1 = matcher.group(9);
            String lomm2 = matcher.group(10);
            String lomm3 = matcher.group(11);
            String lomm4 = matcher.group(12);
            String lomm5 = matcher.group(13);
            String lohem = matcher.group(14);
            longitudeHem = lohem;

            StringBuilder laBf = new StringBuilder();
            laBf.append(haxToInt(ladd));
            laBf.append(haxToInt(lamm1));
            laBf.append(".");
            laBf.append(haxToInt(lamm2));
            laBf.append(haxToInt(lamm3));
            laBf.append(haxToInt(lamm4));
            laBf.append(haxToInt(lamm5));
            latitude = laBf.toString();

            StringBuilder loBf = new StringBuilder();
            loBf.append(haxToInt(loddd));
            loBf.append(haxToInt(lomm1));
            loBf.append(".");
            loBf.append(haxToInt(lomm2));
            loBf.append(haxToInt(lomm3));
            loBf.append(haxToInt(lomm4));
            loBf.append(haxToInt(lomm5));
            longitude = loBf.toString();
        }
    }
}