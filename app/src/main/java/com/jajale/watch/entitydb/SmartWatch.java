package com.jajale.watch.entitydb;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by lilonghui on 2016/2/25.
 * Email:lilonghui@bjjajale.com
 */

@DatabaseTable(tableName = "SmartWatch")
public class SmartWatch implements Parcelable {

    public static String ID = "user_id";
    /**
     * birthday : 2015-01-01
     * electricities : 100
     * gps_lat : 0
     * gps_lon : 0
     * header_img_url : http://192.168.1.127:20000/Images/123.jpg
     * is_manage : 0
     * nick_name :
     * phone_num_binded : 13812345678
     * relation :
     * role_type : 1
     * sex : 0
     * user_id : 1
     */

    @DatabaseField(id = true, columnName = "user_id")//用户id
    private String user_id;
    @DatabaseField(columnName = "birthday")//生日
    private String birthday;
    @DatabaseField(columnName = "electricities")//电量
    private int electricities;
    @DatabaseField(columnName = "gps_lat")//经度
    private String gps_lat;
    @DatabaseField(columnName = "gps_lon")//纬度
    private String gps_lon;
    @DatabaseField(columnName = "header_img_url")//头像地址
    private String header_img_url;
    @DatabaseField(columnName = "is_manage")//是否是管理员
    private int is_manage;
    @DatabaseField(columnName = "nick_name")//昵称
    private String nick_name;
    @DatabaseField(columnName = "phone_num_binded")//电话
    private String phone_num_binded;
    @DatabaseField(columnName = "relation")//关系
    private String relation;
    @DatabaseField(columnName = "role_type")//类型（手表还是用户）
    private int role_type;
    @DatabaseField(columnName = "sex")//性别
    private int sex;
    @DatabaseField(columnName = "watch_imei_binded")//imei号
    private String watch_imei_binded;
    @DatabaseField(columnName = "work_mode")//工作模式
    private int work_mode;
    @DatabaseField(columnName = "switch_step")//计步开关
    private int switch_step;

    @DatabaseField(columnName = "data_type")//计步开关
    private String data_type;
    @DatabaseField(columnName = "address")//计步开关
    private String address;
    @DatabaseField(columnName = "create_time")//计步开关
    private String create_time;


    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public boolean isManger(){
        return is_manage==1;
    }

    public String extra;
    public int getWork_mode() {
        return work_mode;
    }

    public void setWork_mode(int work_mode) {
        this.work_mode = work_mode;
    }

    public int getSwitch_step() {
        return switch_step;
    }

    public void setSwitch_step(int switch_step) {
        this.switch_step = switch_step;
    }

    public String getWatch_imei_binded() {
        return watch_imei_binded;
    }

    public void setWatch_imei_binded(String watch_imei_binded) {
        this.watch_imei_binded = watch_imei_binded;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setElectricities(int electricities) {
        this.electricities = electricities;
    }

    public void setGps_lat(String gps_lat) {
        this.gps_lat = gps_lat;
    }

    public void setGps_lon(String gps_lon) {
        this.gps_lon = gps_lon;
    }

    public void setHeader_img_url(String header_img_url) {
        this.header_img_url = header_img_url;
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

    public void setRole_type(int role_type) {
        this.role_type = role_type;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBirthday() {
        return birthday;
    }

    public int getElectricities() {
        return electricities;
    }

    public String getGps_lat() {
        return gps_lat;
    }

    public String getGps_lon() {
        return gps_lon;
    }

    public String getHeader_img_url() {
        return header_img_url;
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

    public int getRole_type() {
        return role_type;
    }

    public int getSex() {
        return sex;
    }

    public String getUser_id() {
        return user_id;
    }

    public SmartWatch() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.user_id);
        dest.writeString(this.birthday);
        dest.writeInt(this.electricities);
        dest.writeString(this.gps_lat);
        dest.writeString(this.gps_lon);
        dest.writeString(this.header_img_url);
        dest.writeInt(this.is_manage);
        dest.writeString(this.nick_name);
        dest.writeString(this.phone_num_binded);
        dest.writeString(this.relation);
        dest.writeInt(this.role_type);
        dest.writeInt(this.sex);
        dest.writeString(this.watch_imei_binded);
        dest.writeInt(this.work_mode);
        dest.writeInt(this.switch_step);
        dest.writeString(this.data_type);
        dest.writeString(this.address);
        dest.writeString(this.create_time);
        dest.writeString(this.extra);
    }

    protected SmartWatch(Parcel in) {
        this.user_id = in.readString();
        this.birthday = in.readString();
        this.electricities = in.readInt();
        this.gps_lat = in.readString();
        this.gps_lon = in.readString();
        this.header_img_url = in.readString();
        this.is_manage = in.readInt();
        this.nick_name = in.readString();
        this.phone_num_binded = in.readString();
        this.relation = in.readString();
        this.role_type = in.readInt();
        this.sex = in.readInt();
        this.watch_imei_binded = in.readString();
        this.work_mode = in.readInt();
        this.switch_step = in.readInt();
        this.data_type = in.readString();
        this.address = in.readString();
        this.create_time = in.readString();
        this.extra = in.readString();
    }

    public static final Creator<SmartWatch> CREATOR = new Creator<SmartWatch>() {
        public SmartWatch createFromParcel(Parcel source) {
            return new SmartWatch(source);
        }

        public SmartWatch[] newArray(int size) {
            return new SmartWatch[size];
        }
    };
}
