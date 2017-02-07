package com.jajale.watch.listener;

/**
 * Created by athena on 2015/12/2.
 * Email: lizhiqiang@bjjajale.com
 */
public interface VoiceDownloadListener {
    void onSuccess(String path);
    void onFailure();
}
