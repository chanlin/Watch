package com.jajale.watch.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.jajale.watch.entity.SPKeyConstants;


/**
 * 用于操作有关手机硬件 rom等配置信息的SP
 */

public class PhoneSPUtils {

//    private Context context;
    protected SharedPreferences preferences;

    public PhoneSPUtils(Context context) {
//        this.context = context;
        this.preferences = context.getSharedPreferences(SPKeyConstants.JJL_APP_SHAREPREFERENCE, Context.MODE_MULTI_PROCESS + Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
    }

    public PhoneSPUtils(Context context, String filename) {
//        this.context = context;
        this.preferences = context.getSharedPreferences(filename, Context.MODE_MULTI_PROCESS + Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
    }

    public void save(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public void save(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public void save(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    public void save(String key, long value){ preferences.edit().putLong(key, value).apply();}

    public void save(String key, float value){preferences.edit().putFloat(key, value).apply();}


    public boolean getBoolean(String key, boolean value) {
        return preferences.getBoolean(key, value);
    }
    public float getFloat(String key,float value){
        return preferences.getFloat(key, value);
    }

    public long getLong(String key,long value){
        return preferences.getLong(key, value);
    }

    public String getString(String key) {
        return preferences.getString(key, null);
    }

    public int getInt(String key) {
        return preferences.getInt(key, -1);
    }
    public int getIntZ(String key) {
        return preferences.getInt(key, 0);
    }
    public float getFloat(String key){return  preferences.getFloat(key,0);}


    public void remove(String key) {
        preferences.edit().remove(key).apply();
    }

    public void clear() {
        preferences.edit().clear().commit();
    }
}
