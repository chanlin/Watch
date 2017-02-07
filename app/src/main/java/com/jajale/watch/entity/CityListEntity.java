package com.jajale.watch.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.maps.model.LatLng;

public class CityListEntity implements Parcelable {
    private String cityName;
    private String childName;
    private LatLng latLng;
    private String weather;
    private String temperature;

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    @Override
    public String toString() {
        return "CityListEntity{" +
                "cityName='" + cityName + '\'' +
                ", childName='" + childName + '\'' +
                ", latLng=" + latLng +
                '}';
    }

    public CityListEntity() {
    }

    public CityListEntity(String cityName, String childName) {
        this.cityName = cityName;
        this.childName = childName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cityName);
        dest.writeString(this.childName);
        dest.writeParcelable(this.latLng, flags);
    }

    protected CityListEntity(Parcel in) {
        this.cityName = in.readString();
        this.childName = in.readString();
        this.latLng = in.readParcelable(LatLng.class.getClassLoader());
    }

    public static final Parcelable.Creator<CityListEntity> CREATOR = new Parcelable.Creator<CityListEntity>() {
        @Override
        public CityListEntity createFromParcel(Parcel source) {
            return new CityListEntity(source);
        }

        @Override
        public CityListEntity[] newArray(int size) {
            return new CityListEntity[size];
        }
    };
}
