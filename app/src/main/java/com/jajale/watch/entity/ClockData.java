package com.jajale.watch.entity;

import com.jajale.watch.entitydb.Clock;

import java.util.List;

/**
 * 闹钟列表实体类
 *
 * Created by lilonghui on 2015/12/3.
 * Email:lilonghui@bjjajale.com
 */
public class ClockData {

    public static final String KEY = "ClockData_key";


    private List<Clock> clockList;

    public void setClockList(List<Clock> clockList) {
        this.clockList = clockList;
    }

    public List<Clock> getClockList() {
        return clockList;
    }


}
