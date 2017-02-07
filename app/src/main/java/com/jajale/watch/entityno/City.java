package com.jajale.watch.entityno;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by athena on 2015/12/5.
 * Email: lizhiqiang@bjjajale.com
 */
public class City {

    public String id;
    public String name;
    public List<Area> areaList = new ArrayList<Area>();

    public String getString() {
        return name;
    }

}
