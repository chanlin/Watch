package com.jajale.watch.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jajale.watch.BroadcastConstants;
import com.jajale.watch.IntentAction;
import com.jajale.watch.listener.SocketReceiverListener;
import com.jajale.watch.utils.L;

/**
 * Created by llh on 16-3-9.
 *
 * 接受socket的广播，是动态加载的，分别处理不同接收的情况
 *
 *
 */
public class MySocketReceiver extends BroadcastReceiver {

    private SocketReceiverListener mListener;

    public MySocketReceiver(SocketReceiverListener listener){

        this.mListener=listener;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(BroadcastConstants.ACTION_MESSAGE_RECEIVE)) {
            String order = intent.getStringExtra(IntentAction.KEY_MESSAGE_TYPE);
            L.e("接收的socket==广播接收=通知请求数据==="+order);
            mListener.onReceiveCode(order);
        }
    }



}
