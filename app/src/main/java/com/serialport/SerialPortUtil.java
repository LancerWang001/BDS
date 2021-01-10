package com.serialport;

import android.util.Log;

import com.Constants;
import com.example.events.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPortUtil {
    private SerialPort serialPort = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    private ReceiveThread mReceiveThread = null;
    private boolean isStart = false;

    public boolean isStart() {
        return isStart;
    }

    /**
     * 打开串口，接收数据
     * 通过串口，接收单片机发送来的数据
     */
    public void openSerialPort() {
        try {
            // Add params here
            serialPort = new SerialPort(new File(Constants.SERIAL_PORT_ADDR), Constants.SERIAL_PORT_RATE, 0);
            //调用对象SerialPort方法，获取串口中"读和写"的数据流
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
            isStart = true;

        } catch (IOException e) {
            Log.d("SerialPortErr", e.toString());
            Log.d("SerialPortErr", "Can not open serial port.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getSerialPort();
    }

    /**
     * 关闭串口
     * 关闭串口中的输入输出流
     */
    public void closeSerialPort() {
        Log.i("test", "关闭串口");
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            isStart = false;
            if (null != serialPort) {
                serialPort.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送数据
     * 通过串口，发送数据到单片机
     *
     * @param data 要发送的数据
     */
    public void sendSerialPort(String data) {
        try {
            String fullData = data + "\r\n";
            byte[] sendData = fullData.getBytes();
            Log.d("sendData ", new String(sendData));
            outputStream.write(sendData);
            outputStream.flush();
        } catch (IOException e) {
            Log.d("SerialPortErr", e.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getSerialPort() {
        if (mReceiveThread == null) {
            mReceiveThread = new ReceiveThread();
        }
        Log.d("Thread", "Thread start");
        mReceiveThread.start();
    }

    /**
     * 接收串口数据的线程
     */

    private class ReceiveThread extends Thread {
        @Override
        public void run() {
            super.run();
            Log.d("Receive ", "Tread start !!");
            while (isStart) {
                if (inputStream == null) {
                    return;
                }
                byte[] readData = new byte[1024];
                try {
                    Log.d("GET Serialport ", "Start to Read");
//                    Thread.sleep(500);
                    int size = inputStream.read(readData);
                    Log.d("GET Serialport ", new String(readData));
                    if (size > 0) {
                        String readString = new String(readData, 0, size);
                        Log.d("Receive ", readString);
                        // Handle here
                        EventBus.getDefault().post(new MessageEvent(readString));
                    }
                } catch (IOException e) {
                    Log.d("SerialPortErr", e.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
