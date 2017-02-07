package com.jajale.watch.entity;

/**
 * Created by athena on 2016/2/20.
 * Email: lizhiqiang@bjjajale.com
 */
public class ADListEntity {
    private String information_id ;
    private String ad_img ;
    private int item_type ;
    private String req_url ;
    private String sort ;

    public int getItem_type() {
        return item_type;
    }

    public void setItem_type(int item_type) {
        this.item_type = item_type;
    }

    public String getReq_url() {
        return req_url;
    }

    public void setReq_url(String req_url) {
        this.req_url = req_url;
    }

    public String getMsgID() {
        return information_id;
    }

    public void setMsgID(String msgID) {
        this.information_id = msgID;
    }

    public String getImgUrl() {
        return ad_img;
    }

    public void setImgUrl(String imgUrl) {
        this.ad_img = imgUrl;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
