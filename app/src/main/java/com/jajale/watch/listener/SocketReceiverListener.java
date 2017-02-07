package com.jajale.watch.listener;

/**
 * Created by llh on 16-3-9.
 *
 * socket 传递给广播的回调，用于处理不同接收结果的返回
 *
 *
 */
public interface SocketReceiverListener {
    void onReceiveCode(String code);
}
