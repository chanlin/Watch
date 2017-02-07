package com.jajale.watch.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.jajale.watch.AppConstants;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.listener.SendMessageListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lilonghui on 2015/12/6.
 * Email:lilonghui@bjjajale.com
 */
public class SendMessageCodeUtils {


    public static String public_key = "bjjajale";
    private final int MAX_TIMER_INDEX = 60;
    private int secondsInt = MAX_TIMER_INDEX;
    private SendMessageListener sendMessageListener;

    public static String SEND_TYPE_RESETPSSSWORD = "2";//重置密码type
    public static String SEND_TYPE_REGISTER = "1";//注册type
    private TimerTask task;


    public SendMessageCodeUtils(SendMessageListener sendMessageListener) {


        this.sendMessageListener = sendMessageListener;
    }


    public void send() {
        setCountDown(MAX_TIMER_INDEX);
    }


    public void send(final Context context, final String phone, final String type) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("key", getKey(phone, type));
            jsonObject.put("phone_num", phone);
            jsonObject.put("create_time", getTime());
            jsonObject.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API,AppConstants.JAVA_VALIDATE_KEY_URL, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                L.e("resultonsuccess");
                setCountDown(MAX_TIMER_INDEX);
            }

            @Override
            public void onFailure(String result) {
                L.e("resultonfailure");
                sendMessageListener.onError(result);
            }

            @Override
            public void onError() {
                L.e("resultonerror");
            }
        });

//
//
//
//
//        if (CMethod.isNet(context)){
//            try {
//                JSONObject paramJO = new JSONObject();
//                paramJO.put("code", CodeConstants.SEND_MESSAGE_CODE);
//                JSONObject dataObj = new JSONObject();
//                dataObj.put("key", getKey(phone, type));
//                dataObj.put("phone", phone);
//                dataObj.put("time", getTime());
//                dataObj.put("type", type);
//                paramJO.put("data", dataObj);
//                RequestManager.getInstance().addRequest(new SimpleStringRequest(context, AppConstants.POST_URL, paramJO, new SimpleResponseListener() {
//                    @Override
//                    public void onSuccess(String url, String result) {
//                        L.e("SendMessageCodeUtils_onSuccess");
//
//                        setCountDown(MAX_TIMER_INDEX);
//
//                    }
//
//                    @Override
//                    public void onFailure(String url, SimpleResult response) {
//                        L.e("SendMessageCodeUtils_onFailure" + response.getMessage());
//                        sendMessageListener.onError(response.getMessage());
//                    }
//
//                    @Override
//                    public void onError(String url, SimpleResult result) {
//                        L.e("SendMessageCodeUtils_onError" + "error result ");
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        L.e("onErrorResponse" + "error onErrorResponse ");
//                    }
//                }), this);
//            } catch (Exception e) {
//            }
//        }else {
//            ((Activity)context).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    T.s("网络不给力");
//                    sendMessageListener.onError("网络不给力");
//                }
//            });
//        }

//        setCountDown(MAX_TIMER_INDEX);

    }


    private static String getKey(String phone, String type) {
//        调用短信发送接口验证标识 key组成方式  phone + time + bjjajale + type  进行32位MD5 加密
        return Md5Util.string2MD5(phone + getTime() + public_key + type);
    }

    private static String getTime() {

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    //计时器
    private void setCountDown(int index) {
        Timer time = new Timer();
        secondsInt = index;
        task = new TimerTask() {
            @Override
            public void run() {
                secondsInt--;
                mHandler.sendEmptyMessage(secondsInt);
            }
        };
        time.schedule(task, 0, 1000);


    }

    //发送完验证码读秒
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what > 0) {
                sendMessageListener.onReadSecond(msg.what);
            } else {
                sendMessageListener.onStop();
                task.cancel();
            }
        }
    };
}
