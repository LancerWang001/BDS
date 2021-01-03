package com.example.bds;

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
//        shipPos = new Position();
//        shipPos.setLongitude(Double.parseDouble("107.328"));
//        shipPos.setLatitude(Double.parseDouble("37.829"));
//        shipPos.setxRange(getWidth() / 2);
//        shipPos.setyRange(getHeight() / 2);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                time ++;
               invalidate();
            }
        }, 0, 2000);
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
        int unitWidth = getWidth() / 10;
        for (int i = 0; i < 10; i ++) {
            int startY = unitWidth * i;
            int stopY = startY;
            int stopX = getWidth();
            canvas.drawLine(0, startY, stopX, stopY, paint);
        }

        for (int i = 0; i < 10; i ++) {
            int startX = unitWidth * i;
            int stopX = startX;
            int stopY = getWidth();
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
            paint.setStrokeWidth(15);
            paint.setColor(Color.GREEN);
            Position position = targetMap.get(carNum);
            int xRange = position.getxRange() * 4 / 5;
            int yRange = position.getyRange() * 4 / 5;

            canvas.drawPoint(xRange, yRange, paint);
            Log.d(TAG, "xRang->" + xRange + " yRang->" + yRange + "canvas width: " + getWidth());
            paint.setStrokeWidth(10);
            paint.setColor(Color.BLACK);
            canvas.drawText(carNum, (float) (xRange - 7.5), (float) (yRange + 3), paint);
        }
        canvas.restore();
    }

    private int calcXRange (double distance) {
        int distanceInt = (int) distance;
        int width = getWidth();
        Log.d(TAG, "xMaxDistance -> " + xMaxDistance);
        int xRange = (int)(width * (distanceInt / xMaxDistance));
        return width / 2 + xRange;
    }

    private int calcYRange(double distance) {
        int distanceInt = (int) distance;
        int height = getHeight();
        int yRange = (int)(height * (distanceInt / yMaxDistance));
        Log.d(TAG, "yMaxDistance -> " + yMaxDistance);
        return height / 2 + yRange;
    }

    private double findxMaxDistance (double distance) {
        if (Math.abs(distance) > xMaxDistance) {
            return Math.abs(distance) * 2;
        } else {
            double max = 0;
            Set<String> keySet = targetMap.keySet();
            for (String key : keySet) {
                Position position = targetMap.get(key);
                assert position != null;
                if (Math.abs(position.getXdistance()) > max) {
                    max = Math.abs(position.getxRange());
                }
            }
            return max * 2;
        }
    }

    private double findyMaxDistance (double distance) {
        if (Math.abs(distance) > yMaxDistance) {
            return Math.abs(distance) * 2;
        } else {
            double max = 0;
            Set<String> keySet = targetMap.keySet();
            for (String key : keySet) {
                Position position = targetMap.get(key);
                assert position != null;
                if (Math.abs(position.getYdistance()) > max) {
                    max = Math.abs(position.getYdistance());
                }
            }
            return max * 2;
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void onMessage(RecieveUpperControlEvent mess) {
        // Add / Update target position
        Log.d("Message:", mess.toString());
//        Log.d("onMeesage cardNum : ", mess.cardNum);
//        Log.d("onMessage1: ", Double.toString(mess.latitude));
//        Log.d("onMessage2: ", mess.latitudeHem);
//        Log.d("onMessage3: ", Double.toString(mess.longitude));
//        Log.d("onMessage4: ", mess.longitudeHem);
        if (mess.longitudeHem.equals("S")) {
            mess.longitude = Double.parseDouble("-" + mess.longitude);
        }
        if (mess.latitudeHem.equals("W")) {
            mess.latitude = Double.parseDouble("-" + mess.latitude);
        }
        if (null != shipPos) {
            Position p = new Position();
            p.setCardNum(mess.cardNum);
            p.setLatitude(mess.latitude);
            p.setLongitude(mess.longitude);
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
        }
    }
}
