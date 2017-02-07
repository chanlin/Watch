package com.jajale.watch.utils;

import android.content.Context;

/**
 * Created by yinzhiqun on 2015/10/8.
 *
 */
public class DensityUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static float getScale(Context context){
        return context.getResources().getDisplayMetrics().density;
    }

    public static int adjustFontSize(int screenWidth) {
        if (screenWidth <= 240) { // 240X320 屏幕
            return 14;
        } else if (screenWidth <= 320) { // 320X480 屏幕
            return 18;
        } else if (screenWidth <= 480) { // 480X800 或 480X854 屏幕
            return 26;
        } else if (screenWidth <= 540) { // 540X960 屏幕
            return 30;
        } else if (screenWidth <= 800) { // 800X1280 屏幕
            return 45;
        } else { // 大于 800X1280
            return 56;
        }
    }
}
