package com.jajale.watch.message;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.jajale.watch.BroadcastConstants;
import com.jajale.watch.IntentAction;
import com.jajale.watch.entity.RequestEntity;
import com.jajale.watch.utils.L;

import java.io.IOException;

public class SocketService extends Service {

    private static final String TAG = SocketService.class.getSimpleName() + "  ";
    private MsgMessageThreadReceiver threadReceiver;
    private boolean receiverRegisted;
    private MySocketManager socketManager;

    public SocketService() {
        L.e("socket==MessageService init");
    }


    @Override
    public IBinder onBind(Intent intent) {
        checkConnect();
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //启动入口
        checkConnect();
        return START_REDELIVER_INTENT;
    }

    private void checkConnect() {
        if (socketManager == null) {
            socketManager = new MySocketManager(getApplicationContext());
        }
        if (!receiverRegisted) {
            initMessaeReceiver();
//            socketManager.initSocket();
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        receiverRegisted = false;
        unregisterReceiver(threadReceiver);
        L.e("socket==  Service  被迫终止  准备重启");
    }

    /**
     * 监听
     */
    private void initMessaeReceiver() {
        threadReceiver = new MsgMessageThreadReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    if (BroadcastConstants.ACTION_MESSAGE_SEND.equals(intent.getAction())) {
                        sendMessage(intent);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastConstants.ACTION_MESSAGE_SEND);
        registerReceiver(threadReceiver, filter);
        receiverRegisted = true;
    }

    private void sendMessage(Intent intent) throws IOException {
        if (intent != null) {
            if (intent.hasExtra(IntentAction.KEY_SOCKET_ENTITY)) {
                RequestEntity entity = (RequestEntity) intent.getSerializableExtra(IntentAction.KEY_SOCKET_ENTITY);
                if (entity != null) {
                    L.e("socket==service--发送");
                    socketManager.sendSocketRequest(entity);
                }
            }
        }
    }
}
