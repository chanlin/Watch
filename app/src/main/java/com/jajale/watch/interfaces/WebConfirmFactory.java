package com.jajale.watch.interfaces;

import android.webkit.JavascriptInterface;

/**
 * Created by athena on 2015/12/4.
 * Email: lizhiqiang@bjjajale.com
 */
public abstract class WebConfirmFactory  {

    @JavascriptInterface
    public abstract void onConfirm(String confirmInfo);

    @JavascriptInterface
    public abstract void onCancel(String cancelInfo);

    @JavascriptInterface
    public abstract void onClose(String closeInfo);

}
