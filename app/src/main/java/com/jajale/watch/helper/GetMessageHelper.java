package com.jajale.watch.helper;

import android.content.Context;

import com.jajale.watch.listener.GetMessageListener;

/**
 * Created by athena on 2015/11/25.
 * Email: lizhiqiang@bjjajale.com
 */
public class GetMessageHelper {
    private Context mContext;
    private GetMessageListener mListener;

    public GetMessageHelper(Context context, GetMessageListener mListener) {
        this.mContext = context;
        this.mListener = mListener;
    }

    public void getMessage() {
        //可能通过一个接口拿回数据
        mListener.onSuccess(null);

    }
}
