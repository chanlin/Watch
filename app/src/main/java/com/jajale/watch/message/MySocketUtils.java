package com.jajale.watch.message;

import android.content.Context;
import android.content.Intent;

import com.bjjajale.push.common.protocol.MsgProtobuf;
import com.jajale.watch.BroadcastConstants;
import com.jajale.watch.IntentAction;
import com.jajale.watch.SocketConstants;
import com.jajale.watch.entity.RequestEntity;
import com.jajale.watch.factory.ProBufBeanFactory;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LastLoginUtils;

/**
 * Created by chunlongyuan on 12/10/15.
 */
public class MySocketUtils {

    private Context mContext;

    public MySocketUtils(Context context) {
        this.mContext = context;
    }

    /**
     * 维护service
     * 新启动了 SocketService 那么返回 false
     *
     * SocketService 已经启动过了 返回 true
     *
     */
    private boolean maintainService() {

        if (!isConnected()) {
            mContext.startService(new Intent(mContext, SocketService.class));
            return false;
        }
        return true;
    }

    private boolean isConnected() {
        return CMethod.isServiceRunning(mContext, SocketService.class.getCanonicalName());
    }






//    public void logout(SmartWatch watch) {
//        SocketEntity socketEntity = SocketEntity.getSocketEntityByWatch(watch);
//        Intent intent = new Intent();
//        intent.setAction(BroadcastConstants.ACTION_MESSAGE_LOGOUT);
//        intent.putExtra(IntentAction.KEY_SOCKET_ENTITY, socketEntity);
//        mContext.getApplicationContext().sendBroadcast(intent);
//    }

    public void userSocketLogin(final RequestEntity entity){
        if (maintainService()) {
            L.d("socket==message manager 立即登录");
            sendBroadCast2SocketSevice(entity);
        } else {
            L.d("socket==message manager 稍后登录");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    sendBroadCast2SocketSevice(entity);
                }
            }).start();
        }
    }

    public void userSocketLogin(LastLoginUtils lastLoginUtils){

//        String userId=lastLoginUtils.getUserId();

        String userId="1";
        String passWd="123";
        int type= SocketConstants.LOGIN_REQUEST_TYPE;

        MsgProtobuf.LoginRequest loginRequest= ProBufBeanFactory.getInstance().getLoginRequestFromDatas(userId, passWd, "");
        RequestEntity entity=ProBufBeanFactory.getInstance().getRequserEntity(userId, type, loginRequest);
        userSocketLogin(entity);

    }


    private void sendBroadCast2SocketSevice(RequestEntity entity){
        Intent intent = new Intent();
        intent.setAction(BroadcastConstants.ACTION_MESSAGE_SEND);
        intent.putExtra(IntentAction.KEY_SOCKET_ENTITY, entity);
        mContext.getApplicationContext().sendBroadcast(intent);

    }







}
