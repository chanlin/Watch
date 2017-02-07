package com.jajale.watch.entity;

import java.util.List;

/**
 * Created by llh on 16-5-24.
 */
public class DiscoverData {


    /**
     * mo_icon : http://7xosde.com2.z0.glb.qiniucdn.com/5d272cd7-5143-4b01-aa59-c90e81fd269c.jpg
     * mo_name : 安全
     * mo_sort : 4
     * mode_type : 4
     * req_url :
     */

    private List<ModeListEntity> modeList;

    public void setModeList(List<ModeListEntity> modeList) {
        this.modeList = modeList;
    }

    public List<ModeListEntity> getModeList() {
        return modeList;
    }

    public static class ModeListEntity {
        private String mo_icon;
        private String mo_name;
        private String mo_sort;
        private int mode_type;
        private String req_url;

        public void setMo_icon(String mo_icon) {
            this.mo_icon = mo_icon;
        }

        public void setMo_name(String mo_name) {
            this.mo_name = mo_name;
        }

        public void setMo_sort(String mo_sort) {
            this.mo_sort = mo_sort;
        }

        public void setMode_type(int mode_type) {
            this.mode_type = mode_type;
        }

        public void setReq_url(String req_url) {
            this.req_url = req_url;
        }

        public String getMo_icon() {
            return mo_icon;
        }

        public String getMo_name() {
            return mo_name;
        }

        public String getMo_sort() {
            return mo_sort;
        }

        public int getMode_type() {
            return mode_type;
        }

        public String getReq_url() {
            return req_url;
        }
    }
}
