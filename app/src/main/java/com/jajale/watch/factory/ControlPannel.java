package com.jajale.watch.factory;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.webkit.JavascriptInterface;

import com.jajale.watch.CodeConstants;
import com.jajale.watch.entity.ShareData;
import com.jajale.watch.utils.L;

/**
 * Created by athena on 2015/12/4.
 * Email: lizhiqiang@bjjajale.com
 */
//public class ControlPannel extends WebConfirmFactory{
    public class ControlPannel{
    private Activity mActivity;
    private Handler mHandler;

    public ControlPannel(Activity mActivity, Handler mHandler) {
        this.mActivity = mActivity;
        this.mHandler = mHandler;
    }


    @JavascriptInterface
    public void callScanner(){
        mHandler.sendEmptyMessage(CodeConstants.START_SCAN);
    }






    @JavascriptInterface
    public void callShare(String url,String title,String content,String  imgUrl,int  type ){
        Message message=new Message();
        Bundle bundle=new Bundle();

        ShareData shareData=new ShareData();
        shareData.setShare_type(type);
        shareData.setShare_image_url(imgUrl);
        shareData.setShare_description(content);
        shareData.setShare_title(title);
        shareData.setShare_url(url);


        message.what=CodeConstants.CALL_SHARE;
        bundle.putInt("share_type",type);
        bundle.putString("share_url",url);
        bundle.putString("share_title",title);
        bundle.putString("share_content",content);
        bundle.putString("share_imgUrl",imgUrl);
        bundle.putParcelable("share_data_key",shareData);
        message.setData(bundle);
        mHandler.sendMessage(message);
//        mHandler.sendEmptyMessage(CodeConstants.CALL_SHARE);
    }

    /**
     * 不剪裁图片
     * @param type
     */
    @JavascriptInterface
    public void callImageUnCrop(String type) {

        Message message=new Message();
        Bundle bundle=new Bundle();
        message.what=CodeConstants.CALL_IMAGE_UNCROP;
        bundle.putString("image_type",type);
        message.setData(bundle);
        mHandler.sendMessage(message);

    }

    @JavascriptInterface
    public void callImage(String type) {

        Message message=new Message();
        Bundle bundle=new Bundle();
        message.what=CodeConstants.CALL_IMAGE;
        bundle.putString("image_type",type);
        message.setData(bundle);
        mHandler.sendMessage(message);

    }
    @JavascriptInterface
    public void callToast(String Content,int type) {

        Message message=new Message();
        Bundle bundle=new Bundle();
        message.what=CodeConstants.CALL_TOAST;
        bundle.putInt("toast_type",type);
        bundle.putString("toast_string",Content);
        message.setData(bundle);
        mHandler.sendMessage(message);

    }

    /**
     * 证件回调
     * @param type  1 投保人 2 投保对象
     * @param
     */
    @JavascriptInterface
    public void callCredentials(String type){
        Message message=new Message();
        Bundle bundle=new Bundle();
        message.what=CodeConstants.CALL_CREDENTIALS;
        bundle.putString("credentials_type",type);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }

    @JavascriptInterface
    public void callCalendar(String Content,int type) {
        Message message=new Message();
        Bundle bundle=new Bundle();
        message.what=CodeConstants.CALL_CALENDAR;
        bundle.putInt("calendar_type",type);
        bundle.putString("calendar_string",Content);
        message.setData(bundle);
        mHandler.sendMessage(message);

    }

    @JavascriptInterface
    public void onCancel(String cancelInfo) {

    }
    @JavascriptInterface
    public void onClose(String closeInfo) {

    }

    @JavascriptInterface
    public void callTitle(String title,String url,int type) {
        Message message=new Message();
        Bundle bundle=new Bundle();
        message.what=CodeConstants.CALL_TITLE;
        bundle.putInt("title_type",type);
        bundle.putString("title_string", title);
        L.i("guokm",title);
        bundle.putString("title_url", url);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }

    @JavascriptInterface
    public void changeTab(int index) {
        Message message=new Message();
        Bundle bundle=new Bundle();
        message.what=CodeConstants.CHANGE_TAB;
        bundle.putInt("tab_index", index);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }

    @JavascriptInterface
    public void showLinkageCallback(String type) {
        Message message=new Message();
        Bundle bundle=new Bundle();
        message.what=CodeConstants.CALL_AREA;
        bundle.putString("linkage_type", type);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }

    @JavascriptInterface
    public void callPhone(String number) {
        Message message=new Message();
        Bundle bundle=new Bundle();
        message.what=CodeConstants.CALL_PHONE;
        bundle.putString("phone_number", number);
        message.setData(bundle);
        mHandler.sendMessage(message);
    }







}
