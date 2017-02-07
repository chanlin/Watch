package com.jajale.watch.utils;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 和json有关的工具方法都放这里
 * @author pig
 *
 */
public class JSONUtils {

	public static String getString(JSONObject rootJO, String key) throws JSONException {
		return (rootJO != null && !TextUtils.isEmpty(key) && rootJO.has(key)) ? rootJO.getString(key) : null;
	}

	public static int getInt(JSONObject rootJO, String key) throws JSONException {
		return (rootJO != null && !TextUtils.isEmpty(key) && rootJO.has(key)) ? rootJO.getInt(key) : 300;
	}

}
