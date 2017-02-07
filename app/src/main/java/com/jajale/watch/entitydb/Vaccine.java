package com.jajale.watch.entitydb;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * 存储疫苗的相关数据
 *
 *
 * Created by athena on 2015/11/30.
 * Email: lizhiqiang@bjjajale.com
 */
@DatabaseTable(tableName = "Vaccine")
public class Vaccine {
    @DatabaseField(columnName = "vacine_id" , id=true)
    public String vacine_id;

    @DatabaseField(columnName = "month")
    public String month;//接种的月份

    @DatabaseField(columnName = "vacine_type")
    public String vacine_type;//疫苗类型

    @DatabaseField(columnName = "vacine_desc")
    public String vacine_desc;//疫苗描述

    @DatabaseField(columnName = "vacine_status")
    public String vacine_status;//疫苗接种状态

    @DatabaseField(columnName = "child_id")
    public String child_id;//疫苗接种状态

    @DatabaseField(columnName = "extend_1")
    public String extend_1;//疫苗接种状态
    @DatabaseField(columnName = "extend_2")
    public String extend_2;//疫苗接种状态
    @DatabaseField(columnName = "extend_3")
    public String extend_3;//疫苗接种状态

    public String getVacine_id() {
        return vacine_id;
    }

    public void setVacine_id(String vacine_id) {
        this.vacine_id = vacine_id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getVacine_type() {
        return vacine_type;
    }

    public void setVacine_type(String vacine_type) {
        this.vacine_type = vacine_type;
    }

    public String getVacine_desc() {
        return vacine_desc;
    }

    public void setVacine_desc(String vacine_desc) {
        this.vacine_desc = vacine_desc;
    }

    public String getVacine_status() {
        return vacine_status;
    }

    public void setVacine_status(String vacine_status) {
        this.vacine_status = vacine_status;
    }

    public String getExtend_1() {
        return extend_1;
    }

    public void setExtend_1(String extend_1) {
        this.extend_1 = extend_1;
    }

    public String getExtend_2() {
        return extend_2;
    }

    public void setExtend_2(String extend_2) {
        this.extend_2 = extend_2;
    }

    public String getExtend_3() {
        return extend_3;
    }

    public void setExtend_3(String extend_3) {
        this.extend_3 = extend_3;
    }

    public String getChild_id() {
        return child_id;
    }

    public void setChild_id(String child_id) {
        this.child_id = child_id;
    }
}
