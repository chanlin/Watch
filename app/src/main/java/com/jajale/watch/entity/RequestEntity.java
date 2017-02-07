package com.jajale.watch.entity;

import com.google.protobuf.GeneratedMessage;

import java.io.Serializable;

/**
 * Created by llh on 16-3-31.
 *
 * proto buffer 登录请求实体类
 */
public class RequestEntity implements Serializable {

    private static final long serialVersionUID = -7060210544600464481L;

    private int sequence;//序列号
    private int type;//类型
    private String userId;//用户id
    private GeneratedMessage message;

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public GeneratedMessage getMessage() {
        return message;
    }

    public void setMessage(GeneratedMessage message) {
        this.message = message;
    }
}
