package com.jajale.watch.listener;

/**
 * Created by athena on 2015/11/11.
 * Email: lizhiqiang@bjjajale.com
 */
public interface HttpClientListener {
    void onSuccess(String result);
    void onFailure(String result);
    void onError();

}
