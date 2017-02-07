package com.jajale.watch.entity;

import java.util.List;

/**
 * Created by lilonghui on 2015/12/10.
 * Email:lilonghui@bjjajale.com
 */
public class SOSData {


    /**
     * phone : 13641279240
     * nickName : 爸爸
     * number : 1
     * setState : 1
     */

    private List<SosListEntity> sosList;

    public void setSosList(List<SosListEntity> sosList) {
        this.sosList = sosList;
    }

    public List<SosListEntity> getSosList() {
        return sosList;
    }

    public static class SosListEntity {
        private String phone;
        private String number;

        public void setPhone(String phone) {
            this.phone = phone;
        }


        public void setNumber(String number) {
            this.number = number;
        }


        public String getPhone() {
            return phone;
        }


        public String getNumber() {
            return number;
        }

    }
}
