package com.example.bds.homefragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.beans.Position;
import com.example.events.LocationEvent;
import com.example.events.UpdateTrackMessage;
import com.example.events.uppercontrol.RecieveUpperControlEvent;
import static com.example.service.TruckingServce.getDistance;
import static com.example.service.TruckingServce.getSigleDistance;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class Location extends View {
    String TAG = "Location Canvas:";
    //    定义画笔
    Paint paint;

    int time = 0;
    public Timer timer = new Timer();

    HashMap<String, Position> targetMap = new HashMap<String, Position>();

    Position shipPos;

    double xMaxDistance;

    double yMaxDistance;

    public Location(Context context){
        super(context);
        EventBus.getDefault().register(this);
        paint = new Paint();
   }

    public Location(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public Location(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("CANVAS", canvas.toString());
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.GRAY);
        Log.d("Draw", "draw ..." + time);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GREEN);
        // draw lines
        int widthLines = 10;
        if (xMaxDistance != 0) {
            widthLines = (int) Math.ceil(xMaxDistance / 1000) * 2; // 一公里为一条直线
            if (widthLines > 20) widthLines = 20;
        };
        int heightLines = 10;
        if (yMaxDistance != 0) {
            heightLines = (int) Math.ceil(yMaxDistance / 1000) * 2;
            if (heightLines > 21) heightLines = 21;
        }
        int unitHeight = getHeight() / heightLines;
        int unitWidth = getWidth() / widthLines;

        for (int i = 0; i < heightLines; i ++) {
            int startY = unitHeight * i;
            int stopY = startY;
            int stopX = getWidth();
            canvas.drawLine(0, startY, stopX, stopY, paint);
        }

        for (int i = 0; i < widthLines; i ++) {
            int startX = unitWidth * i;
            int stopX = startX;
            int stopY = getHeight();
            canvas.drawLine(startX, 0, stopX, stopY, paint);
        }
        canvas.save();

        // draw points :
        // draw ship :
        if (null == shipPos) return;
        paint.setStrokeWidth(30);
        paint.setColor(Color.WHITE);
        canvas.drawPoint(shipPos.getxRange(), shipPos.getyRange(), paint);
        // draw targets :

        Set<String> keySet = targetMap.keySet();
        for (String carNum : keySet) {
            paint.setStrokeWidth(27);
            paint.setColor(Color.GREEN);
            Position position = targetMap.get(carNum);
            int xRange = position.getxRange();
            int yRange = position.getyRange();

            canvas.drawPoint(xRange, yRange, paint);
            Log.d(TAG, "xRang->" + xRange + " yRang->" + yRange + "canvas width: " + getWidth());
            paint.setStrokeWidth(15);
            paint.setColor(Color.BLACK);
            paint.setTextSize(25);
            canvas.drawText(carNum, (float) (xRange - 7.5), (float) (yRange + 5), paint);
        }
        canvas.restore();
    }

    private int calcXRange (double distance) {
        int distanceInt = (int) distance;
        int width = getWidth();
        int xRange = (int)(width * (distanceInt / xMaxDistance) * 0.5);
        Log.d(TAG, "xMaxDistance -> " + xMaxDistance);
        Log.d(TAG, "xRange -> " + xRange);
        return width / 2 + xRange;
    }

    private int calcYRange(double distance) {
        int distanceInt = (int) distance;
        int height = getHeight();
        int yRange = (int)(height * (distanceInt / yMaxDistance) * 0.5);
        Log.d(TAG, "yMaxDistance -> " + yMaxDistance);
        Log.d(TAG, "yRange -> " + yRange);
        return height / 2 - yRange;
    }

    private double findxMaxDistance (double distance) {
        if (Math.abs(distance) > xMaxDistance * 1.1) {
            return Math.abs(distance) * 1.1;
        } else {
            double max = 0;
            Set<String> keySet = targetMap.keySet();
            for (String key : keySet) {
                Position position = targetMap.get(key);
                assert position != null;
                if (Math.abs(position.getXdistance()) > max * 1.1) {
                    max = Math.abs(position.getXdistance()) * 1.1;
                }
            }
            return max;
        }
    }

    private double findyMaxDistance (double distance) {
        if (Math.abs(distance) > yMaxDistance * 1.1) {
            return Math.abs(distance) * 1.1;
        } else {
            double max = 0;
            Set<String> keySet = targetMap.keySet();
            for (String key : keySet) {
                Position position = targetMap.get(key);
                assert position != null;
                if (Math.abs(position.getYdistance()) > max * 1.1) {
                    max = Math.abs(position.getYdistance()) * 1.1;
                }
            }
            return max;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onMessageEvent(LocationEvent event) {
        double shipLatitude = event.message[0];
        double shipLongitude = event.message[1];
        shipPos = new Position();
        shipPos.setLongitude(shipLongitude);
        shipPos.setLatitude(shipLatitude);
        shipPos.setxRange(getWidth() / 2);
        shipPos.setyRange(getHeight() / 2);
        Log.d("Ship Position" , Double.toString(event.message[0]));
        invalidate();
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onMessage(RecieveUpperControlEvent mess) {
        // Add / Update target position
        Log.d("Position latitude:", Double.toString(mess.latitude));
        Log.d("Position longitude:", Double.toString(mess.longitude));
        if (null != shipPos) {
            Position p = new Position();
            p.setCardNum(mess.cardNum);
            p.setLatitude(mess.latitude);
            p.setLongitude(mess.longitude);
            p.setLatHem(mess.latitudeHem);
            p.setLongHem(mess.longitudeHem);
            double directDistance = getDistance(mess.latitude, mess.longitude, shipPos.getLatitude(), shipPos.getLongitude());
            double xDistance = getSigleDistance(mess.longitude, shipPos.getLongitude());
            double yDistance = getSigleDistance(mess.latitude, shipPos.getLatitude());
            xMaxDistance = findxMaxDistance(xDistance);
            yMaxDistance = findyMaxDistance(yDistance);
            p.setXdistance(xDistance);
            p.setYdistance(yDistance);
            p.setDistance(directDistance);
            p.setxRange(calcXRange(xDistance));
            p.setyRange(calcYRange(yDistance));
            targetMap.put(mess.cardNum, p);
            EventBus.getDefault().post(new UpdateTrackMessage(mess.timestamp, mess.targetPower, this.targetMap, mess.cardNum));
            invalidate();
        }
    }
}
