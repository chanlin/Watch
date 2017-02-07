package com.jajale.watch.listener;

import com.jajale.watch.entityno.SimpleResult;

/**
 * Created by athena on 2015/11/11.
 * Email: lizhiqiang@bjjajale.com
 */
public interface SimpleResponseListener {
    void onSuccess(String url, String result);
    void onFailure(String url, SimpleResult response);
    void onError(String url, SimpleResult result);

}
