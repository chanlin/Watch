package com.jajale.watch.factory;

import com.amap.api.maps.model.LatLng;
import com.jajale.watch.R;
import com.jajale.watch.entity.CityListEntity;
import com.jajale.watch.entity.LocationUserInfoEntity;
import com.jajale.watch.entitydb.SmartWatch;
import com.jajale.watch.utils.CMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by athena on 2015/11/24.
 * Email: lizhiqiang@bjjajale.com
 */
public class LocationFactory {
    private static LocationFactory instance = null;

    public static LocationFactory getInstance() {
        if (instance == null) {
            instance = new LocationFactory();
        }
        return new LocationFactory();
    }

    public List<LocationUserInfoEntity> getLocationList(List<SmartWatch> watchs) {
        List<LocationUserInfoEntity> locationUserInfoEntityLists = new ArrayList<LocationUserInfoEntity>();
        LocationUserInfoEntity entity;
        if (watchs.size() != 0) {
            for (int i = 0; i < watchs.size(); i++) {
                String lat = watchs.get(i).getGps_lat();
                String lon = watchs.get(i).getGps_lon();
//                116.4236,40.044364

                double child_lat = CMethod.parseDouble(lat);
                double child_lng = CMethod.parseDouble(lon);
                entity = new LocationUserInfoEntity();
                entity.setLatLng(new LatLng(child_lat, child_lng));
                entity.setWatchID(watchs.get(i).getUser_id());
                entity.setNickName(watchs.get(i).getNick_name());
                entity.setPhone(watchs.get(i).getPhone_num_binded());
                entity.setSex(watchs.get(i).getSex());
                entity.setIsManager(watchs.get(i).getIs_manage());

                entity.setAddress(watchs.get(i).getAddress());
                entity.setBaseType(CMethod.parseInt(watchs.get(i).getData_type()));
                entity.setBaseTime(watchs.get(i).getCreate_time());


                entity.setElectricity(watchs.get(i).getElectricities());
                if (watchs.get(i).getSex() == 0) {
                    entity.setHeadView(R.mipmap.head_image_girl_location);
                    entity.setHeadView_press(R.mipmap.head_image_girl_press);
                } else {
                    entity.setHeadView(R.mipmap.head_image_boy_location);
                    entity.setHeadView_press(R.mipmap.head_image_boy_press);
                }
                locationUserInfoEntityLists.add(entity);

            }
        }
        return locationUserInfoEntityLists;
    }


    public List<LatLng> getLatLngList(List<LocationUserInfoEntity> watchs) {
        List<LatLng> latlngs = new ArrayList<LatLng>();
        if (CMethod.getLatAndLng().latitude != 0&&CMethod.getLatAndLng().longitude != 0) {
            latlngs.add(CMethod.getLatAndLng());
        }
        for (int i = 0; i < watchs.size(); i++) {
            if (watchs.get(i).getLatLng().latitude != 0&&watchs.get(i).getLatLng().longitude != 0) {
                latlngs.add(watchs.get(i).getLatLng());
            }
        }

        return latlngs;
    }


    public ArrayList<CityListEntity> getCityEntityList(List<SmartWatch> watchs) {
        ArrayList<CityListEntity> cityListEntities = new ArrayList<CityListEntity>();

        for (int i = 0; i < watchs.size(); i++) {
            String lat = watchs.get(i).getGps_lat();
            String lon = watchs.get(i).getGps_lon();

                double child_lat = CMethod.parseDouble(lat);
                double child_lng = CMethod.parseDouble(lon);
                CityListEntity cityListEntity = new CityListEntity();
                cityListEntity.setChildName(watchs.get(i).getNick_name());
                cityListEntity.setLatLng(new LatLng(child_lat, child_lng));
                cityListEntities.add(cityListEntity);

        }
        return cityListEntities;
    }

}
