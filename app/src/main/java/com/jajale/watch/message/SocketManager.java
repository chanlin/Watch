package com.jajale.watch.message;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

//import com.bjjajale.push.common.protocol.MsgProto;
import com.jajale.watch.entitydb.SocketEntity;
import com.jajale.watch.receiver.NetworkReceiver;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.L;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理Socket
 * Created by chunlongyuan on 12/9/15.
 */
public class SocketManager {

    private static long lastTime;
    private Context mContext;
    private static final String TAG = SocketManager.class.getSimpleName();
    private final NetworkReceiver networkReceiver;

    public SocketManager(Context context) {
        this.mContext = context;

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
                        checkSocket();
                        L.d(TAG + "网络连接   去检查socket状态    " + name);
                    } else {
                        L.d(TAG + "网络断开   ");
                    }
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(networkReceiver, filter);
    }

    private void checkSocket() {

        long now = System.currentTimeMillis();
        boolean handle = (now - lastTime > 10 * 1000);
        lastTime = now;
        L.d(TAG + "网络连接   去检查socket状态    判断检查频率是否合格 handle: " + handle);
        if (!handle) {
            return;
        }
        L.d(TAG + "网络连接   去检查socket状态    开线程去检查");

        new Thread(new Runnable() {
            @Override
            public void run() {
                L.d(TAG + "网络连接   去检查socket状态    检查(socketRecoderManager != null) : " + (socketRecoderManager != null));
                if (socketRecoderManager != null) {
                    List<SocketEntity> records = socketRecoderManager.getRecords();
                    if (records != null && records.size() > 0) {

                        for (SocketEntity record : records) {
                            String mapKey = getMapKey(record);
                            if (socketMap.containsKey(mapKey)) {
                                SocketHolder socketHolder = socketMap.get(mapKey);
                                Socket socket = socketHolder.getSocket();
                                if (socket.isConnected() && !socket.isClosed()) {
                                    //没问题 跳过去
                                    L.d(TAG + "网络连接   去检查socket状态    没问题 跳过去");
                                    break;
                                }
                                //有问题 释放
                                try {
                                    L.d(TAG + "网络连接   去检查socket状态    有问题 释放");
                                    socket.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            L.d("网络连接   重连socket");
                            //这里就需要重连了
                            addConnect(record, null);

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }).start();


    }

    interface InitDelegate {
        void onFinish();
    }



    /**
     * 存所有Socket
     */
    public Map<String, SocketHolder> socketMap = new HashMap<String, SocketHolder>();

    public SocketRecordManager socketRecoderManager;


    public void init() {
        //

        if (socketRecoderManager == null) {
            socketRecoderManager = new SocketRecordManager(mContext);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<SocketEntity> socketEntities = socketRecoderManager.getRecords();
                if (socketEntities != null && socketEntities.size() > 0) {
                    for (SocketEntity socketEntity : socketEntities) {
                        if (!isSocketExit(socketEntity)) {
                            addConnect(socketEntity, null);
                            try {
                                Thread.sleep(2 * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }).start();
    }

    private void addConnect(SocketEntity socketEntity, InitDelegate delegate) {
        if (!socketMap.containsKey(getMapKey(socketEntity))) {
            connect(socketEntity, delegate);
        }
    }

    private void closeConnect(SocketEntity socketEntity) {
        String mapKey = getMapKey(socketEntity);
        if (socketMap.containsKey(mapKey)) {
            try {
                SocketHolder socketHolder = socketMap.get(mapKey);
//                if (socketHolder.needClose(socketEntity.getWatchID())) {
                Socket socket = socketHolder.getSocket();
                OutputStream outputStream = socket.getOutputStream();
                outputStream.close();
                socket.close();
                socketHolder.clear();
                socketMap.remove(mapKey);
//                }
                if (CMethod.isNet(mContext)) {
                    addConnect(socketEntity, null);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeConnect(SocketEntity socketEntity) {
        String mapKey = getMapKey(socketEntity);
        if (socketMap.containsKey(mapKey)) {
            try {
                SocketHolder socketHolder = socketMap.get(mapKey);
                if (socketHolder.needClose(socketEntity.getWatchID())) {
                    Socket socket = socketHolder.getSocket();
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.close();
                    socket.close();
                    socketMap.remove(mapKey);
                    socketRecoderManager.deleteRecoder(socketEntity);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void connect(final SocketEntity socketEntity, final InitDelegate delegate) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                L.d("---socket manager 连接socket  --> " + socketEntity.getIp() + socketEntity.getPort());
                try {
                    final Socket socket = new Socket(socketEntity.getIp(), socketEntity.getPort());
                    socket.setKeepAlive(true);
//                    socket.setSoTimeout(2 * 1000);
                    final InputStream inputStream = socket.getInputStream();
                    socketMap.put(getMapKey(socketEntity), new SocketHolder(socketEntity.getWatchID(), socket));
                    socketRecoderManager.saveRecoder(socketEntity);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                while (isConnected(socket)) {
                                    byte[] buffer = new byte[1024];
                                    int byteSize = inputStream.read(buffer);


//
                                    if (byteSize > 0) {
                                        L.e("socket===size===" + byteSize);

                                    } else {
                                        L.e("socket 挂了");
                                        closeConnect(socketEntity);
                                        break;
                                    }
                                }
                            } catch (IOException e) {
                                L.d("循环里 socket断了" + e.toString());
                                e.printStackTrace();
                                closeConnect(socketEntity);
                            }
                        }
                    }).start();


//                    socketEntity.setMessage(message);
                    sendCommand(socketEntity);

                    if (delegate != null) {
                        delegate.onFinish();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private boolean isConnected(Socket socket) {
        return socket != null && !socket.isClosed() && socket.isConnected();
    }

    public boolean isSocketExit(SocketEntity socketEntity) {
        return socketMap.containsKey(getMapKey(socketEntity));
    }

    public void sendCommand(SocketEntity socketEntity) throws IOException {

        final String mapKey = getMapKey(socketEntity);
//        final MsgProto.Message message = socketEntity.getMessage();
        InitDelegate initDelegate = new InitDelegate() {
            @Override
            public void onFinish() {
                try {
                    SocketHolder socketHolder = socketMap.get(mapKey);
                    Socket socket = socketHolder.getSocket();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        if (!isSocketExit(socketEntity)) {
            addConnect(socketEntity, initDelegate);
        } else {
            SocketHolder socketHolder = socketMap.get(getMapKey(socketEntity));
            socketHolder.add(socketEntity.getWatchID());
            initDelegate.onFinish();
        }

    }


    private String getMapKey(SocketEntity socketEntity) {
        return socketEntity.getIp() + ":" + socketEntity.getPort();
    }


//    private byte[] test(String uesrID) {
//
//        MsgProto.LoginRequest.Builder builder = MsgProto.LoginRequest.newBuilder();
//
//        builder.setSign("");
//        builder.setUserId(uesrID);
//        MsgProto.LoginRequest build = builder.build();
//
//        MsgProto.Message.Builder messageBuilder = MsgProto.Message.newBuilder();
//
//        MsgProto.Request.Builder requestBuilder = MsgProto.Request.newBuilder().setLoginRequest(build);
//
//        messageBuilder.setRequest(requestBuilder);
//        messageBuilder.setMsgType(MsgProto.MessageType.Login_Request);
//        messageBuilder.setSequence(2);
//
//
//        MsgProto.Message message = messageBuilder.build();
//
//
//        int serializedSize = message.getSerializedSize();
//
//        byte[] result = new byte[19 + serializedSize];
//
//        System.arraycopy(CMethod.int2Byte(1), 0, result, 0, 1);//协议头标志：1 类型：char,1byte
//        System.arraycopy(CMethod.int2Byte(1), 0, result, 1, 1);//协议版本：1 类型：char,1byte
//        System.arraycopy(CMethod.int2Byte(19), 0, result, 2, 1);//头部长度：19 类型：char,1byte
//        System.arraycopy(CMethod.int2ByteArray(serializedSize), 0, result, 3, 4);//数据长度：程序计算,4byte
//        System.arraycopy(CMethod.int2ByteArray(1), 0, result, 7, 4);//序列号：程序自增计算,4byte
//        System.arraycopy(CMethod.int2ByteArray(0), 0, result, 11, 4);//保留字段：填0,4byte
//        System.arraycopy(CMethod.int2ByteArray(0), 0, result, 15, 4);//保留字段：填0,4byte
//        System.arraycopy(message.toByteArray(), 0, result, 19, serializedSize);
//
//        return result;
//    }

//    public static int readInteger2(byte[] srctemp2, int num) throws IOException {
//        byte[] temp1 = new byte[1];
//        System.arraycopy(srctemp2, 0, temp1, 0, 1);
//        byte[] temp2 = new byte[1];
//        System.arraycopy(srctemp2, 1, temp2, 0, 1);
//        byte[] temp3 = new byte[1];
//        System.arraycopy(srctemp2, 2, temp3, 0, 1);
//
//        byte[] temp4 = new byte[4];
//        System.arraycopy(srctemp2, 3, temp4, 0, 4);
//        byte[] temp5 = new byte[4];
//        System.arraycopy(srctemp2, 7, temp5, 0, 4);
//        byte[] temp6 = new byte[4];
//        System.arraycopy(srctemp2, 11, temp6, 0, 4);
//        byte[] temp7 = new byte[4];
//        System.arraycopy(srctemp2, 15, temp7, 0, 4);
//        int dataLength = byteArray2Int(temp4);
//
//        byte[] temp8 = new byte[dataLength];
//        L.e("socket====dataLength==" + dataLength);
//        System.arraycopy(srctemp2, 19, temp8, 0, dataLength);
//
//
//        MsgProto.Message.Builder mergeFrom = MsgProto.Message.getDefaultInstance().newBuilderForType().mergeFrom(temp8);
//        MsgProto.Message build = mergeFrom.build();
//        MsgProto.MessageType msgType = build.getMsgType();
//        L.e("socket====msgType==" + msgType.name());
//
//
////        MsgProto.Message.Builder builder = MsgProto.Message.newBuilder().mergeFrom(temp8);
////        MsgProto.Message message = builder.build();
////        MsgProto.Message message = MsgProto.Message.parseFrom(temp8);
//
//        System.out.println("111");
//
//        return 3;
//    }


}
