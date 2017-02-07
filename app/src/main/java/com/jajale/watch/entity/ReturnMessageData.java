package com.jajale.watch.entity;

/**
 * Created by lilonghui on 2016/3/2.
 * Email:lilonghui@bjjajale.com
 */
public class ReturnMessageData {

    /**
     * content : 你有没有没有
     * content_type : 1
     * create_time : 2016-03-02 14:7:44
     * msg_type : 1
     * msg_uuid : fc87c178-ad5f-4de4-9138-bec9ba95c65b
     * order_by : 21
     * receive_user : 14
     * send_user : 14
     * time_stamp : 1456898864606
     * times : 0
     * type : 2
     */

    private String content;
    private int content_type;
    private String create_time;
    private int msg_type;
    private String msg_uuid;
    private String order_by;
    private String receive_user;
    private String send_user;
    private long time_stamp;
    private long times;
    private int type;

    public void setContent(String content) {
        this.content = content;
    }

    public void setContent_type(int content_type) {
        this.content_type = content_type;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public void setMsg_uuid(String msg_uuid) {
        this.msg_uuid = msg_uuid;
    }

    public void setOrder_by(String order_by) {
        this.order_by = order_by;
    }

    public void setReceive_user(String receive_user) {
        this.receive_user = receive_user;
    }

    public void setSend_user(String send_user) {
        this.send_user = send_user;
    }

    public void setTime_stamp(long time_stamp) {
        this.time_stamp = time_stamp;
    }

    public void setTimes(Long times) {
        this.times = times;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public int getContent_type() {
        return content_type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public int getMsg_type() {
        return msg_type;
    }

    public String getMsg_uuid() {
        return msg_uuid;
    }

    public String getOrder_by() {
        return order_by;
    }

    public String getReceive_user() {
        return receive_user;
    }

    public String getSend_user() {
        return send_user;
    }

    public long getTime_stamp() {
        return time_stamp;
    }

    public Long getTimes() {
        return times;
    }

    public int getType() {
        return type;
    }
}
