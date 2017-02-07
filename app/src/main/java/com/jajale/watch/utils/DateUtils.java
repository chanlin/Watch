package com.jajale.watch.utils;

import android.content.Context;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by lilonghui on 2015/12/9.
 * Email:lilonghui@bjjajale.com
 */
public class DateUtils {
    private SimpleDateFormat sdf;

    public DateUtils(Context context) {
        sdf = new SimpleDateFormat("yyyyMMdd");
    }

    //获取当前日期
    public String getToday() {
        Date d = new Date(System.currentTimeMillis());
        String date = sdf.format(d);
        return date;
    }

    //截取本月
    public String getCurrentMonth() {
        Date d = new Date(System.currentTimeMillis());
        String t = sdf.format(d);
        String m = t.substring(4, 6);
        return m;
    }

    //截取本年
    public String getCurrentYear() {
        Date d = new Date(System.currentTimeMillis());
        String t = sdf.format(d);
        String y = t.substring(0, 4);
        return y;
    }



    //判断闰年
    public static boolean isLeapYear(int year) {
        if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
            return true;
        }
        return false;
    }
}
