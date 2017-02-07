package com.jajale.watch.entitydb;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 用于多账户登录的时候记录
 * <p>
 * Created by athena on 2015/11/11.
 * Email: lizhiqiang@bjjajale.com
 * 只用于记录登录过的用户、已经登录状态
 */
@DatabaseTable(tableName = "Account")
public class Account {

    @DatabaseField(columnName = "phone", id = true)
    public String phone;//用户手机号
    @DatabaseField(columnName = "password")
    public String password;//用户密码
    @DatabaseField(columnName = "loginTime")
    public String loginTime;//用户最后一次登录时间
    @DatabaseField(columnName = "updateTime")
    public String updateTime;//用户最后一次登录时间


    //扩展业务
    @DatabaseField(columnName = "exten_1")//手表用户保险结束时间
    public String exten_1;
    @DatabaseField(columnName = "exten_2")//手表用户保险结束时间
    public String exten_2;
    @DatabaseField(columnName = "exten_3")//手表用户保险结束时间
    public String exten_3;


}
