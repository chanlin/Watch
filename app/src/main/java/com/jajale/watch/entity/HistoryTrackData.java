package com.jajale.watch.entity;

import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.LatLng;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.L;

import java.util.List;

/**
 * Created by lilonghui on 2016/1/21.
 * Email:lilonghui@bjjajale.com
 */
public class HistoryTrackData {



    private List<TrajectoryListEntity> trajectoryList;

    public void setTrajectoryList(List<TrajectoryListEntity> trajectoryList) {
        this.trajectoryList = trajectoryList;
    }

    public List<TrajectoryListEntity> getTrajectoryList() {
        return trajectoryList;
    }

    public static class TrajectoryListEntity {
        private String gps_time;
        private String gps_lat;
        private String gps_lon;

        public LatLng getLatLng() {
            return converterLatLng(new LatLng(parseDouble(gps_lat), parseDouble(gps_lon)));
        }

        public void setSysTime(String sysTime) {
            this.gps_time = sysTime;
        }

        public void setLat(String lat) {
            this.gps_lat = lat;
        }

        public void setLon(String lon) {
            this.gps_lon = lon;
        }

        public String getSysTime() {
            return gps_time;
        }

        public String getLat() {
            return gps_lat;
        }

        public String getLon() {
            return gps_lon;
        }
    }
    /**
     * 将其他（GPS）坐标系转换成高德的坐标系
     *
     * @param latLng
     * @return
     */
    private static LatLng converterLatLng(LatLng latLng) {
        CoordinateConverter converter = new CoordinateConverter(BaseApplication.getContext());
        // CoordType.GPS 待转换坐标类型
        converter.from(CoordinateConverter.CoordType.GPS);
        //lLatLng待转换坐标    点
        L.e("原经纬度：" + "lat==" + latLng.latitude + ",lng==" + latLng.longitude);
        converter.coord(latLng);
        // 执行转换操作
        LatLng desLatLng = converter.convert();
        L.e("转换后经纬度：" + "lat==" + desLatLng.latitude + ",lng==" + desLatLng.longitude);
        return desLatLng;

    }
    private static double parseDouble(String string) {

        if (!CMethod.isEmpty(string)) {
            return Double.parseDouble(string);
        }
        return 0;
    }


}
