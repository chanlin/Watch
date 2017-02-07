package com.jajale.watch.entity;

/**
 * Created by athena on 2016/2/20.
 * Email: lizhiqiang@bjjajale.com
 */
public class BindWatchData {

    /**
     * birthday :
     * gps_lat : 0.00
     * gps_lon : 0.00
     * is_manage : 0
     * nick_name :
     * phone_num_binded :
     * relation :
     * sex : 0
     * switch_step : 0
     * user_id : 5005
     * watch_imei_binded : 868056021028840
     * work_mode :
     */

    private String birthday;
    private String gps_lat;
    private String gps_lon;
    private int is_manage;
    private String nick_name;
    private String phone_num_binded;
    private String relation;
    private int sex;
    private int switch_step;
    private String user_id;
    private String watch_imei_binded;
    private String work_mode;

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setGps_lat(String gps_lat) {
        this.gps_lat = gps_lat;
    }

    public void setGps_lon(String gps_lon) {
        this.gps_lon = gps_lon;
    }

    public void setIs_manage(int is_manage) {
        this.is_manage = is_manage;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public void setPhone_num_binded(String phone_num_binded) {
        this.phone_num_binded = phone_num_binded;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setSwitch_step(int switch_step) {
        this.switch_step = switch_step;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setWatch_imei_binded(String watch_imei_binded) {
        this.watch_imei_binded = watch_imei_binded;
    }

    public void setWork_mode(String work_mode) {
        this.work_mode = work_mode;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getGps_lat() {
        return gps_lat;
    }

    public String getGps_lon() {
        return gps_lon;
    }

    public int getIs_manage() {
        return is_manage;
    }

    public String getNick_name() {
        return nick_name;
    }

    public String getPhone_num_binded() {
        return phone_num_binded;
    }

    public String getRelation() {
        return relation;
    }

    public int getSex() {
        return sex;
    }

    public int getSwitch_step() {
        return switch_step;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getWatch_imei_binded() {
        return watch_imei_binded;
    }

    public String getWork_mode() {
        return work_mode;
    }
}
