package com.jajale.watch.helper;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.jajale.watch.entitydb.Config;
import com.jajale.watch.entityno.BaseResult;
import com.jajale.watch.listener.OnFinishListener;
import com.jajale.watch.listener.ResponseListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.ConfigPreferencesUtils;

/**
 * Created by athena on 2015/11/11.
 * Email: lizhiqiang@bjjajale.com
 */
public class ConfigHelper implements ResponseListener, Response.ErrorListener {

    private Context mContext;
    private OnFinishListener onFinishListener;

    public ConfigHelper(Context context) {
        this.mContext = context;
    }

    public ConfigHelper(Context context, OnFinishListener listener) {
        this.mContext = context;
        this.onFinishListener = listener;
    }

    public void start() {
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        loadFinish();
    }

    @Override
    public void onSuccess(String url, String result) {
        try {
            if (!CMethod.isEmptyOrZero(result)) {
                Config config = new Gson().fromJson(result, Config.class);
                if (config != null) {
                    ConfigPreferencesUtils configPreferencesUtils = new ConfigPreferencesUtils(mContext);
                    configPreferencesUtils.handle(config);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadFinish();

    }

    @Override
    public void onFailure(String url, BaseResult response) {
//        L.e(AppConstants.CONFIG + "接口请求失败:");
        loadFinish();
    }

    @Override
    public void onError(String url, BaseResult result) {
//        L.e(AppConstants.CONFIG + "接口请求失败:" + result.getMessage());
        loadFinish();
    }

    private void loadFinish() {
        if (onFinishListener != null) {
            onFinishListener.onFinish();
        }

    }
}
