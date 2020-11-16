package com.netease.nim.uikit.util.screenshot;

import android.util.Log;

/**
 * MLog
 *
 * @author zhuanghongji
 */

public class MLog {

    private static final String TAG = "ScreenshotManager";

    private static boolean isLogEnable = false;

    static void enableLog(boolean enable) {
        MLog.isLogEnable = enable;
    }

    public static void v(String msg, Object... args) {
        if (!isLogEnable) {
            return;
        }
        String message = String.format(msg, args);
        Log.v(TAG, message);
    }
}
