package com.jajale.watch.entity;

import java.util.List;

/**
 * Created by llh on 16-4-13.
 */
public class AlbumData {


    /**
     * imgList : [{"imgUrl":"","nicName":"","createTime":"10-1012: 10"},{"imgUrl":"","nicName":"","createTime":"10-1012: 10"}]
     */

    private List<ImgListEntity> imgList;

    public void setImgList(List<ImgListEntity> imgList) {
        this.imgList = imgList;
    }

    public List<ImgListEntity> getImgList() {
        return imgList;
    }

    public static class ImgListEntity {
        /**
         * imgUrl :
         * nicName :
         * createTime : 10-1012: 10
         */

        private String imgUrl;
        private String nicName;
        private String createTime;

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public void setNicName(String nicName) {
            this.nicName = nicName;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public String getNicName() {
            return nicName;
        }

        public String getCreateTime() {
            return createTime;
        }
    }
}
