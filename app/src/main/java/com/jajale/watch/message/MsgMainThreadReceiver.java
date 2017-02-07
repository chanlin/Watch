package com.jajale.watch.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 需要接收消息就注册该广播
 */
public class MsgMainThreadReceiver extends BroadcastReceiver {

    public MsgMainThreadReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //在需要的地方也注册改广播，处理后中断广播
//        L.d("main thread -- > "  + android.os.Process.myPid() + " --- " + intent.getStringExtra(IntentAction.KEY_MESSAGE_CONTENT));
//        L.e("main thread socket -- > " + android.os.Process.myPid() + " --- " + intent.getStringExtra(IntentAction.KEY_MESSAGE_CONTENT));
    }
}
