package com.jajale.watch.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.bjjajale.push.common.protocol.MsgProtobuf;
import com.jajale.watch.AppConstants;
import com.jajale.watch.BroadcastConstants;
import com.jajale.watch.IntentAction;
import com.jajale.watch.SocketConstants;
import com.jajale.watch.entity.RequestEntity;
import com.jajale.watch.entity.SPKeyConstants;
import com.jajale.watch.factory.ProBufBeanFactory;
import com.jajale.watch.receiver.NetworkReceiver;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LastLoginUtils;
import com.jajale.watch.utils.PhoneSPUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;

/**
 * Created by llh on 16-3-10.
 */
public class MySocketManager {
    private static final String TAG = "socket===";
    private final Context mContext;
    private final NetworkReceiver networkReceiver;
    private static long lastTime;
    private static Socket socket;
    private PhoneSPUtils phoneSPUtils;

    /**
     * 存所有Socket
     */

    interface InitDelegate {
        void onFinish();
    }


    public MySocketManager(Context context) {
        this.mContext = context;
        this.phoneSPUtils=new PhoneSPUtils(context);
        networkReceiver = new NetworkReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);
                L.d(TAG + "网络变化");
                if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo info = connectivityManager.getActiveNetworkInfo();
                    if (info != null && info.isAvailable()) {
                        String name = info.getTypeName();
                        L.d(TAG + "网络连接   去检查socket状态    " + name);
                        loginSocket();
                    } else {
                        L.d(TAG + "网络断开   ");
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(networkReceiver, filter);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastConstants.ACTION);
        mContext.registerReceiver(updateReceiver, intentFilter);

        IntentFilter intentFilter1 = new IntentFilter();
        intentFilter1.addAction(BroadcastConstants.SOCKET_TRUE_OR_FLASE_RECEIVER);
        mContext.registerReceiver(updateReceiver, intentFilter1);

        sp = mContext.getSharedPreferences("NotDisturb", mContext.MODE_PRIVATE);// 创建对象
        editor = sp.edit();
        name = sp.getString("Telephone_Number", "");
        L.e("socket12"+name);
    }


    //失敗後重新登錄
    private void loginSocket() {
//        long now = System.currentTimeMillis();
//        boolean handle = (now - lastTime > 10 * 1000);
//        lastTime = now;
//        L.d(TAG + "网络连接   去检查socket状态    判断检查频率是否合格 handle: " + handle);
//        if (!handle) {
//            return;
//        }
        closeConnect();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                L.d(TAG + "重新登录");
                LastLoginUtils lastLoginUtils = new LastLoginUtils(mContext);

                MySocketUtils mySocketUtils = new MySocketUtils(mContext);
                mySocketUtils.userSocketLogin(lastLoginUtils);
            }
        }).start();


    }

//
//    /**
//     * 检查socket连接状态
//     */
//    private void checkSocketState() {
//        long now = System.currentTimeMillis();
//        boolean handle = (now - lastTime > 10 * 1000);
//        lastTime = now;
//        L.d(TAG + "网络连接   去检查socket状态    判断检查频率是否合格 handle: " + handle);
//        if (!handle) {
//            return;
//        }
//        L.d(TAG + "网络连接   去检查socket状态    开线程去检查");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                L.d(TAG + "去检查socket状态22222222");
//                    if (socket.isConnected() && !socket.isClosed()) {
//                        L.d(TAG + "网络连接   去检查socket状态    没问题 跳过去");
//                    } else {
//                        //有问题 释放
//                        try {
//                            L.d(TAG + "网络连接   去检查socket状态    有问题 释放");
//                            socket.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                L.d("网络连接   重连socket");
//                //这里就需要重连了
//                loginSocket();
//            }
//        }).start();
//
//    }

//    /**
//     * 初始化socket
//     */
//    public void initSocket() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                LastLoginUtils lastLoginUtils = new LastLoginUtils(mContext);
//                connectSocket(lastLoginUtils.getUserId(), null);
//                try {
//                    Thread.sleep(2 * 1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
//    }

    /**
     * 关闭socket
     */
    private void closeConnect() {
        try {
            if (isSocketConnected(socket)) {
                OutputStream outputStream = socket.getOutputStream();
                outputStream.flush();
                outputStream.close();
                socket.close();
                socket = null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String name;
    boolean flag = true;

    private BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {//发送语音后接收的广播
            String action = intent.getAction();
            String str = intent.getStringExtra("data");
            if (action.equals(BroadcastConstants.ACTION)) {
                try {
                    L.e("str==="+str);
//                    L.e("socket1 ==="+socket.getLocalAddress() + socket.getLocalPort());
                    Writer writer = new OutputStreamWriter(socket.getOutputStream());
//			       	 writer.write("0045{\"id\":\"13112345679\",\"cmd\":\"tk\",\"imei\":\"358688000000158\",\"info\":\"123\"}");
                    L.e("writer==" + writer.toString());
                    writer.write(str);
                    writer.flush();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (action.equals(BroadcastConstants.SOCKET_TRUE_OR_FLASE_RECEIVER)){
                L.e("socket1ss");
//                flag = false;
                name = intent.getStringExtra("user_id");

            }
        }
    };

    /**
     * 链接socket
     */
    private void connectSocket(final String user_id, final InitDelegate initDelegate) {
        L.e("socket===链接");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    closeConnect();
                    String sp_ip=phoneSPUtils.getString(SPKeyConstants.SP_SOCKET_IP);
                    sp_ip=CMethod.isEmpty(sp_ip)? AppConstants.SOCKET_IP:sp_ip;
                    socket = new Socket(sp_ip, AppConstants.SOCKET_PORT);
                    L.e("socket==SOCKET_IP==" + sp_ip);
                    L.e("socket==SOCKET_PORT==" + AppConstants.SOCKET_PORT);
                    socket.setKeepAlive(true);
                    socket.setSoTimeout(0);
                    final InputStream inputStream = socket.getInputStream();
                    //添加发送心跳
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                            while (true){
//                                name = sp.getString("Telephone_Number", "");
                                L.e("socket1=="+name);
                                Writer writer = new OutputStreamWriter(socket.getOutputStream());
                                writer.write("0021{\"id\":\"" + name + "\",\"cmd\":\"ping\"}");//0021{"id":"13112345679","cmd":"ping"}
//			       	 writer.write("0045{\"id\":\"13112345679\",\"cmd\":\"tk\",\"imei\":\"358688000000158\",\"info\":\"123\"}");
                                writer.flush();
                                Thread.sleep(10*1000);
                            }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();

                    //System.err.println(""+isSocketConnected(socket));
                    L.e("socket",""+isSocketConnected(socket));

                    Reader reader = new InputStreamReader(socket.getInputStream());
                    char chars[] = new char[4];
                    int len;
                    StringBuilder sb = new StringBuilder();
                    String temp;
                    int head_len=0;
                    int rec_len=0;
                    char rev[] = new char[1];
                    //0028{"id":"13006616705","cmd":"TK","info":1}

                    while(isSocketConnected(socket)){
                        len=0;
                        head_len=0;
                        rec_len=0;
                        sb.delete(0, sb.length());
                        if((len=reader.read(chars))>0){
                            temp = new String(chars, 0, len);

                            sb.append(temp);

                            head_len=Integer.valueOf(sb.toString(),16);
                            //System.out.println(head_len);
                            L.e("socketlen",head_len+"");
                        }

                        while((len=reader.read(rev))>0){
                            rec_len +=len;
                            temp = new String(rev, 0, len);

                            sb.append(temp);

                            if(rec_len == head_len){
                                break;
                            }
                        }
                        //System.out.println(sb.toString());
                        L.e("socketsb", sb.toString());
//                        0028{"id":"13112345679","cmd":"TK","info":1}手表接收到的语音后服务器返回的数据
                        if (sb.toString().contains("\"cmd\":\"TK\",\"info\":1")){
                            L.e("socketsb2", sb.toString().contains("\"cmd\":\"TK\",\"info\":1")+"");
                            Intent intent = new Intent();
                            intent.setAction(BroadcastConstants.ACTION_MESSAGE_RECEIVE);
                            intent.putExtra(IntentAction.KEY_MESSAGE_TYPE, "2");
                            intent.putExtra(IntentAction.KEY_MESSAGE_WATCHID, user_id);
                            mContext.sendBroadcast(intent);
                        }
                        //0044{"id":"18059949795","cmd":"tk","imei":"358688000000158","info":"offline"}手表发送语音后服务器返回的数据
                        if (sb.toString().contains("\"cmd\":\"tk\"") && sb.toString().contains("\"info\":\"offline\"")) {
                            Intent intent = new Intent();
                            intent.setAction(BroadcastConstants.SEND_VOICE_SUCCESS);
                            mContext.sendBroadcast(intent);
                        }
                    }

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while (isSocketConnected(socket)) {

                                    byte[] buffer = new byte[4];
                                    int byteSize = inputStream.read(buffer);
                                    L.e("socket===byteSize==" + byteSize);
                                    if (byteSize > 0) {
                                        int proBufType = ProBufBeanFactory.getInstance().getProBufTypeFromBytes(buffer);
                                        L.e("接收的socket===类型==" + proBufType);
                                        switch (proBufType) {
                                            case SocketConstants.LOGIN_RESPONSE_TYPE://1-登录响应
                                                MsgProtobuf.LoginResponse loginResponse = ProBufBeanFactory.getInstance().getLoginResponseFromBytes(buffer);
                                                String loginResponseData = loginResponse.getData();
                                                int status = loginResponse.getStatus();
                                                L.e("接收的socket===登录响应数据===" + loginResponseData);

                                                if ("密码错误".equals(loginResponseData)){
                                                    Intent intent = new Intent();
                                                    intent.setAction(BroadcastConstants.ACTION_MESSAGE_RECEIVE);
                                                    intent.putExtra(IntentAction.KEY_MESSAGE_TYPE, "3");
                                                    mContext.sendBroadcast(intent);
                                                }
                                                if (status == 0) {//成功
                                                    Intent intent = new Intent();
                                                    intent.setAction(BroadcastConstants.ACTION_MESSAGE_RECEIVE);
                                                    intent.putExtra(IntentAction.KEY_MESSAGE_TYPE, "2");
                                                    intent.putExtra(IntentAction.KEY_MESSAGE_WATCHID, user_id);
                                                    mContext.sendBroadcast(intent);
                                                } else {//失败
//                                                    loginSocket();
                                                }

                                                break;
                                            case SocketConstants.NOTIFICATION_REQUEST_TYPE://2-通知请求
                                                MsgProtobuf.NotificationRequest notificationRequest = ProBufBeanFactory.getInstance().getNotificationRequestFromBytes(buffer);
                                                String notificationRequestData = notificationRequest.getData();
                                                int sequence = notificationRequest.getSequence();
                                                int version = notificationRequest.getVersion();
                                                String type = notificationRequest.getType();
                                                L.e("接收的socket===通知请求数据===notificationRequestData==" + notificationRequestData);
                                                L.e("接收的socket===通知请求数据===sequence==" + sequence);
                                                L.e("接收的socket===通知请求数据===version==" + version);
                                                L.e("接收的socket===通知请求数据===type==" + type);

                                                MsgProtobuf.NotificationResponse n = ProBufBeanFactory.getInstance().getNotificationResponseFromRequset(notificationRequest);
                                                RequestEntity entity = ProBufBeanFactory.getInstance().getRequserEntity(user_id, SocketConstants.NOTIFICATION_RESPONSE_TYPE, n);
                                                sendSocketRequest(entity);
                                                Intent intent = new Intent();
                                                intent.setAction(BroadcastConstants.ACTION_MESSAGE_RECEIVE);
                                                intent.putExtra(IntentAction.KEY_MESSAGE_TYPE, type);
                                                intent.putExtra(IntentAction.KEY_MESSAGE_WATCHID, user_id);
                                                mContext.sendBroadcast(intent);

                                                break;
                                            case SocketConstants.NOTIFICATION_RESPONSE_TYPE://3-通知响应
                                                MsgProtobuf.NotificationResponse notificationResponse = ProBufBeanFactory.getInstance().getNotificationResponseFromBytes(buffer);
                                                String notificationResponseData = notificationResponse.getData();
                                                L.e("接收的socket===通知响应数据===" + notificationResponseData);
                                                break;
                                            case SocketConstants.LINK_REQUEST_TYPE://4-link请求
                                                MsgProtobuf.LinkResponse l = ProBufBeanFactory.getInstance().getLinkResponse();
                                                RequestEntity link_entity = ProBufBeanFactory.getInstance().getRequserEntity(user_id, SocketConstants.LINK_RESPONSE_TYPE, l);
                                                sendSocketRequest(link_entity);
                                                L.e("接收的socket===link请求");
                                                break;
                                            case SocketConstants.LINK_RESPONSE_TYPE://5-link响应
                                                L.e("接收的socket===link响应");
                                                break;
                                        }

                                    } else {
//                                        loginSocket();
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                L.d("循环里 socket==断了" + e.toString());

//                                loginSocket();

                            }
                        }
                    }).start();
                    if (initDelegate != null) {
                        initDelegate.onFinish();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * socket是否连接
     *
     * @param socket
     * @return
     */
    private boolean isSocketConnected(Socket socket) {
        return socket != null && !socket.isClosed() && socket.isConnected();
    }


    /**
     * 发送socket命令
     */
    public void sendSocketRequest(final RequestEntity requestEntity) {
        ProBufBeanFactory.sequence++;
        InitDelegate initDelegate = new InitDelegate() {
            @Override
            public void onFinish() {
                try {

                    byte[] socketByte = ProBufBeanFactory.getInstance().getProBufByteArrayFromMessage(requestEntity.getType(), requestEntity.getMessage());

                    if (socket.isConnected()) {
                        OutputStream outputStream = socket.getOutputStream();
                        outputStream.write(socketByte);
                        outputStream.flush();
                    }
//

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        if (!isSocketConnected(socket)) {
            connectSocket(requestEntity.getUserId(), initDelegate);
        } else {
            initDelegate.onFinish();
        }

    }

    public Socket getSocket() {
        return socket;
    }

}
