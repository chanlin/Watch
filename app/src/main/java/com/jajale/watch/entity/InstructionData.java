package com.jajale.watch.entity;

/**
 * Created by llh on 16-6-3.
 */
public class InstructionData {

    /**
     * mo_name : 使用说明
     * req_url : http://192.168.1.110:8082/directionsController/index.do?imei=868867020165308
     */

    private String mo_name;
    private String req_url;

    public void setMo_name(String mo_name) {
        this.mo_name = mo_name;
    }

    public void setReq_url(String req_url) {
        this.req_url = req_url;
    }

    public String getMo_name() {
        return mo_name;
    }

    public String getReq_url() {
        return req_url;
    }
}
