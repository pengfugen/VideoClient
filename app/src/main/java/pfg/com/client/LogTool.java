package pfg.com.client;

import android.util.Log;

/**
 * Created by FPENG3 on 2019/4/12.
 */

public class LogTool {

    private static String PRE_TAG = "VideoClient/";

    public static void logd(String tag, String msg) {
        Log.d(PRE_TAG+tag, msg);
    }
    public static void loge(String tag, String msg) {
        Log.e(PRE_TAG+tag, msg);
    }
}
