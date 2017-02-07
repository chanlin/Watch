package com.jajale.watch.listener;

/**
 * Created by athena on 2015/11/20.
 * Email: lizhiqiang@bjjajale.com
 */
public interface VersionUpdateListener {
    void showToast(String message);
    void downLoad(String url);
    void next();

}
