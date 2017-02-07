package com.jajale.watch.entity;

import java.util.List;

/**
 * Created by llh on 16-4-29.
 */
public class DHBActivityData {


    /**
     * activity_list : [{"activity_id":"1","activity_name":"活动1","activity_place":"五路居","activity_img_url":"http://7xosde.com2.z0.glb.qiniucdn.com/dcb9b148-fabf-4ac2-b526-b6a2e635d37d.png","activity_start_time":"2015年04月28日"},{"activity_id":"2","activity_name":"活动2","activity_place":"五棵松","activity_img_url":"http://7xosde.com2.z0.glb.qiniucdn.com/dcb9b148-fabf-4ac2-b526-b6a2e635d37d.png","activity_start_time":"2015年04月28日"}]
     */

    private List<ActivityListEntity> activity_list;

    public void setActivity_list(List<ActivityListEntity> activity_list) {
        this.activity_list = activity_list;
    }

    public List<ActivityListEntity> getActivity_list() {
        return activity_list;
    }

    public static class ActivityListEntity {
        /**
         * activity_id : 1
         * activity_name : 活动1
         * activity_place : 五路居
         * activity_img_url : http://7xosde.com2.z0.glb.qiniucdn.com/dcb9b148-fabf-4ac2-b526-b6a2e635d37d.png
         * activity_start_time : 2015年04月28日
         */

        private String activity_id;
        private String activity_name;
        private String activity_place;
        private String activity_img_url;
        private String activity_start_time;

        public void setActivity_id(String activity_id) {
            this.activity_id = activity_id;
        }

        public void setActivity_name(String activity_name) {
            this.activity_name = activity_name;
        }

        public void setActivity_place(String activity_place) {
            this.activity_place = activity_place;
        }

        public void setActivity_img_url(String activity_img_url) {
            this.activity_img_url = activity_img_url;
        }

        public void setActivity_start_time(String activity_start_time) {
            this.activity_start_time = activity_start_time;
        }

        public String getActivity_id() {
            return activity_id;
        }

        public String getActivity_name() {
            return activity_name;
        }

        public String getActivity_place() {
            return activity_place;
        }

        public String getActivity_img_url() {
            return activity_img_url;
        }

        public String getActivity_start_time() {
            return activity_start_time;
        }
    }
}
