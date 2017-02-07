package com.jajale.watch.entity;

import java.util.List;

/**
 * Created by lilonghui on 2015/12/11.
 * Email:lilonghui@bjjajale.com
 */
public class SplashImageData {

    /**
     * imgList : [{"item_type":1,"imgData":[{"ad_img":"http://7xosde.com2.z0.glb.qiniucdn.com/b33fd6bc-87ae-4e14-85ed-0d1101293633.jpg","sort":1}]},{"item_type":4,"imgData":[{"ad_img":"http://7xosde.com2.z0.glb.qiniucdn.com/88c0951f-a399-42b8-bb24-ff790d887418.jpg","sort":3},{"ad_img":"http://7xosde.com2.z0.glb.qiniucdn.com/f269c492-c9a4-4b95-8fa2-1e9da937492b.jpg","sort":3},{"ad_img":"http://7xosde.com2.z0.glb.qiniucdn.com/4d10257c-5f38-4fb9-9a6f-c65188b19338.jpg","sort":5}]}]
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
         * item_type : 1
         * imgData : [{"ad_img":"http://7xosde.com2.z0.glb.qiniucdn.com/b33fd6bc-87ae-4e14-85ed-0d1101293633.jpg","sort":1}]
         */

        private int item_type;
        private List<ImgDataEntity> imgData;

        public void setItem_type(int item_type) {
            this.item_type = item_type;
        }

        public void setImgData(List<ImgDataEntity> imgData) {
            this.imgData = imgData;
        }

        public int getItem_type() {
            return item_type;
        }

        public List<ImgDataEntity> getImgData() {
            return imgData;
        }

        public static class ImgDataEntity {
            /**
             * ad_img : http://7xosde.com2.z0.glb.qiniucdn.com/b33fd6bc-87ae-4e14-85ed-0d1101293633.jpg
             * sort : 1
             */

            private String ad_img;
            private int sort;

            public void setAd_img(String ad_img) {
                this.ad_img = ad_img;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public String getAd_img() {
                return ad_img;
            }

            public int getSort() {
                return sort;
            }
        }
    }
}
