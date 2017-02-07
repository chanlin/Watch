package com.jajale.watch.utils;

import android.widget.Toast;

import com.jajale.watch.BaseApplication;

/**
 *
 * Toast 显示的通用工具
 *
 * Created by athena on 2015/11/19.
 * Email: lizhiqiang@bjjajale.com
 */
public class T {
    public static void s(String text) {
        Toast.makeText(BaseApplication.getContext(), text, Toast.LENGTH_SHORT).show();
    }public static void s(int resid) {
        Toast.makeText(BaseApplication.getContext(), resid, Toast.LENGTH_SHORT).show();
    }
}