package com.jajale.watch.entity;

import com.jajale.watch.entitydb.NotDisturb;

import java.util.List;

/**
 * Created by lilonghui on 2015/12/4.
 * Email:lilonghui@bjjajale.com
 */
public class NotDisturbData {

    public static final String KEY = "NotDisturbData_key";



    private List<NotDisturb> disturbList;

    public void setDisturbList(List<NotDisturb> disturbList) {
        this.disturbList = disturbList;
    }

    public List<NotDisturb> getDisturbList() {
        return disturbList;
    }


}
