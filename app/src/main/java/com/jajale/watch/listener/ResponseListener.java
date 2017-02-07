package com.jajale.watch.listener;

import com.jajale.watch.entityno.BaseResult;

/**
 * Created by athena on 2015/11/11.
 * Email: lizhiqiang@bjjajale.com
 */
public interface ResponseListener {
    void onSuccess(String url, String result);
    void onFailure(String url, BaseResult response);
    void onError(String url, BaseResult result);

}
