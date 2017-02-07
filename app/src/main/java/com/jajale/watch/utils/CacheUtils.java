package com.jajale.watch.utils;


import android.content.Context;

import java.io.File;

/**
 * dir
 * Created by Issac on 7/19/13.
 */
public class CacheUtils {
    /**
     * Get the external app cache directory.
     *
     * @param context The context to use
     * @return The external cache dir
     */
    public static String getExternalCacheDir(Context context) {
        File file = context.getExternalCacheDir();//   --/storage/emulated/0/Android/data/com.jajale.watch/cache
        if (file == null) {
            file = context.getCacheDir();//   --/data/data/com.jajale.watch/
        }
        return file.getAbsolutePath() + File.separator;
    }
    /**
     * Get the external app cache directory.
     *
     * @param context The context to use
     * @return The external cache dir
     */
    public static String getExternalBookCacheDir(Context context) {
        File file = context.getExternalFilesDir("book");//   --/storage/emulated/0/Android/data/com.jajale.watch/cache
        if (file == null) {
            file = context.getCacheDir();//   --/data/data/com.jajale.watch/
        }
        return file.getAbsolutePath() + File.separator;
    }
//    public static boolean hasExternalCacheDir() {
//        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
//    }
}
