package com.liuzhuang.library.log;

import android.util.Log;

/**
 * Created by liuzhuang on 10/29/15.
 */
public class EZLog {
    public static boolean IS_DEBUG = true;
    private static final String LOG_TAG = "EZScalpel";

    public static void logi(String tag, Object msg) {
        if (IS_DEBUG) {
            Log.i(LOG_TAG + "_" + tag, msg == null ? "" : msg.toString());
        }
    }

    public static void logd(String tag, Object msg) {
        if (IS_DEBUG) {
            Log.d(LOG_TAG + "_" + tag, msg == null ? "" : msg.toString());
        }
    }

    public static void loge(String tag, Object msg) {
        if (IS_DEBUG) {
            Log.e(LOG_TAG + "_" + tag, msg == null ? "" : msg.toString());
        }
    }
}
