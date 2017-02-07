package com.jajale.watch.listener;

import java.util.List;

/**
 * 拉取list回调
 * Created by chunlongyuan on 12/1/15.
 */
public interface ListListener<T> {

    void onError(String message);

    void onSuccess(List<T> tList);
}
