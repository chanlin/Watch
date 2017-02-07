package com.jajale.watch.entity;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */
public class HYReturnMessageListData {
    /*
    {"ret":200,"data":{"code":0,
    "message":[{"file":"358688000000158_1474865674.amr"},
    {"file":"358688000000158_1474892198.amr"}],
    "info":""},"msg":""}
     */
    private int ret;
    private Data data;
    private String msg;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
class Data{
    private int code;
    private Message message;
    private String info;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
class Message{
    private List<File> message;

    public List<File> getMessage() {
        return message;
    }

    public void setMessage(List<File> message) {
        this.message = message;
    }
}
class HYFile{
    private String file;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
