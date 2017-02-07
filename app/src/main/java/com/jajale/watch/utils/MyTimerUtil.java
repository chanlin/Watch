package com.jajale.watch.utils;

/**
 * Created by Sunzhigang on 2016/3/14.
 */
public class MyTimerUtil {
    /**
     * one hour in ms
     */
    private static final int ONE_HOUR = 1 * 60 * 60 * 1000;
    /**
     * one minute in ms
     */
    private static final int ONE_MIN = 1 * 60 * 1000;
    /**
     * one second in ms
     */
    private static final int ONE_SECOND = 1 * 1000;

    /**
     * HH:mm:ss
     */
    public static String formatTime(long ms) {
        StringBuilder sb = new StringBuilder();
        int hour = (int) (ms / ONE_HOUR);
        int min = (int) ((ms % ONE_HOUR) / ONE_MIN);
        int sec = (int) (ms % ONE_MIN) / ONE_SECOND;
        if (hour == 0) {
//			sb.append("00:");
        } else if (hour < 10) {
            sb.append("0").append(hour).append(":");
        } else {
            sb.append(hour).append(":");
        }
        if (min == 0) {
            sb.append("00:");
        } else if (min < 10) {
            sb.append("0").append(min).append(":");
        } else {
            sb.append(min).append(":");
        }
        if (sec == 0) {
            sb.append("00");
        } else if (sec < 10) {
            sb.append("0").append(sec);
        } else {
            sb.append(sec);
        }
        return sb.toString();
    }
}
