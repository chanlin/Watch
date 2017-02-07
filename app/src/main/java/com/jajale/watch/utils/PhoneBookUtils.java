package com.jajale.watch.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.jajale.watch.listener.PhoneBookListener;

/**
 * 电话簿工具
 * <p/>
 * Created by lilonghui on 2015/12/8.
 * Email:lilonghui@bjjajale.com
 */
public class PhoneBookUtils {


    private Context mContext;
    private PhoneBookListener mPhoneBookListener;
    private String mPhone;
    private String mRelation;
    private String mNumber;
    private boolean isfinish = false;

    private Gson gson = new Gson();


//    private final MySocketUtils socketUtils;


//    public PhoneBookUtils(Context context, Watch watch, String phone, String relation, PhoneBookListener phoneBookListener) {
//        this.mContext = context;
//        this.mPhoneBookListener = phoneBookListener;
//        this.watch = watch;
//        this.mPhone = phone;
//        this.mRelation = relation;
////        mSocketTools = new SocketTools(context, watch);
//        socketUtils = new MySocketUtils(context);
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(BroadcastConstants.ACTION_MESSAGE_RECEIVE);
//
//        try {
////            mContext.registerReceiver(receiver, intentFilter);
//        } catch (Exception e) {
//            mPhoneBookListener.onFinish();
//        }
//
//    }
//


    /**
     * 编辑电话本
     */
//    public void editPhoneBook() {
//
//        if (CMethod.isNet(mContext)) {
//            try {
//                JSONObject paramJO = new JSONObject();
//                paramJO.put("code", CodeConstants.GET_PHONE_BOOK_LIST_CODE);
//                JSONObject dataObj = new JSONObject();
//                dataObj.put("userID", BaseApplication.getUserInfo().userID);
//                dataObj.put("watchID", watch.getWatchID());
//                paramJO.put("data", dataObj);
//                SimpleStringRequest request = new SimpleStringRequest(mContext, AppConstants.POST_URL, paramJO, new SimpleResponseListener() {
//                    @Override
//                    public void onSuccess(String url, String result) {
//                        L.e("PhoneBookUtils__onSuccess==" + result);
//                        Gson gson = new Gson();
//                        PhoneBookData fromJson = gson.fromJson(result, PhoneBookData.class);
//                        List<PhoneBookData.PhoneListEntity> lists = fromJson.getPhoneList();
//                        //如果该电话簿含有自己的电话，则返回
//                        for (PhoneBookData.PhoneListEntity entity : lists) {
//                            if (entity.getNickName().equals("") && entity.getPhone().equals("") || entity.getPhone().equals(mPhone)) {
//
//                                setCountDownToDismissDialog();//定时关闭
//                                //发送手表指令
//                                entity.setPhone(mPhone);
//                                entity.setNickName(mRelation);
//                                mNumber = entity.getNumber();
//                                socketUtils.setPhoneBook(watch, lists);
//                                return;
//                            }
//                        }
//                        finish();
//                    }
//
//                    @Override
//                    public void onFailure(String url, SimpleResult response) {
//                        L.e("PhoneBookUtils__onFailure==" + response.getMessage());
//                        finish();
//                    }
//
//                    @Override
//                    public void onError(String url, SimpleResult result) {
//                        L.e("PhoneBookUtils__onError" + result.getMessage());
//                        finish();
//                    }
//
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        finish();
//                    }
//                });
//                RequestManager.getInstance().addRequest(request, null);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            ((Activity)mContext).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    T.s("网络不给力");
//                }
//            });
//        }
//
//
//    }

    /**
     * 删除
     */
//    public void removePhoneBook() {
//
//        if (CMethod.isNet(mContext)) {
//            try {
//                JSONObject paramJO = new JSONObject();
//                paramJO.put("code", CodeConstants.GET_PHONE_BOOK_LIST_CODE);
//                JSONObject dataObj = new JSONObject();
//                dataObj.put("userID", BaseApplication.getUserInfo().userID);
//                dataObj.put("watchID", watch.getWatchID());
//                paramJO.put("data", dataObj);
//                SimpleStringRequest request = new SimpleStringRequest(mContext, AppConstants.POST_URL, paramJO, new SimpleResponseListener() {
//                    @Override
//                    public void onSuccess(String url, String result) {
//                        L.e("PhoneBookUtils>>>==onSuccess==" + result);
//                        Gson gson = new Gson();
//                        PhoneBookData fromJson = gson.fromJson(result, PhoneBookData.class);
//                        List<PhoneBookData.PhoneListEntity> lists = fromJson.getPhoneList();
//                        //如果该电话簿含有自己的电话，则设置为空
//                        for (PhoneBookData.PhoneListEntity entity : lists) {
//                            if (entity.getPhone().equals(mPhone)) {
//                                setCountDownToDismissDialog();//定时关闭
//                                mPhone = "";
//                                mRelation = "";
//                                //发送手表指令
//                                entity.setPhone(mPhone);
//                                entity.setNickName(mRelation);
//                                mNumber = entity.getNumber();
//                                socketUtils.setPhoneBook(watch, lists);
//
//                                return;
//                            }
//                        }
//                        finish();
//                    }
//
//                    @Override
//                    public void onFailure(String url, SimpleResult response) {
//                        L.e("PhoneBookUtils>>>==onFailure==" + response.getMessage());
//                        finish();
//                    }
//
//                    @Override
//                    public void onError(String url, SimpleResult result) {
//                        L.e("PhoneBookUtils>>>==onError" + result.getMessage());
//                        finish();
//                    }
//
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        finish();
//                    }
//                });
//                RequestManager.getInstance().addRequest(request, null);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            ((Activity)mContext).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    T.s("网络不给力");
//                }
//            });
//        }
//
//    }

    /**
     * 上传网络
     */
//    private void setPhoneBookToFromNetWork(String phone, String nickName, String number) {
//
//        if (CMethod.isNet(mContext)) {
//            try {
//                JSONObject paramJO = new JSONObject();
//                paramJO.put("code", CodeConstants.SET_PHONE_BOOK_PHONE_CODE);
//                JSONObject dataObj = new JSONObject();
//
//                dataObj.put("userID", BaseApplication.getUserInfo().userID);
//                dataObj.put("watchID", watch.getWatchID());
//                dataObj.put("phone", phone);
//                dataObj.put("nickName", nickName);
//                dataObj.put("number", number);
//                paramJO.put("data", dataObj);
//                SimpleStringRequest request = new SimpleStringRequest(mContext, AppConstants.POST_URL, paramJO, new SimpleResponseListener() {
//                    @Override
//                    public void onSuccess(String url, String result) {
//                        L.e("setPhoneBookToFromNetWork>>>==onSuccess==" + result);
//                        finish();
//                        LastLoginUtils utils = new LastLoginUtils(mContext);
//                        utils.AddPhoneBook(watch.getWatchID());
//                    }
//
//                    @Override
//                    public void onFailure(String url, SimpleResult response) {
//                        L.e("setPhoneBookToFromNetWork>>>==onFailure==" + response.getMessage());
//                        finish();
//                    }
//
//                    @Override
//                    public void onError(String url, SimpleResult result) {
//                        L.e("setPhoneBookToFromNetWork>>>==onError==" + result.getMessage());
//                        finish();
//                    }
//
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        finish();
//                    }
//                });
//                RequestManager.getInstance().addRequest(request, null);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else {
//            ((Activity)mContext).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    T.s("网络不给力");
//                }
//            });
//        }
//
//
//    }
//
//    /**
//     * 设置计时关闭dialog
//     */
//    private void setCountDownToDismissDialog() {
//        final Timer timer = new Timer(); //设置定时器
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                finish();
//                timer.cancel();
//                this.cancel();
//            }
//        }, 5000); //设置50毫秒的时长
//    }



//    private void getSystemWarning(final String order){
//        MessageUtils.getSystemWarningMsg(BaseApplication.getUserInfo().userID, new SimpleResponseListener() {
//            @Override
//            public void onSuccess(String url, String result) {
//                L.e("getSystemWarning ---- onSuccess ");
//                GetWarningMsg response = gson.fromJson(result, GetWarningMsg.class);
//                if (response.getMsgList() != null && response.getMsgList().size() > 0) {
//                    for (int i = 0; i < response.getMsgList().size(); i++) {
//                        Message msg = MessageFactory.getInstance().createMsgByWarningResp(response.getMsgList().get(i));
//                        MessageUtils.saveMsg2DB(msg);
//                        ChildUtils.updateChildByMessage("000", msg);
//                        updateRelationByMsg(msg, order);
//                        if (!msg.content_type.equals(MessageContentType.SYS_BETTRATY+"")){
//                            MobclickAgent.onEvent(mContext, UMeventId.UMENG_SOS_MESSAGE_NUMBER);
//                        }
//                    }
//
//                }
//            }
//
//            @Override
//            public void onFailure(String url, SimpleResult response) {
//                L.e("getSystemWarning ---- onFailure ");
//            }
//
//            @Override
//            public void onError(String url, SimpleResult result) {
//                L.e("getSystemWarning ---- onError ");
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                L.e("getSystemWarning ---- onErrorResponse ");
//            }
//        },(Activity)mContext);
//    }


}
