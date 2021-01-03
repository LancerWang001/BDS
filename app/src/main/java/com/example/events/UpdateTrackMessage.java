package com.example.events;

import com.example.beans.Position;

import java.util.HashMap;

public class UpdateTrackMessage {


    public String timestamp;
    public String targetPower;
    public HashMap<String, Position> targetMap;
    public String cardNum;

    public UpdateTrackMessage(String timestamp , String targetPower, HashMap<String, Position> targetMap,String cardNum){
        Position p = new Position();
        this.timestamp = timestamp;
        this.targetPower = targetPower;
        this.targetMap = targetMap;
        this.cardNum = cardNum;
    }

}
