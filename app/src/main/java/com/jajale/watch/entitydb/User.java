package com.jajale.watch.entitydb;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by athena on 2015/11/11.
 * Email: lizhiqiang@bjjajale.com
 */

@DatabaseTable(tableName = "User")
public class User implements Serializable {


    @DatabaseField(id = true, columnName = "userID")
    public String userID;

    @DatabaseField(columnName = "nickname")
    public String nick_name;

    @DatabaseField(columnName = "watchBind")
    public String watchBind;

    @DatabaseField(columnName = "birthday")
    public String birthday;

    @DatabaseField(columnName = "gender")
    public String gender;

    @DatabaseField(columnName = "avatar")
    public String avatar;

    @DatabaseField(columnName = "longitude")
    public double longitude;
    @DatabaseField(columnName = "latitude")
    public double latitude;

}
