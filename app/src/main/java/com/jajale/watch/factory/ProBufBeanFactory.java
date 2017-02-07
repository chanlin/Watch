package com.jajale.watch.factory;

import com.bjjajale.push.common.protocol.MsgProtobuf;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.jajale.watch.SocketConstants;
import com.jajale.watch.entity.RequestEntity;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.L;

/**
 * Created by llh on 16-3-10.
 * 设置 socket proto buffer 信息
 */
public class ProBufBeanFactory {
    public static int sequence = 1;

    public static ProBufBeanFactory instance;


    public static ProBufBeanFactory getInstance() {
        if (instance == null) {
            instance = new ProBufBeanFactory();
        }
        return instance;
    }


    public RequestEntity getRequserEntity(String userId, int type, GeneratedMessage message){
        RequestEntity entity=new RequestEntity();
        L.e("socket===发送的==entity===sequence==" + sequence);
        entity.setSequence(sequence);
        entity.setUserId(userId);
        entity.setType(type);
        entity.setMessage(message);
        return entity;
    }

    /**
     * 获取Proto buffer  登录请求数据
     *
     * @return
     */
    public MsgProtobuf.LoginRequest getLoginRequestFromDatas(String  userId,String passWd,String imei) {

        MsgProtobuf.LoginRequest.Builder builder = MsgProtobuf.LoginRequest.newBuilder();

        builder.setUserId(userId);
        L.e("socket===发送的==登录===sequence==" + sequence);
        builder.setSequence(sequence);
        builder.setVersion(SocketConstants.SOCKET_VERSION);
        builder.setPasswd(passWd);
        builder.setImei(imei);
        MsgProtobuf.LoginRequest loginRequest = builder.build();
        return loginRequest;

    }


    public MsgProtobuf.NotificationResponse getNotificationResponseFromRequset(MsgProtobuf.NotificationRequest request){
        MsgProtobuf.NotificationResponse.Builder builder = MsgProtobuf.NotificationResponse.newBuilder();
        builder.setSequence(request.getSequence());
        builder.setVersion(SocketConstants.SOCKET_VERSION);
        builder.setStatus(3);
        return builder.build();
    }


    /**
     *得到请求数据Byte数组
     */
    public byte[] getProBufByteArrayFromMessage(int type,GeneratedMessage message){
        int serializedSize = message.getSerializedSize();
        byte[] result = new byte[12 + serializedSize];
        System.arraycopy(CMethod.int2ByteArray(1), 0, result, 0, 4);//协议版本
        System.arraycopy(CMethod.int2ByteArray(serializedSize), 0, result, 4, 4);//数据长度
        System.arraycopy(CMethod.int2ByteArray(type), 0, result, 8, 4);//消息类型
        System.arraycopy(message.toByteArray(), 0, result, 12, serializedSize);
        return result;
    }





    /**
     * 获取返回的消息类型
     * @param bytes
     * @return
     */
    public int getProBufTypeFromBytes(byte[] bytes) {
        byte[] proBufTypeOfByte = new byte[4];
        System.arraycopy(bytes, 8, proBufTypeOfByte, 0, 4);
        int proBufTypeOfInt = CMethod.byteArray2Int(proBufTypeOfByte);
        return proBufTypeOfInt;
    }






    /**
     * 获取probuf数据
     * @param bytes
     * @return
     */
    private byte[] getProBufFromByte(byte[] bytes){
        byte[] proBufLengthByte = new byte[4];
        System.arraycopy(bytes, 4, proBufLengthByte, 0, 4);
        int dataLength = CMethod.byteArray2Int(proBufLengthByte);
        byte[] proBufByte = new byte[dataLength];
        System.arraycopy(bytes, 12, proBufByte, 0, dataLength);
        return proBufByte;
    }




    /**
     * 获取登录响应数据
     * @param bytes
     * @return
     */
    public MsgProtobuf.LoginResponse getLoginResponseFromBytes(byte[] bytes){
        try {
            MsgProtobuf.LoginResponse loginResponse= MsgProtobuf.LoginResponse.parseFrom(getProBufFromByte(bytes));
            return  loginResponse;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return  null;
    }

    /**
     * 获取通知请求数据
     * @param bytes
     * @return
     */
    public MsgProtobuf.NotificationRequest getNotificationRequestFromBytes(byte[] bytes){
        try {
            MsgProtobuf.NotificationRequest notificationRequest= MsgProtobuf.NotificationRequest.parseFrom(getProBufFromByte(bytes));
            return  notificationRequest;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return  null;
    }

    /**
     * 获取通知响应数据
     * @param bytes
     * @return
     */
    public MsgProtobuf.NotificationResponse getNotificationResponseFromBytes(byte[] bytes){
        try {
            MsgProtobuf.NotificationResponse notificationResponse= MsgProtobuf.NotificationResponse.parseFrom(getProBufFromByte(bytes));
            return  notificationResponse;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return  null;
    }


    /**
     * 获取link请求数据
     * @return
     */
    public MsgProtobuf.LinkRequest getLinkRequest(){
        MsgProtobuf.LinkRequest.Builder builder = MsgProtobuf.LinkRequest.newBuilder();
        return  builder.build();
    }
    /**
     * 获取link响应数据
     * @return
     */
    public MsgProtobuf.LinkResponse getLinkResponse(){
        MsgProtobuf.LinkResponse.Builder builder = MsgProtobuf.LinkResponse.newBuilder();

        return  builder.build();
    }


}
