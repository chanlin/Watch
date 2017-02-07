package com.jajale.watch.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class WeatherEntity implements Parcelable {
     private List<CityListEntity> listEntities;

    public List<CityListEntity> getListEntities() {
        return listEntities;
    }

    public void setListEntities(List<CityListEntity> listEntities) {
        this.listEntities = listEntities;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.listEntities);
    }

    public WeatherEntity() {
    }

    protected WeatherEntity(Parcel in) {
        this.listEntities = new ArrayList<CityListEntity>();
        in.readList(this.listEntities, List.class.getClassLoader());
    }

    public static final Parcelable.Creator<WeatherEntity> CREATOR = new Parcelable.Creator<WeatherEntity>() {
        public WeatherEntity createFromParcel(Parcel source) {
            return new WeatherEntity(source);
        }

        public WeatherEntity[] newArray(int size) {
            return new WeatherEntity[size];
        }
    };
}
