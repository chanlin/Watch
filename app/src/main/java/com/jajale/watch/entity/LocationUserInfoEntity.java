package com.jajale.watch.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.model.LatLng;

/**
 * Created by lilonghui on 2015/12/1.
 * Email:lilonghui@bjjajale.com
 */
public class LocationUserInfoEntity implements Parcelable {

    private LatLng latLng;//经纬度
    private String  nickName;//昵称
    private String watchID;//手表id
    private int headView;//头像
    private int headView_press;//头像
    private int BaseType;
    private String baseTime;

    public String getBaseTime() {
        return baseTime;
    }

    public void setBaseTime(String baseTime) {
        this.baseTime = baseTime;
    }

    private int electricity;

    public int getElectricity() {
        return electricity;
    }

    public void setElectricity(int electricity) {
        this.electricity = electricity;
    }

    private String Address;

    public int getBaseType() {
        return BaseType;
    }

    public void setBaseType(int baseType) {
        BaseType = baseType;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getHeadView_press() {
        return headView_press;
    }

    public void setHeadView_press(int headView_press) {
        this.headView_press = headView_press;
    }

    private String phone;//手机
    private int sex;//性别
    private int isManager;//权限
    private int port;
    private String ip;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getIsManager() {
        return isManager;
    }

    public void setIsManager(int isManager) {
        this.isManager = isManager;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getWatchID() {
        return watchID;
    }

    public void setWatchID(String watchID) {
        this.watchID = watchID;
    }

    public int getHeadView() {
        return headView;
    }

    public void setHeadView(int headView) {
        this.headView = headView;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.latLng, 0);
        dest.writeString(this.nickName);
        dest.writeString(this.watchID);
        dest.writeInt(this.headView);
        dest.writeInt(this.headView_press);
        dest.writeString(this.phone);
        dest.writeInt(this.sex);
        dest.writeInt(this.isManager);
        dest.writeInt(this.port);
        dest.writeInt(this.BaseType);
        dest.writeString(this.ip);
        dest.writeString(this.Address);
    }

    public LocationUserInfoEntity() {
    }

    protected LocationUserInfoEntity(Parcel in) {
        this.latLng = in.readParcelable(LatLng.class.getClassLoader());
        this.nickName = in.readString();
        this.watchID = in.readString();
        this.headView = in.readInt();
        this.headView_press = in.readInt();
        this.phone = in.readString();
        this.sex = in.readInt();
        this.isManager = in.readInt();
        this.port = in.readInt();
        this.BaseType = in.readInt();
        this.ip = in.readString();
        this.Address = in.readString();
    }

    public static final Parcelable.Creator<LocationUserInfoEntity> CREATOR = new Parcelable.Creator<LocationUserInfoEntity>() {
        public LocationUserInfoEntity createFromParcel(Parcel source) {
            return new LocationUserInfoEntity(source);
        }

        public LocationUserInfoEntity[] newArray(int size) {
            return new LocationUserInfoEntity[size];
        }
    };
}
