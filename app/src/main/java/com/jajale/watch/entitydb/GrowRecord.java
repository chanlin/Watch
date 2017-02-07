package com.jajale.watch.entitydb;

import java.io.Serializable;

/**
 * 成长记录
 * Created by chunlongyuan on 11/24/15.
 */
public class GrowRecord implements Serializable{

    public static final String FIELD_GROWTHID = "growthID";
    public String growth_id;
    public double height;
    public double weight;
    public String content;
    public String create_time;
    public int month;

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
