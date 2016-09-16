package com.appmojo.sdk.utils;

import android.util.Log;

/**
 * Created by nutron on 4/29/15 AD.
 */
@SuppressWarnings("ALL")
public class AMLog {
    public static final String TAG = "AppMojo";
    private static boolean LOG_VERBOSE = true;
    private static boolean LOG_DEBUG = true;
    private static boolean LOG_INFO = true;
    private static boolean LOG_WARNING = true;
    private static boolean LOG_ERROR = true;

    private AMLog(){
    }

    /**
     * * To specify the log type that you want to show on logcat.
     * @param v : verbose log
     * @param d : debug log
     * @param i : info log
     * @param w : warning log
     * @param e : error log
     */
    public static void setEnableLog(boolean v, boolean d, boolean i, boolean w, boolean e) {
        LOG_VERBOSE = v;
        LOG_DEBUG = d;
        LOG_INFO = i;
        LOG_WARNING = w;
        LOG_ERROR = e;
    }

    public static void v(String msg) {
        if (LOG_VERBOSE) {
            Log.v(TAG, msg);
        }
    }

    public static void v(String msg, Throwable throwable) {
        if (LOG_VERBOSE) {
            Log.v(TAG, msg, throwable);
        }
    }

    public static void v(String tag, String msg, Object... arg) {
        if (LOG_VERBOSE) {
            Log.v(tag, String.format(msg, arg));
        }
    }

    public static void v(String tag, String msg, Throwable throwable) {
        if (LOG_VERBOSE) {
            Log.v(tag, msg, throwable);
        }
    }

    public static void d(String msg) {
        if (LOG_DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void d(String msg, Throwable throwable) {
        if (LOG_DEBUG) {
            Log.d(TAG, msg, throwable);
        }
    }

    public static void d(String tag, String msg, Object... arg) {
        if (LOG_DEBUG) {
            Log.d(tag, String.format(msg, arg));
        }
    }

    public static void d(String tag, String msg, Throwable throwable) {
        if (LOG_DEBUG) {
            Log.d(tag, msg, throwable);
        }
    }

    public static void i(String msg) {
        if (LOG_INFO) {
            Log.i(TAG, msg);
        }
    }

    public static void i(String msg, Throwable throwable) {
        if (LOG_INFO) {
            Log.i(TAG, msg, throwable);
        }
    }

    public static void i(String tag, String msg, Object... arg) {
        if (LOG_INFO) {
            Log.i(tag, String.format(msg, arg));
        }
    }

    public static void i(String tag, String msg, Throwable throwable) {
        if (LOG_INFO) {
            Log.i(tag, msg, throwable);
        }
    }

    public static void w( String msg) {
        if (LOG_WARNING) {
            Log.w(TAG, msg);
        }
    }

    public static void w(String msg, Throwable throwable) {
        if (LOG_WARNING) {
            Log.w(TAG, msg, throwable);
        }
    }

    public static void w(String tag, String msg, Object... arg) {
        if (LOG_WARNING) {
            Log.w(tag, String.format(msg, arg));
        }
    }

    public static void w(String tag, String msg, Throwable throwable) {
        if (LOG_WARNING) {
            Log.w(tag, msg, throwable);
        }
    }

    public static void e(String msg) {
        if (LOG_ERROR) {
            Log.e(TAG, msg);
        }
    }

    public static void e(String msg, Throwable throwable) {
        if (LOG_ERROR) {
            Log.e(TAG, msg, throwable);
        }
    }

    public static void e(String tag, String msg, Object... arg) {
        if (LOG_ERROR) {
            Log.e(tag, String.format(msg, arg));
        }
    }

    public static void e(String tag, String msg, Throwable throwable) {
        if (LOG_ERROR) {
            Log.e(tag, msg, throwable);
        }
    }
}
