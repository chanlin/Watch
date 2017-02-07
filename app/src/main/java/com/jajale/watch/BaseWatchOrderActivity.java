package com.jajale.watch;

import android.content.IntentFilter;
import android.os.Bundle;

import com.jajale.watch.listener.SocketReceiverListener;
import com.jajale.watch.receiver.MySocketReceiver;


public class BaseWatchOrderActivity extends BaseActivity implements SocketReceiverListener {

    private MySocketReceiver mySocketReceiver;

    @Override
    protected void onStart() {
        super.onStart();

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mySocketReceiver = new MySocketReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastConstants.ACTION_MESSAGE_RECEIVE);
        registerReceiver(mySocketReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mySocketReceiver);
        super.onDestroy();
    }

    @Override
    public void onReceiveCode(String code) {

    }




}
