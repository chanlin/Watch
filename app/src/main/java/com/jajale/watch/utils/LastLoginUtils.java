package com.jajale.watch.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 记录最后登录的用户信息
 * Created by chunlongyuan on 12/2/15.
 */
public class LastLoginUtils {

    private SharedPreferences preferences;
    private static final String FILE_NAME = "preference_login";
    private static final String KEY_PHONE = "key_phone";
    private static final String KEY_USER_ID = "key_user_id";
    private static final String KEY_PASSWORD = "key_password";
    private static final String KEY_STATE = "key_state";
    private static final String KEY_IS_ADD_PHONE_BOOK = "key_is_add_phone_book";

    public LastLoginUtils(Context context) {
        this.preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_MULTI_PROCESS + Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
    }

    public void setPhone(String phone) {
        preferences.edit().putString(KEY_PHONE, phone).apply();
    }
    public void setUserId(String userId) {
        preferences.edit().putString(KEY_USER_ID, userId).apply();
    }
    public void setPassword(String password) {
        preferences.edit().putString(KEY_PASSWORD, password).apply();
    }

    public String getUserId() {
        return preferences.getString(KEY_USER_ID, null);
    }
    public String getPhone() {
        return preferences.getString(KEY_PHONE, null);
    }

    public String getPassword() {
        return preferences.getString(KEY_PASSWORD, null);
    }

    /**
     * 标记账号登录
     */
    public void login() {
        preferences.edit().putBoolean(KEY_STATE, true).apply();
    }

    /**
     * 标记账号退出
     */
    public void logout() {
        preferences.edit().remove(KEY_STATE).apply();
    }

    /**
     * 是否已经登录
     * @return
     */
    public boolean isLogin() {
        return preferences.getBoolean(KEY_STATE, false);
    }
    /**
     * 是否为该手表添加电话本
     * @return
     */
    public boolean isAddPhoneBook(String watchID) {
        return preferences.getBoolean(KEY_IS_ADD_PHONE_BOOK+watchID, false);
    }
    /**
     * 标记该手表添加电话簿
     * @return
     */
    public void AddPhoneBook(String watchID) {
        preferences.edit().putBoolean(KEY_IS_ADD_PHONE_BOOK+watchID, true).apply();
    }
    /**
     * 解除该手表后标记未添加电话本
     */
    public void RemovePhoneBook(String watchID) {
        preferences.edit().remove(KEY_IS_ADD_PHONE_BOOK+watchID).apply();
    }

}
