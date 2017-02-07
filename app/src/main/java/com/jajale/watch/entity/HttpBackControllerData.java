package com.jajale.watch.entity;

import org.json.JSONObject;

/**
 * Created by lilonghui on 2016/2/25.
 * Email:lilonghui@bjjajale.com
 */
public class HttpBackControllerData {


    /**
     * code : 300
     * desc : 登录名不合法/用户名或者密码失败
     * time : 2016-02-25 10:37:17
     */

    private int code;
    private String desc;
    private String time;
    private JSONObject data;

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getTime() {
        return time;
    }
}
