package com.jajale.watch.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lilonghui on 2016/1/12.
 * Email:lilonghui@bjjajale.com
 */
public class SafeFenceData {

    /**
     * safe_id : 1
     * safe_title : 家
     * gps_lat : 22.552233759259
     * gps_lon : 114.01834289802
     * radius : 500
     * address :
     */

    private List<SafeListEntity> safeList;

    public void setSafeList(List<SafeListEntity> safeList) {
        this.safeList = safeList;
    }

    public List<SafeListEntity> getSafeList() {
        return safeList;
    }

    public static class SafeListEntity implements Serializable {
        private String safe_id;
        private String safe_title;
        private String gps_lat;
        private String gps_lon;
        private int radius;
        private String address;

        public void setSafe_id(String safe_id) {
            this.safe_id = safe_id;
        }

        public void setSafe_title(String safe_title) {
            this.safe_title = safe_title;
        }

        public void setGps_lat(String gps_lat) {
            this.gps_lat = gps_lat;
        }

        public void setGps_lon(String gps_lon) {
            this.gps_lon = gps_lon;
        }

        public void setRadius(int radius) {
            this.radius = radius;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getSafe_id() {
            return safe_id;
        }

        public String getSafe_title() {
            return safe_title;
        }

        public String getGps_lat() {
            return gps_lat;
        }

        public String getGps_lon() {
            return gps_lon;
        }

        public int getRadius() {
            return radius;
        }

        public String getAddress() {
            return address;
        }
    }
}
