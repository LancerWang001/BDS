package com.socket;

import android.util.Log;

import com.example.events.EmittSocketEvent;
import com.example.events.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static com.Constants.DT_HOST;
import static com.Constants.DT_PORT;

public class DTSocket {
    public Socket socket;
    InputStream inputStream;
    OutputStream outputStream;

    String TAG = "DTSocket";

    private boolean isOpen;

    public void connect() {
        new Thread(() -> {
            try {
                EventBus.getDefault().post(new EmittSocketEvent("1"));
                socket = new Socket(DT_HOST, DT_PORT);
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                isOpen = true;
                EventBus.getDefault().post(new EmittSocketEvent("0"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            while (isOpen) {
                DTSocket.this.readData();
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
            if (socket == null) {
                connect();
            }
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
            Log.d(TAG, "Connect drop !!!");
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
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void failedConnect () {
        Log.d(TAG, "Connect drop !!!");
        close();
        EventBus.getDefault().post(new EmittSocketEvent("2"));
    }

    public boolean isConnected () {
        return socket != null && socket.isConnected();
    }
}