package com.jajale.watch.entity;

import com.jajale.watch.entitydb.SmartWatch;

import java.util.List;

/**
 * Created by athena on 2015/12/6.
 * Email: lizhiqiang@bjjajale.com
 */
public class GrowPageEntity {
    private List<SmartWatch> watches;
    private int current_index = 0 ;

    public List<SmartWatch> getWatches() {
        return watches;
    }

    public void setWatches(List<SmartWatch> watches) {
        this.watches = watches;
    }

    public int getCurrent_index() {
        return current_index;
    }

    public void setCurrent_index(int current_index) {
        this.current_index = current_index;
    }
}
