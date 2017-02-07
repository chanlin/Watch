package com.jajale.watch.listener;

import com.jajale.watch.entity.ResultEntity;

/**
 * Created by athena on 2015/11/20.
 * Email: lizhiqiang@bjjajale.com
 */
public interface LoginListener {
    void success(ResultEntity entity);
    void error(String message);
}
