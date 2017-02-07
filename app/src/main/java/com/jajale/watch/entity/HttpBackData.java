package com.jajale.watch.entity;

/**
 * Created by lilonghui on 2016/2/25.
 * Email:lilonghui@bjjajale.com
 */
public class HttpBackData {


    /**
     * code : 300
     * desc : 登录名不合法/用户名或者密码失败
     * time : 2016-02-25 10:37:17
     */

    private int code;
    private String desc;
    private String time;
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
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
