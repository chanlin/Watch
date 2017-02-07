package com.jajale.watch.entitydb;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * 跟使用用户 相关联的亲属列表
 *
 * Created by athena on 2015/11/26.
 * Email: lizhiqiang@bjjajale.com
 */

@DatabaseTable(tableName = "Relative")
public class Relative {
    //用户特征相关
    @DatabaseField(id = true, columnName = "user_name")//注册用户id
    public String user_name;
    @DatabaseField(columnName = "watch_id")//关注手表的id
    public String watch_id;
    @DatabaseField(columnName = "nick_name")//与手表用户的亲属关系描述
    public String nick_name;
    @DatabaseField(columnName = "user_gender")//用户性别 0是女性  1是男性
    public Integer user_gender;
    @DatabaseField(columnName = "user_age")//用户年龄
    public String user_age;
    @DatabaseField(columnName = "user_height")//用户身高
    public String user_height;
    @DatabaseField(columnName = "user_position")//用户当前位置
    public String user_position;
    @DatabaseField(columnName = "user_longtitude")//用户当前位置
    public String user_longtitude;
    @DatabaseField(columnName = "user_latitude")//用户当前位置
    public String user_latitude;
    @DatabaseField(columnName = "user_phone")//用户电话号码
    public String user_phone;
    @DatabaseField(columnName = "user_avatar")//的头像地址
    public String user_avatar;
    @DatabaseField(columnName = "user_avatar_status")//的头像地址
    public String user_avatar_status;






}
