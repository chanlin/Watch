package com.jajale.watch.listener;


/**
 * 获取验证码回调
 * Created by chunlongyuan on 12/1/15.
 */
public interface SendMessageListener {

    void onError(String message);

    void onReadSecond(int second);

    void onStop();

}
