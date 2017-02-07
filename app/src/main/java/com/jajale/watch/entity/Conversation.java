package com.jajale.watch.entity;


import com.jajale.watch.entitydb.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by athena on 2015/11/28.
 * Email: lizhiqiang@bjjajale.com
 */
public class Conversation {
    private int msgCount = 0 ;
    private List<Message> messages;

    public Conversation(int msgCount, List<Message> messages) {
        this.msgCount = msgCount;
        this.messages = messages;
    }

    public Conversation() {
        this.messages = new ArrayList<Message>();
    }

    public int getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(int msgCount) {
        this.msgCount = msgCount;
    }

    public List<Message> getAllMessage() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Message getMessage(int index){
        return messages.get(index);
    }

}
