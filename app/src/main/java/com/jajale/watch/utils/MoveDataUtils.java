package com.jajale.watch.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.jajale.watch.AppConstants;
import com.jajale.watch.PublicSwitch;
import com.jajale.watch.listener.HttpClientListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.protocol.HTTP;

/**
 * Created by llh on 16-6-17.
 */
public class MoveDataUtils {
    public static final String CAN_LOGIN = "login";
    public static final String CAN_MOVE = "move";
    public static final String NEED_CHECK_USER = "need_check_user";

    /**
     * 检查用户是否迁移接口
     */
    public static void checkuser(final String phone_num, final HttpClientListener listener) {

        String url = AppConstants.MOVE_DATA_CHECK_USER;
        L.e("http_client____url----->" + url);

        RequestParams params = new RequestParams();
        params.put("phone_num", phone_num);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(PublicSwitch.NETWORK_TIME_OUT_TIME);
        client.addHeader(HTTP.CONTENT_TYPE,
                "application/x-www-form-urlencoded;charset=UTF-8");
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                // TODO Auto-generated method stub
//                try {

                    String result = new String(responseBody);
                    if (!CMethod.isEmpty(result)) {
                        listener.onSuccess(new String(responseBody));
                    } else {
                        listener.onFailure("访问网络失败，请稍后重试！");
                    }


//                } catch (Exception e) {
//                    listener.onFailure("访问网络失败，请稍后重试！");
//                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                // TODO Auto-generated method stub
                L.e("http_client___error----->" + error);
                listener.onError();
                T.s("访问网络失败，请稍后重试！");
                error.printStackTrace();
            }
        });
    }




    /**
     * 检查用户是否迁移接口
     */
    public static void moveData(final String phone_num, final String pass,final String num,final HttpClientListener listener) {

        String url = AppConstants.MOVE_DATA_MV;
        L.e("http_client____url----->" + url);

        RequestParams params = new RequestParams();
        params.put("phone_num", phone_num);
        params.put("pass", pass);
        params.put("no", num);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(PublicSwitch.NETWORK_TIME_OUT_TIME);
        client.addHeader(HTTP.CONTENT_TYPE,
                "application/x-www-form-urlencoded;charset=UTF-8");
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                // TODO Auto-generated method stub
//                try {

                String result = new String(responseBody);
                if (!CMethod.isEmpty(result)) {
                    listener.onSuccess(new String(responseBody));
                } else {
                    listener.onFailure("访问网络失败，请稍后重试！");
                }


//                } catch (Exception e) {
//                    listener.onFailure("访问网络失败，请稍后重试！");
//                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                // TODO Auto-generated method stub
                L.e("http_client___error----->" + error);
                listener.onError();
                T.s("访问网络失败，请稍后重试！");
                error.printStackTrace();
            }
        });
    }

    public static void callPhone(Context context){

        Intent phoneIntent = new Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:" + "4000710608"));
        context.startActivity(phoneIntent);
    }


}
