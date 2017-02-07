package com.jajale.watch.factory;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.jajale.watch.AppConstants;
import com.jajale.watch.R;
import com.jajale.watch.entity.ShareData;
import com.jajale.watch.utils.AccessTokenKeeper;
import com.jajale.watch.utils.ShareUtil;
import com.jajale.watch.utils.T;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;


/**
 * Created by llh on 16-3-14.
 */
public class ShareFactory implements IWeiboHandler.Response {
    private int wxSdkVersion;
    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
    private IWXAPI api;
    private static ShareFactory instance = null;
    private Context mContext;
    public static final int SHARE_CLIENT = 1;
    public static final int SHARE_ALL_IN_ONE = 2;
    private int mShareType = SHARE_CLIENT;
    private String accessToken;// 用户访问令牌
    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;


    private ShareFactory(Context context) {
        api = WXAPIFactory.createWXAPI(context, AppConstants.WX_APP_ID);
        api.registerApp(AppConstants.WX_APP_ID);
        wxSdkVersion = api.getWXAppSupportAPI();

        this.mContext = context;
    }

    public static ShareFactory getInstance(Context context) {
        if (instance == null) {
            instance = new ShareFactory(context);
        }
        return instance;
    }

    private void downPicture(String image_url){

    }



    public void shareWX(String url, String title, String content, Bitmap   bitmap) {
        if (wxSdkVersion == 0) {
            T.s("您未安装微信客户端！");
        } else if (wxSdkVersion < TIMELINE_SUPPORTED_VERSION) {
            T.s("您的微信客户端版本过低！");
        } else {
            WXWebpageObject webpage_weixin = new WXWebpageObject();
            webpage_weixin.webpageUrl = url;
            WXMediaMessage msg_weixin = new WXMediaMessage(webpage_weixin);
            msg_weixin.title = title;
            msg_weixin.description = content;

            Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 150,
                    150, true);
            bitmap.recycle();
            msg_weixin.thumbData = ShareUtil.bmpToByteArray(
                    thumbBmp, true);

            SendMessageToWX.Req req_weixin = new SendMessageToWX.Req();
            req_weixin.transaction = ShareUtil.buildTransaction("webpage");
            req_weixin.message = msg_weixin;
            req_weixin.scene = SendMessageToWX.Req.WXSceneSession;
            api.sendReq(req_weixin);

        }
    }


    public void shareWX(ShareData shareData) {
        if (wxSdkVersion == 0) {
            T.s("您未安装微信客户端！");
        } else if (wxSdkVersion < TIMELINE_SUPPORTED_VERSION) {
            T.s("您的微信客户端版本过低！");
        } else {
            WXWebpageObject webpage_weixin = new WXWebpageObject();
            webpage_weixin.webpageUrl = shareData.getShare_url();
            WXMediaMessage msg_weixin = new WXMediaMessage(webpage_weixin);
            msg_weixin.title = shareData.getShare_title();
            msg_weixin.description = shareData.getShare_description();

            Bitmap bitmap=shareData.getShare_bitmap();
            Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 150,
                    150, true);
            bitmap.recycle();
            msg_weixin.thumbData = ShareUtil.bmpToByteArray(
                    thumbBmp, true);

            SendMessageToWX.Req req_weixin = new SendMessageToWX.Req();
            req_weixin.transaction = ShareUtil.buildTransaction("webpage");
            req_weixin.message = msg_weixin;
            req_weixin.scene = SendMessageToWX.Req.WXSceneSession;
            api.sendReq(req_weixin);

        }
    }

    public void shareWXFriend(ShareData shareData) {
        if (wxSdkVersion == 0) {
            T.s("您未安装微信客户端！");
        } else if (wxSdkVersion < TIMELINE_SUPPORTED_VERSION) {
            T.s("您的微信客户端版本过低！");
        } else {
            WXWebpageObject webpage_weixin = new WXWebpageObject();
            webpage_weixin.webpageUrl = shareData.getShare_url();
            WXMediaMessage msg_weixin = new WXMediaMessage(webpage_weixin);
            msg_weixin.title = shareData.getShare_title();
            msg_weixin.description = shareData.getShare_description();

            Bitmap bitmap= shareData.getShare_bitmap();

            Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 150,
                    150, true);
            bitmap.recycle();
            msg_weixin.thumbData = ShareUtil.bmpToByteArray(
                    thumbBmp, true);
            SendMessageToWX.Req req_weixin = new SendMessageToWX.Req();
            req_weixin.transaction = ShareUtil.buildTransaction("webpage");
            req_weixin.message = msg_weixin;
            req_weixin.scene = SendMessageToWX.Req.WXSceneTimeline;
            api.sendReq(req_weixin);

        }
    }

    public void shareWXFriend(String url, String title, String content, Bitmap   bitmap) {
        if (wxSdkVersion == 0) {
            T.s("您未安装微信客户端！");
        } else if (wxSdkVersion < TIMELINE_SUPPORTED_VERSION) {
            T.s("您的微信客户端版本过低！");
        } else {
            WXWebpageObject webpage_weixin = new WXWebpageObject();
            webpage_weixin.webpageUrl = url;
            WXMediaMessage msg_weixin = new WXMediaMessage(webpage_weixin);
            msg_weixin.title = title;
            msg_weixin.description = content;

//            Bitmap bitmap= CMethod.getBitmaptoFile(path,"share");

            Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 150,
                    150, true);
            bitmap.recycle();
            msg_weixin.thumbData = ShareUtil.bmpToByteArray(
                    thumbBmp, true);
            SendMessageToWX.Req req_weixin = new SendMessageToWX.Req();
            req_weixin.transaction = ShareUtil.buildTransaction("webpage");
            req_weixin.message = msg_weixin;
            req_weixin.scene = SendMessageToWX.Req.WXSceneTimeline;
            api.sendReq(req_weixin);

        }
    }


    public void shareSina(IWeiboShareAPI mWeiboShareAPI,ShareData shareData) {
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();//初始化微博的分享消息
        weiboMessage.textObject = ShareUtil.getTextObj(shareData.getShare_title());
        weiboMessage.imageObject = ShareUtil.getImageObj(shareData.getShare_bitmap());
        weiboMessage.mediaObject = ShareUtil.getWebpageObj(shareData.getShare_url(), shareData.getShare_title(), shareData.getShare_description(), shareData.getShare_bitmap());
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        AuthInfo authInfo = new AuthInfo(mContext, AppConstants.SINA_APP_KEY, AppConstants.SINA_REDIRECT_URL, AppConstants.SINA_SCOPE);
        Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(mContext.getApplicationContext());
        String token = "";
        if (accessToken != null) {
            token = accessToken.getToken();
        }
        mWeiboShareAPI.sendRequest((Activity) mContext, request, authInfo, token, new WeiboAuthListener() {

            @Override
            public void onWeiboException(WeiboException arg0) {
            }

            @Override
            public void onComplete(Bundle bundle) {
                // TODO Auto-generated method stub
                Oauth2AccessToken newToken = Oauth2AccessToken.parseAccessToken(bundle);
                AccessTokenKeeper.writeAccessToken(mContext.getApplicationContext(), newToken);
            }

            @Override
            public void onCancel() {
            }
        });
    }


    @Override
    public void onResponse(BaseResponse baseResp) {
        if (baseResp != null) {
            switch (baseResp.errCode) {
                case WBConstants.ErrorCode.ERR_OK:
                    T.s(R.string.weibosdk_demo_toast_share_success);
                    break;
                case WBConstants.ErrorCode.ERR_CANCEL:
                    T.s(R.string.weibosdk_demo_toast_share_canceled);
                    break;
                case WBConstants.ErrorCode.ERR_FAIL:
                    T.s(R.string.weibosdk_demo_toast_share_failed);
                    break;
            }
        }
    }







}
