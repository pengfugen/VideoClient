package pfg.com.client;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;

public class MainActivity extends Activity {
    private Client mClient;

    private int count = 0;
    private Button bt_spspps;
    private Button bt_h264;
    private Button go_camera;

    private SenderHandlerThread mSenderThread;

    private final static int MSG_SEND_SPSPPS = 0;
    private final static int MSG_SEND_H264 = 1;

    private class SenderHandler extends Handler {
        private byte[] fileBytes;
        public SenderHandler(Looper looper) {
            super(looper);
            fileBytes = getByte("/storage/emulated/0/Movies/720p.h264");
        }

        @Override
        public void handleMessage(Message msg) {
            if(msg.what == MSG_SEND_SPSPPS) {
                handleSendSPSPPS(fileBytes);
            } else if(msg.what == MSG_SEND_H264) {
                handleSendH264(fileBytes);
            }
        }
    }

    private void handleSendSPSPPS(byte[] fileBytes) {
        mClient = Client.getInstance();
        mClient.connect();
        start: for (int i = 0; i < fileBytes.length; i++) {
            if (fileBytes[i] == 0 && fileBytes[i + 1] == 0
                    && fileBytes[i + 2] == 0
                    && fileBytes[i + 3] == 1) {
                end: for (int j = i + 4; j < fileBytes.length; j++) {
                    if (fileBytes[j] == 0
                            && fileBytes[j + 1] == 0
                            && fileBytes[j + 2] == 0
                            && fileBytes[j + 3] == 1) {
                        byte[] temp = Arrays.copyOfRange(
                                fileBytes, i, j);
                        mClient.sendLength(intToBytes(temp.length));
                        mClient.sendSPSPPS(temp);
                        count++;
                        if (count == 2) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    bt_spspps.setEnabled(false);
                                }
                            });
                            return;
                        }
                        break end;
                    }
                }
            }
        }
    }

    private void handleSendH264(byte[] fileBytes) {
        start: for (int i = 0; i < fileBytes.length; i++) {
            if (fileBytes[i] == 0 && fileBytes[i + 1] == 0
                    && fileBytes[i + 2] == 0
                    && fileBytes[i + 3] == 1) {
                end: for (int j = i + 4; j < fileBytes.length; j++) {
                    if (fileBytes[j] == 0
                            && fileBytes[j + 1] == 0
                            && fileBytes[j + 2] == 0
                            && fileBytes[j + 3] == 1) {
                        byte[] temp = Arrays.copyOfRange(
                                fileBytes, i, j);
                        mClient.sendLength(intToBytes(temp.length));
                        mClient.sendFrame(temp);
                        break end;
                    }
                }
            }
        }
    }

    private SenderHandler mHandler;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_spspps = (Button) findViewById(R.id.bt_spspps);
        bt_h264 = (Button) findViewById(R.id.bt_h264);

        bt_spspps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessage(MSG_SEND_SPSPPS);
            }
        });
        bt_h264.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessage(MSG_SEND_H264);
            }
        });

        go_camera = (Button) findViewById(R.id.go_camera);
        go_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Camera2VideoActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mSenderThread == null) {
            mSenderThread = new SenderHandlerThread("sender");
            mSenderThread.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mHandler == null) {
            mHandler = new SenderHandler(mSenderThread.getLooper());
        }

        Intent intent = new Intent(MainActivity.this, Camera2VideoActivity.class);
        startActivity(intent);

        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSenderThread.quitSafely();

        mSenderThread = null;
        mHandler = null;

    }

    private byte[] getByte(String path) {
        File f = new File(path);
        InputStream in;
        byte bytes[] = null;
        try {
            in = new FileInputStream(f);
            bytes = new byte[(int) f.length()];
            in.read(bytes);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bytes;
    }

    public byte[] intToBytes(int i) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (i & 0xff);
        bytes[1] = (byte) ((i >> 8) & 0xff);
        bytes[2] = (byte) ((i >> 16) & 0xff);
        bytes[3] = (byte) ((i >> 24) & 0xff);
        return bytes;
    }
}
