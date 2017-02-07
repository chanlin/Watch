package com.jajale.watch.utils;

import android.text.TextUtils;
import android.util.Log;

import com.jajale.watch.PublicSwitch;

/**
 * Created by athena on 2015/11/11.
 * Email: lizhiqiang@bjjajale.com
 */
public class L {
    private static final String TAG = "jjl-Debug";
    public static void d(String tag, String msg) {
        if (PublicSwitch.isLog && !TextUtils.isEmpty(msg)) {
            Log.d(tag, " " + msg);
        }
    }

    public static void d(String msg) {
        if (PublicSwitch.isLog && !TextUtils.isEmpty(msg)) {
            Log.e(TAG, msg);
        }
    }

    /**
     * Send a {@link #} log message and log the exception.
     *
     * @param tag
     *            Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg
     *            The message you would like logged.
     * @param tr
     *            An exception to log
     */
    public static void d(String tag, String msg, Throwable tr) {
        if (PublicSwitch.isLog && !TextUtils.isEmpty(msg)) {
            Log.d(tag, msg, tr);
        }
    }

    public static void e(Throwable tr) {
        if (PublicSwitch.isLog) {
            Log.e(TAG, "", tr);
        }
    }

    public static void i(String msg) {
        if (PublicSwitch.isLog && !TextUtils.isEmpty(msg)) {
            Log.i(TAG, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (PublicSwitch.isLog && !TextUtils.isEmpty(msg)) {
            Log.i(tag, msg);
        }
    }

    /**
     * Send a {@link #} log message and log the exception.
     *
     * @param tag
     *            Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg
     *            The message you would like logged.
     * @param tr
     *            An exception to log
     */
    public static void i(String tag, String msg, Throwable tr) {
        if (PublicSwitch.isLog && !TextUtils.isEmpty(msg)) {
            Log.i(tag, msg, tr);
        }

    }

    /**
     * Send an {@link #} log message.
     *
     * @param tag
     *            Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg
     *            The message you would like logged.
     */
    public static void e(String tag, String msg) {
        if (PublicSwitch.isLog && !TextUtils.isEmpty(msg)) {
            Log.e(tag, msg);
        }
    }

    public static void e(String msg) {
        if (PublicSwitch.isLog && !TextUtils.isEmpty(msg)) {
            Log.e(TAG, msg);
        }
    }

    /**
     * Send a {@link #} log message and log the exception.
     *
     * @param tag
     *            Used to identify the source of a log message. It usually
     *            identifies the class or activity where the log call occurs.
     * @param msg
     *            The message you would like logged.
     * @param tr
     *            An exception to log
     */
    public static void e(String tag, String msg, Throwable tr) {
        if (PublicSwitch.isLog) {
            Log.e(tag, msg, tr);
        }
    }

    public static void e(String msg, Throwable tr) {
        if (PublicSwitch.isLog && !TextUtils.isEmpty(msg)) {
            Log.e(TAG, msg, tr);
        }
    }

    public static void systemErr(String msg) {
        // if (true) {
        if (PublicSwitch.isLog && !TextUtils.isEmpty(msg)) {
            if (msg != null) {
                Log.e(TAG, msg);
            }

        }
    }

}
