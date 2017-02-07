package com.jajale.watch.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by athena on 2015/12/13.
 * Email: lizhiqiang@bjjajale.com
 */
public class CityEntity {
    private List<BaseEntity> areaList = new ArrayList<BaseEntity>();
    private int id ;
    private String name ;

    public List<BaseEntity> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<BaseEntity> areaList) {
        this.areaList = areaList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
