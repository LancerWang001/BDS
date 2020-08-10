package com.serialport;

import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPort {
    private static final String TAG = "SerialPort";

    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    public SerialPort(File device, int baudrate, int flags)
            throws SecurityException, IOException, RuntimeException {

        if (!device.canRead() || !device.canWrite()) {
            try {
                /* Missing read/write permission, trying to chmod the file */
                Process su;
                su = Runtime.getRuntime().exec("/system/bin/su");
                String cmd = "chmod 666 " + device.getAbsolutePath() + "\n"
                        + "exit\n";
                su.getOutputStream().write(cmd.getBytes());
                if ((su.waitFor() != 0) || !device.canRead()
                        || !device.canWrite()) {
                    throw new RuntimeException("get root access failed.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //开启串口，传入物理地址、波特率、flags值
        mFd = open(device.getAbsolutePath(), baudrate, flags);
        if (mFd == null) {
            Log.e(TAG, "native open returns null");
            throw new IOException("can not generate serial port I/O.");
        }
        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);
    }

    //获取串口的输入流
    public InputStream getInputStream() {
        return mFileInputStream;
    }

    //获取串口的输出流
    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

    // JNI调用，开启串口
    private native static FileDescriptor open(String path, int baudrate, int flags);

    //关闭串口
    public native void close();

    static {
        System.out.println("===============Start Add .so===============");
        //加载库文件.so文件
        System.loadLibrary("serial_port");
        System.out.println("****************End Add .so****************");
    }
}
