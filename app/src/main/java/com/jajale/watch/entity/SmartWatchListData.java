package com.jajale.watch.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.jajale.watch.entitydb.SmartWatch;

import java.util.List;

/**
 * Created by lilonghui on 2016/2/25.
 * Email:lilonghui@bjjajale.com
 */
public class SmartWatchListData implements Parcelable {

    public static String KEY="SmartWatchListData_key";
    private List<SmartWatch> watchList;


    public List<SmartWatch> getWatchList() {
        return watchList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(watchList);
    }

    public SmartWatchListData() {
    }

    protected SmartWatchListData(Parcel in) {
        this.watchList = in.createTypedArrayList(SmartWatch.CREATOR);
    }

    public static final Parcelable.Creator<SmartWatchListData> CREATOR = new Parcelable.Creator<SmartWatchListData>() {
        public SmartWatchListData createFromParcel(Parcel source) {
            return new SmartWatchListData(source);
        }

        public SmartWatchListData[] newArray(int size) {
            return new SmartWatchListData[size];
        }
    };
}
