package com.jajale.watch.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by athena on 2015/12/13.
 * Email: lizhiqiang@bjjajale.com
 */
public class ProvienceEntity {
    private List<CityEntity> cityList = new ArrayList<CityEntity>();
    private int id ;
    private String name ;


    public List<CityEntity> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityEntity> cityList) {
        this.cityList = cityList;
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
