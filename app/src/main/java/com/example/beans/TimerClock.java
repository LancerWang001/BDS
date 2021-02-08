package com.example.beans;

import android.util.Log;

import com.example.events.timer.TargetEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

public class TimerClock {
    public int time = 8;
    public String symbol;
    Timer timer;
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            time -= 1;
            if (time == 0) {
                cancel();
                timer.cancel();
            }
            Log.d("TimerClock:", Integer.toString(time));
            // dispatch event to show on desk
            EventBus.getDefault().post(new TargetEvent(symbol, Integer.toString(time)));
        }
    };

    public TimerClock(String symbol) {
        this.symbol = symbol;
    }

    public TimerClock(String symbol, int time, TimerTask task) {
        this.symbol = symbol;
        this.time = time;
        this.timerTask = task;
    }

    public void start() {
        if (timer != null) return;
        timer = new Timer();
        timer.schedule(timerTask, 0, 1000);
    }

    public void stop() {
        if (timer != null) timer.cancel();
    }
}

