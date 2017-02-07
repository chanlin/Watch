package com.jajale.watch.entitydb;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 家庭成员
 *
 * Created by lilonghui on 2015/12/2.
 * Email:lilonghui@bjjajale.com
 */

@DatabaseTable(tableName = "FamilyMember")
public class FamilyMember {

    public static String ID="_id";
    public static String WATCHID = "watchID";

    @DatabaseField(id = true, columnName = "_id")
    public String id;
    @DatabaseField(columnName = "watchID")
    public String watchId;
    @DatabaseField(columnName = "user_id")
    public String user_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @DatabaseField(columnName = "phone")
    public String phone_num_binded;
    @DatabaseField(columnName = "relation")
    public String relation;
    @DatabaseField(columnName = "is_manage")
    public int is_manage;



//    {"familyList":[{"friend_id":0,"is_manage":"1","phone_num_binded":"12353555556","relation":"张三","user_id":14}]}



    public void setPhone(String phone) {
        this.phone_num_binded = phone;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public void setIsManage(int isManage) {
        this.is_manage = isManage;
    }



    public String getPhone() {
        return phone_num_binded;
    }

    public String getRelation() {
        return relation;
    }

    public int getIsManage() {
        return is_manage;
    }

    public String getWatchId() {
        return watchId;
    }

    public void setWatchId(String watchId) {
        this.watchId = watchId;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
