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
@DatabaseTable(tableName = "GDLocation")
public class GDLocation {

    @DatabaseField(columnName = "red_id" , id=true)
    public String red_id;
    @DatabaseField(columnName = "lon")
    public String lon;//用户手机号
    @DatabaseField(columnName = "lat")
    public String lat;//用户密码
    @DatabaseField(columnName = "time")
    public String time;//用户最后一次登录时间
    @DatabaseField(columnName = "millions")
    public String millions;//用户最后一次登录时间

    @DatabaseField(columnName = "address")
    public String address;//用户最后一次登录时间


}
