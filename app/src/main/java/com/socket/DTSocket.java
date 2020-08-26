package com.socket;

import android.util.Log;

import com.example.events.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.Constants.*;

public class DTSocket {
    Socket socket;
    InputStream inputStream;
    OutputStream outputStream;

    public void connect() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(DT_HOST, DT_PORT);
                    inputStream = socket.getInputStream();
                    outputStream = socket.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                while (true) {
                    DTSocket.this.readData();
                }

            }
        }).start();
    }

    public void writeData(String data) {
        try {
            String fullData = data + "\r\n";
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
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            socket.close();
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
