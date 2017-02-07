package com.jajale.watch.entitydb;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.jajale.watch.utils.CMethod;

import java.text.ParseException;

/**
 * Created by lilonghui on 2016/3/1.
 * Email:lilonghui@bjjajale.com
 */
public class MsgMember implements Parcelable {

    public static final String KEY = "MsgMember";

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
    @DatabaseField(columnName = "user_identity")//用户分类 系统1 默认用户2
    public Integer user_identity = 2;
    @DatabaseField(columnName = "create_time")//手表用户数据更新时间
    public Long create_time;
    @DatabaseField(columnName = "update_time")//手表用户数据更新时间
    public Long update_time;
    @DatabaseField(columnName = "count_unread")//手表用户未读消息数
    public Integer count_unread;
    @DatabaseField(columnName = "count_msg")//手表用户消息总数
    public Integer count_msg;
    @DatabaseField(columnName = "last_msg_type")//最后一封消息的类型
    public String last_msg_type;
    @DatabaseField(columnName = "last_msg_desc")//最后一封消息的描述
    public String last_msg_desc;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getElectricities() {
        return electricities;
    }

    public void setElectricities(int electricities) {
        this.electricities = electricities;
    }

    public String getGps_lat() {
        return gps_lat;
    }

    public void setGps_lat(String gps_lat) {
        this.gps_lat = gps_lat;
    }

    public String getGps_lon() {
        return gps_lon;
    }

    public void setGps_lon(String gps_lon) {
        this.gps_lon = gps_lon;
    }

    public String getHeader_img_url() {
        return header_img_url;
    }

    public void setHeader_img_url(String header_img_url) {
        this.header_img_url = header_img_url;
    }

    public int getIs_manage() {
        return is_manage;
    }

    public void setIs_manage(int is_manage) {
        this.is_manage = is_manage;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getPhone_num_binded() {
        return phone_num_binded;
    }

    public void setPhone_num_binded(String phone_num_binded) {
        this.phone_num_binded = phone_num_binded;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public int getRole_type() {
        return role_type;
    }

    public void setRole_type(int role_type) {
        this.role_type = role_type;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getWatch_imei_binded() {
        return watch_imei_binded;
    }

    public void setWatch_imei_binded(String watch_imei_binded) {
        this.watch_imei_binded = watch_imei_binded;
    }

    public Integer getUser_identity() {
        return user_identity;
    }

    public void setUser_identity(Integer user_identity) {
        this.user_identity = user_identity;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public Long getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Long update_time) {
        if (update_time == 0) {
            setUpdate_time("");
        } else {
            this.update_time = update_time;
        }
    }

    public void setUpdate_time(String update_time) {
        if (!CMethod.isEmpty(update_time)) {
            Long result = null;
            try {
                if (update_time == null) {
                    result = System.currentTimeMillis();
                } else {
                    result = Long.parseLong(update_time);
                }

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    result = CMethod.getTimeMillion(update_time);
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            }
            this.update_time = result;
        } else {
            this.update_time = Long.parseLong("0");
        }


    }

    public Integer getCount_unread() {
        return count_unread;
    }

    public void setCount_unread(Integer count_unread) {
        this.count_unread = count_unread;
    }

    public Integer getCount_msg() {
        return count_msg;
    }

    public void setCount_msg(Integer count_msg) {
        this.count_msg = count_msg;
    }

    public String getLast_msg_type() {
        return last_msg_type;
    }

    public void setLast_msg_type(String last_msg_type) {
        this.last_msg_type = last_msg_type;
    }

    public String getLast_msg_desc() {
        return last_msg_desc;
    }

    public void setLast_msg_desc(String last_msg_desc) {
        this.last_msg_desc = last_msg_desc;
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
        dest.writeValue(this.user_identity);
        dest.writeValue(this.create_time);
        dest.writeValue(this.update_time);
        dest.writeValue(this.count_unread);
        dest.writeValue(this.count_msg);
        dest.writeString(this.last_msg_type);
        dest.writeString(this.last_msg_desc);
    }

    public MsgMember() {
    }

    protected MsgMember(Parcel in) {
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
        this.user_identity = (Integer) in.readValue(Integer.class.getClassLoader());
        this.create_time = (Long) in.readValue(Long.class.getClassLoader());
        this.update_time = (Long) in.readValue(Long.class.getClassLoader());
        this.count_unread = (Integer) in.readValue(Integer.class.getClassLoader());
        this.count_msg = (Integer) in.readValue(Integer.class.getClassLoader());
        this.last_msg_type = in.readString();
        this.last_msg_desc = in.readString();
    }

    public static final Parcelable.Creator<MsgMember> CREATOR = new Parcelable.Creator<MsgMember>() {
        public MsgMember createFromParcel(Parcel source) {
            return new MsgMember(source);
        }

        public MsgMember[] newArray(int size) {
            return new MsgMember[size];
        }
    };
}
