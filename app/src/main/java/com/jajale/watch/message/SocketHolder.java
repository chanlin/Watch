package com.jajale.watch.message;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by athena on 2015/12/12.
 * Email: lizhiqiang@bjjajale.com
 */
public class SocketHolder {

    private Socket socket;
    private List<String> userIds = new ArrayList<String>();
//    private int count = 0;

    public SocketHolder(String userId, Socket socket){
        this.socket = socket;
        this.userIds.add(userId);
//        this.count = 1;
    }


//    public void setSocket(Socket socket){
//        this.socket = socket;
//        count++;
//    }


    public Socket getSocket() {
        return socket;
    }

    public boolean needClose(String watchID){
//        count--;
        userIds.remove(watchID);
        return userIds.size() < 1;
    }


    public void add(String watchID) {
        if (!userIds.contains(watchID)){
            userIds.add(watchID);
        }
    }

    public void clear() {
        userIds.clear();
    }
}
