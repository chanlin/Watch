package com.jajale.watch.entitydb;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 成长标准
 * Created by chunlongyuan on 12/7/15.
 */
@DatabaseTable(tableName = "StandardGrow")
public class StandardGrow {

    @DatabaseField(generatedId = true)
    public int _id;
    @DatabaseField(columnName = "month")
    public int month;
    @DatabaseField(columnName = "heightLower")//最低标准
    public double heightLower;
    @DatabaseField(columnName = "heightUper")//最高标准
    public double heightUper;
    @DatabaseField(columnName = "heightStander")//标准
    public double heightStander;
    @DatabaseField(columnName = "weightLower")//最低标准
    public double weightLower;
    @DatabaseField(columnName = "weightUper")//最高标准
    public double weightUper;
    @DatabaseField(columnName = "weightStander")//标准
    public double weightStander;
    @DatabaseField(columnName = "gender")//性别
    public int gender;
}
