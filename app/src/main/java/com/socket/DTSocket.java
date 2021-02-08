package com.socket;

import android.util.Log;

import com.example.beans.TimerClock;
import com.example.events.MessageEvent;
import com.example.events.ServiceEvent;
import com.utils.ClockTask;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import static com.Constants.DT_HOST;
import static com.Constants.DT_PORT;

public class DTSocket {
    String TAG = "DTSocket";
    public Socket socket;
    InputStream inputStream;
    OutputStream outputStream;

    private KeepAliveTimer keepAliveTimer;

    private class KeepAliveTimer {
        private Timer timer;
        public void start () {
            stop();
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    DTSocket.this.writeData("keepAlive");
                }
            }, 0, 60000);
        }
        public void stop () {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }
    }

    private boolean isOpen;

    public void connect() {
        new Thread(() -> {
            synchronized (DTSocket.this) {
                try {
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(DT_HOST, DT_PORT), 3000);
                    inputStream = socket.getInputStream();
                    outputStream = socket.getOutputStream();
                    isOpen = true;
                    if (null != keepAliveTimer) {
                        keepAliveTimer.stop();
                        keepAliveTimer = null;
                    }
                    keepAliveTimer = new KeepAliveTimer();
                    keepAliveTimer.start();
                    EventBus.getDefault().post(new ServiceEvent("DT"));
                } catch (IOException e) {
                    e.printStackTrace();
                    failedConnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                while (isOpen) {
                    DTSocket.this.readData();
                }
            }
        }).start();
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void writeData(String data) {
        try {
            String fullData = data + "\r\n";
            Log.d(TAG, fullData);
            outputStream.write(fullData.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readData() {
        byte[] bt = new byte[1024];
        try {
            int size = inputStream.read(bt);
            String data = new String(bt, 0, size);
            Log.d("Socket Receive ", data);
            EventBus.getDefault().post(new MessageEvent(data));
        } catch (IOException e) {
            failedConnect();
            e.printStackTrace();
        } catch (Exception e) {
            failedConnect();
        }
    }

    public void close() {
        try {
            if (socket != null) {
                socket.shutdownInput();
                socket.shutdownOutput();
            }
            do {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (socket != null && (!socket.isInputShutdown() || !socket.isOutputShutdown()));

            if (socket != null) {
                socket.close();
            }
            isOpen = false;
            socket = null;
            inputStream = null;
            outputStream = null;
            if (keepAliveTimer != null) {
                keepAliveTimer.stop();
                keepAliveTimer = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void failedConnect() {
        Log.d(TAG, "Connect drop !!!");
        EventBus.getDefault().post(new ServiceEvent("DT",true));
        close();
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }
}