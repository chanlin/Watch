package com.jajale.watch.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.jajale.watch.AppConstants;
import com.jajale.watch.entity.ResultEntity;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.listener.LoginListener;
import com.jajale.watch.listener.OnFinishListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 登录
 * Created by lilonghui on 2016/06/12.
 * Email:lilonghui@bjjajale.com
 */
public class LoginUtils {
    public static void login(final Context context, final String user_name, final String password, final LoginListener listener)
    {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_name", user_name);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API, AppConstants.JAVA_LOGIN_URL, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                final ResultEntity fromJson = gson.fromJson(result, ResultEntity.class);
                //存储账号信息
                AccountUtils.updateAccount(user_name, password);
                //存储user
                UserUtils.updateUser(fromJson.getUser_id(), fromJson.getWatch_bind() + "", new OnFinishListener() {
                    @Override
                    public void onFinish() {
                        LastLoginUtils lastLoginUtils = new LastLoginUtils(context);
                        lastLoginUtils.setPhone(user_name);
                        lastLoginUtils.setPassword(password);
                        lastLoginUtils.setUserId(fromJson.getUser_id());
                        lastLoginUtils.login();
                        listener.success(fromJson);
                    }
                });
            }

            @Override
            public void onFailure(String result) {
                listener.error(result);
            }

            @Override
            public void onError() {
                listener.error("");
            }
        });
    }

}
