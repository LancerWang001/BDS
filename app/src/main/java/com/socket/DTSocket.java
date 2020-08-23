package com.socket;

import android.app.usage.UsageEvents;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DTSocket {
    Socket socket;
    InputStream inputStream;
    OutputStream outputStream;
    ExecutorService executorService;

    public void connect(){
        executorService = Executors.newCachedThreadPool();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("192.168.0.104",8080);
                    inputStream = socket.getInputStream();
                    outputStream = socket.getOutputStream();
                }catch (IOException e){
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
                while (true){
                    DTSocket.this.readData();
                }

            }
        }).start();
    }

    public void writeData(String data){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String fullData = data + "\r\n";
                    outputStream.write(fullData.getBytes());
                    outputStream.flush();
                } catch (IOException e){
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    public void readData(){
        byte[] bt = new byte[1024];
        try{
            int size = inputStream.read(bt);
            String data = new String(bt, 0, size);
            Log.d("Socket Receive ", data);
            EventBus.getDefault().post(data);
        }catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void close(){
       try{
           socket.close();
           inputStream.close();
           outputStream.close();
       }catch (IOException e){
           e.printStackTrace();
       }
       catch (Exception e){
           e.printStackTrace();
       }
    }
}
