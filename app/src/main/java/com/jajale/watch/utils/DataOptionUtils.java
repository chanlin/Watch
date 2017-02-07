package com.jajale.watch.utils;

import android.content.Context;

import org.apache.http.util.EncodingUtils;

import java.io.InputStream;

/**
 * Created by athena on 2015/12/15.
 * Email: lizhiqiang@bjjajale.com
 */
public class DataOptionUtils {

    /**
     * 读取本地的 raw中存好的json文件
     * @param c
     * @return
     */
    public static String readJsonFromRaw(Context c , int rawId){
        String json = "";
        try{
            InputStream in = c.getResources().openRawResource(rawId);
            int length = in.available();
            byte [] buffer = new byte [length];

            in.read(buffer);
            json = EncodingUtils.getString(buffer, "utf-8");

        }catch(Exception e){
            L.i("Utils", "readJsonFromRaw 方法出错:"+e);
        }
        return json;
    }


}
