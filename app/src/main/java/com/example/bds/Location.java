package com.example.bds;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.events.LocationEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Location extends View {
    //    定义画笔
    Paint paint;

    int time = 0;
    Timer timer = new Timer();

    ArrayList deviceCardNumArray;

    public Location(Context context){
        super(context);
        EventBus.getDefault().register(this);
        paint = new Paint();
        paint.setColor(Color.GREEN);
   }

    public Location(Context context, ArrayList deviceCardNumArray){
        super(context);
        this.deviceCardNumArray = deviceCardNumArray;
        EventBus.getDefault().register(this);
        paint = new Paint();
        paint.setColor(Color.GREEN);
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
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Location.this.time += 2;
                if (Location.this.time > 200) {
                    Location.this.timer.cancel();
                    return;
                }
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
        canvas.drawColor(Color.GRAY);
        //canvas.drawRect(new Rect(0,0,100,100),paint);
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL);
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
        paint.setStrokeWidth(15);
        int x1 = 60 + time * 2;
        int x2 = 360 - time * 2;
        int y1 = 100 + time * 2;
        int y2 = 300 - time * 2;
        canvas.drawPoint(x1, y1, paint);
        canvas.drawPoint(x2, y2, paint);
        canvas.restore();
        super.draw(canvas);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LocationEvent event) {
        Log.d("GPD destination" , Double.toString(event.message[0]));
    }
}
