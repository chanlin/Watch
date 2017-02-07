package com.jajale.watch.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 *
 * 疫苗内容实体类
 *
 * Created by lilonghui on 2015/11/30.
 * Email:lilonghui@bjjajale.com
 */
public class VaccineContentData {

    public static final String KEY = "VaccineContentData_key";
    /**
     * vaccineName : 乙肝疫苗
     * number : 第一次
     * describe : 乙型病毒性肝炎
     * sort : 1
     * state : 0
     * time : 1900/1/1 0:00:00
     */


    private List<VaccineListEntity> vaccineList;

    public void setVaccineList(List<VaccineListEntity> vaccineList) {
        this.vaccineList = vaccineList;
    }

    public List<VaccineListEntity> getVaccineList() {
        return vaccineList;
    }

    public static class     VaccineListEntity implements Parcelable {
        private String vaccine_name;
        private String times;
        private String describes;
        private String sort;
        private String id;

        public String getVaccineID() {
            return id;
        }

        public void setVaccineID(String vaccineID) {
            this.id = vaccineID;
        }

        private int state;
        private String create_time;

        public void setVaccineName(String vaccineName) {
            this.vaccine_name = vaccineName;
        }

        public void setNumber(String number) {
            this.times = number;
        }

        public void setDescribe(String describe) {
            this.describes = describe;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public void setState(int state) {
            this.state = state;
        }

        public void setTime(String time) {
            this.create_time = time;
        }

        public String getVaccineName() {
            return vaccine_name;
        }

        public String getNumber() {
            return times;
        }

        public String getDescribe() {
            return describes;
        }

        public String getSort() {
            return sort;
        }

        public int   getState() {
            return state;
        }

        public String getTime() {
            return create_time;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.vaccine_name);
            dest.writeString(this.times);
            dest.writeString(this.describes);
            dest.writeString(this.sort);
            dest.writeInt(this.state);
            dest.writeString(this.create_time);
            dest.writeString(this.id);
        }

        public VaccineListEntity() {
        }

        protected VaccineListEntity(Parcel in) {
            this.vaccine_name = in.readString();
            this.times = in.readString();
            this.describes = in.readString();
            this.sort = in.readString();
            this.state = in.readInt();
            this.create_time = in.readString();
            this.id = in.readString();
        }

        public static final Parcelable.Creator<VaccineListEntity> CREATOR = new Parcelable.Creator<VaccineListEntity>() {
            public VaccineListEntity createFromParcel(Parcel source) {
                return new VaccineListEntity(source);
            }

            public VaccineListEntity[] newArray(int size) {
                return new VaccineListEntity[size];
            }
        };
    }
}
