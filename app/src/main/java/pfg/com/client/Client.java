package pfg.com.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import android.util.Log;

/**
 * Socket通信的工具类(客户端)
 * Created by 禽兽先生
 * Created on 2016.03.06
 */
public class Client {
    private static Client mClient;
    private static Socket mSocket;
    // 要连接的服务器IP地址
    private static final String HOST_ADDRESS = "192.168.100.86"; //客户端的IP地址(设置->手机状态->IP)
    // 要连接的服务器端口号
    private static final int HOST_PORT = 12580;

    static String TAG = "Client";

    /**
     * 单例模式
     */
    public static Client getInstance() {
        if (mClient == null) {
            mClient = new Client();
        }
        return mClient;
    }

    /**
     * 打开Socket通道,连接服务器
     */
    private static Socket getSocket() {
        if (mSocket == null) {
            try {
                mSocket = new Socket(HOST_ADDRESS, HOST_PORT);
                Log.i(TAG, "socket成功连接1");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.i(TAG, "IOException:"+e.getMessage());
            }
        }
        return mSocket;
    }

    /**
     * 打开Socket通道,连接服务器
     */
    public void connect() {
        // TODO Auto-generated method stub
        mSocket = getSocket();
        Log.i(TAG, "socket成功连接2");
    }

    /**
     * 断开Socket连接
     */
    public void disconnect() {
        if (mSocket != null && mSocket.isConnected()) {
            try {
                mSocket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void sendLength(byte[] bytes) {
        try {
            if (!mSocket.isConnected()) {
                mSocket = getSocket();
            }
            OutputStream os = mSocket.getOutputStream();
            os.write(bytes);
            os.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void sendSPSPPS(byte[] bytes) {
        try {
            if (!mSocket.isConnected()) {
                mSocket = getSocket();
            }
            OutputStream os = mSocket.getOutputStream();
            os.write(bytes);
            os.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void sendFrame(byte[] bytes) {
        try {
            if (!mSocket.isConnected()) {
                mSocket = getSocket();
            }
            OutputStream os = mSocket.getOutputStream();
            os.write(bytes, 0, bytes.length);
            os.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
