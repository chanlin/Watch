package com.jajale.watch.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 疫苗年龄实体类
 *
 * Created by lilonghui on 2015/11/30.
 * Email:lilonghui@bjjajale.com
 */
public class VaccineAgeData {
    public static final String KEY = "VaccineAgeData_key";

    /**
     * varccineID : 1
     * age : 出生时
     * sort : 1
     */



    private List<VaccineAgeListEntity> vaccineAgeList;

    public void setVaccineAgeList(List<VaccineAgeListEntity> vaccineAgeList) {
        this.vaccineAgeList = vaccineAgeList;
    }

    public List<VaccineAgeListEntity> getVaccineAgeList() {
        return vaccineAgeList;
    }

    public static class VaccineAgeListEntity implements Parcelable {
        private String id;
        private String age;
        private String sort;

        public void setVarccineID(String varccineID) {
            this.id = varccineID;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getVarccineID() {
            return id;
        }

        public String getAge() {
            return age;
        }

        public String getSort() {
            return sort;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.age);
            dest.writeString(this.sort);
        }

        public VaccineAgeListEntity() {
        }

        protected VaccineAgeListEntity(Parcel in) {
            this.id = in.readString();
            this.age = in.readString();
            this.sort = in.readString();
        }

        public static final Parcelable.Creator<VaccineAgeListEntity> CREATOR = new Parcelable.Creator<VaccineAgeListEntity>() {
            public VaccineAgeListEntity createFromParcel(Parcel source) {
                return new VaccineAgeListEntity(source);
            }

            public VaccineAgeListEntity[] newArray(int size) {
                return new VaccineAgeListEntity[size];
            }
        };
    }
}
