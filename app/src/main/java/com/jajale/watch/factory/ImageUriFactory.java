package com.jajale.watch.factory;

import android.net.Uri;



/**
 * Created by yinzhiqun on 2015/11/3.
 *
 */
public class ImageUriFactory {



    public static Uri getAssetsUri(String filename){
        return  Uri.parse("asset:///" + filename);
    }

    public static Uri getFileUri(String filename){
        return  Uri.parse("file:///" + filename);
    }

    public static Uri getResUri(int resId){
        return Uri.parse("res:///" + resId);
    }

}
