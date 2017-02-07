package com.jajale.watch.entity;

import java.util.List;

/**
 * Created by lilonghui on 2015/12/10.
 * Email:lilonghui@bjjajale.com
 */
public class PhoneBookData {


    /**
     * phone : 13641279240
     * nickName : 爸爸
     * number : 1
     * setState : 1
     */

    private List<PhoneListEntity> phoneBookList;

    public void setPhoneList(List<PhoneListEntity> phoneList) {
        this.phoneBookList = phoneList;
    }

    public List<PhoneListEntity> getPhoneList() {
        return phoneBookList;
    }

    public static class PhoneListEntity {
        private String phone;
        private String nickName;
        private String number;

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public void setNumber(int number) {
            this.number = number +"";
        }



        public String getPhone() {
            return phone;
        }

        public String getNickName() {
            return nickName;
        }

        public String getNumber() {
            return number;
        }

    }
}
