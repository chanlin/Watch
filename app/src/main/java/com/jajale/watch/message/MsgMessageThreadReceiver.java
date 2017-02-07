package com.jajale.watch.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jajale.watch.utils.L;

public class MsgMessageThreadReceiver extends BroadcastReceiver {

    public MsgMessageThreadReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        L.d("111 " + intent.getAction());
        L.d("MsgMessageThreadReceiver thread -- > "  + android.os.Process.myPid());
        //在需要的地方也注册改广播，处理后中断广播
    }
}
