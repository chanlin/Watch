package com.jajale.watch.entity;

/**
 * Created by llh on 16-4-11.
 */
public class DHBMessage {


    /**
     * msg_id : 3
     * title : 活动一活动一活动一活动一活动一活动一活动一活动一活动一活动一活动一活动一活动一活动一活动一活动一
     * content : 活动推送内容三
     * type : 1
     */

    private String msg_id;
    private String title;
    private String content;
    private int type;

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg_id() {
        return msg_id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getType() {
        return type;
    }
}
