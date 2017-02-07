package com.jajale.watch.utils;

import com.google.gson.Gson;
import com.jajale.watch.AppConstants;
import com.jajale.watch.PublicSwitch;
import com.jajale.watch.entity.AppInfo;
import com.jajale.watch.entity.HttpBackData;
import com.jajale.watch.listener.HttpClientListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

/**
 * Created by lilonghui on 2016/2/23.
 * Email:lilonghui@bjjajale.com
 */
public class HttpUtils {


    public static RequestParams getParams(JSONObject jsonObject) {
        RequestParams params = new RequestParams();
        params.put("platform", AppConstants.PLATFORM);
        L.e("http_client____platform----->"+AppConstants.PLATFORM);
        params.put("app_v", AppInfo.getInstace().getVersionName());
        L.e("http_client____app_v----->" + AppInfo.getInstace().getVersionName());
        params.put("apiversion", AppConstants.API_VERSION);
        L.e("http_client____apiversion----->" + AppConstants.API_VERSION);
        if (jsonObject!=null){
            L.e("http_client____data1----->" + jsonObject.toString());
            String data = CMethod.encryptThreeDESECB(jsonObject.toString(), AppConstants.SECRET_KEY);
            params.put("data", data);
            L.e("http_client____data2----->" + data);
        }else{
            params.put("data", CMethod.encryptThreeDESECB("{}", AppConstants.SECRET_KEY));
            L.e("http_client____data2-----无参数>");
        }
        return params;
    }
    public static RequestParams getParams(JSONObject jsonObject,int code) {
        RequestParams params = new RequestParams();
        params.put("platform", AppConstants.PLATFORM);
        L.e("http_client____platform----->"+AppConstants.PLATFORM);
        params.put("code", code);
        L.e("http_client____code----->"+code);
        params.put("app_v", AppInfo.getInstace().getVersionName());
        L.e("http_client____app_v----->" + AppInfo.getInstace().getVersionName());
        params.put("apiversion", AppConstants.API_VERSION);
        L.e("http_client____apiversion----->" + AppConstants.API_VERSION);
        if (jsonObject!=null){
            L.e("http_client____data1----->" + jsonObject.toString());
            String data = CMethod.encryptThreeDESECB(jsonObject.toString(), AppConstants.SECRET_KEY);
            params.put("data", data);
            L.e("http_client____data2----->" + data);
        }else{
            params.put("data", CMethod.encryptThreeDESECB("{}", AppConstants.SECRET_KEY));
            L.e("http_client____data2-----无参数>");
        }
        return params;
    }


    /**
     * post方法请求数据API或CENTER
     * @param apiCode  不同的apiCode得到不同的url头
     * @param url 具体url地址（不包含地址和端口号）
     * @param jsonObject 传递的参数
     * @param listener 回调监听
     */
    public static void PostDataToWeb(int apiCode,String url, JSONObject jsonObject, final HttpClientListener listener) {

        url=UrlAddressUrils.getPath(apiCode,url);
        L.e("http_client____url----->" + url);

        RequestParams params = getParams(jsonObject);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(PublicSwitch.NETWORK_TIME_OUT_TIME);
        client.addHeader(HTTP.CONTENT_TYPE,
                "application/x-www-form-urlencoded;charset=UTF-8");
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                // TODO Auto-generated method stub
                L.e("http_client____all_result----->" + new String(responseBody));
                try {
                    Gson gson = new Gson();
                    HttpBackData httpBackData = gson.fromJson(new String(responseBody), HttpBackData.class);
                    if (httpBackData == null) {
                        listener.onError();
                        T.s("访问网络失败，请稍后重试！");
                        return;
                    }

                    L.e("http_client____desc_result----->" + httpBackData.getDesc());
                    String string_result= CMethod.decryptThreeDESECB(httpBackData.getData(), AppConstants.SECRET_KEY);
                    L.e("http_client____data_result----->" + string_result);

                    if (httpBackData.getCode() == 200) {
                        if (httpBackData.getData() != null)
                            listener.onSuccess(string_result);
                    } else {
                        if (httpBackData.getDesc() != null)
                            listener.onFailure(httpBackData.getDesc());
                    }
                } catch (Exception e) {
                    listener.onFailure("访问网络失败，请稍后重试！");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {
                // TODO Auto-generated method stub
                L.e("http_client___error----->" + error);
                listener.onError();
//                T.s("访问网络失败，请稍后重试！");
                error.printStackTrace();
            }
        });
    }


    /**
     * post方法请求数据CONTROLLER
     * @param url  具体url地址（不包含地址和端口号）
     * @param code  CONTROLLER接口的相关code
     * @param jsonObject 传递的数据
     * @param listener 回调监听
     */
    public static void PostDataToWeb(String url, int code, final JSONObject jsonObject, final HttpClientListener listener) {

        url=UrlAddressUrils.getPath(UrlAddressUrils.CODE_CONTROLLER,url);
        L.e("http_client____url----->" + url);

        RequestParams params = getParams(jsonObject,code);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(PublicSwitch.NETWORK_TIME_OUT_TIME);
        client.addHeader(HTTP.CONTENT_TYPE,
                "application/x-www-form-urlencoded;charset=UTF-8");
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                // TODO Auto-generated method stub
                L.e("http_client____data_result----->" + CMethod.decryptThreeDESECB(new String(responseBody), AppConstants.SECRET_KEY));

                String result=CMethod.decryptThreeDESECB(new String(responseBody), AppConstants.SECRET_KEY);

                try {
                    HttpBackData httpBackData=new HttpBackData();
                    JSONObject rootJO = new JSONObject(result);
                    httpBackData.setCode(JSONUtils.getInt(rootJO, "code"));
                    httpBackData.setDesc(JSONUtils.getString(rootJO, "desc"));
                    httpBackData.setTime(JSONUtils.getString(rootJO, "time"));
                    httpBackData.setData(JSONUtils.getString(rootJO, "data"));

                    if (httpBackData == null) {
                        listener.onError();
                        T.s("访问网络失败，请稍后重试！");
                        return;
                    }
                    if (httpBackData.getCode() == 200) {
                        if (httpBackData.getData() != null)
                            listener.onSuccess(httpBackData.getData());
                    } else {
                        if (httpBackData.getDesc() != null)
                            listener.onFailure(httpBackData.getDesc());
                    }
                } catch (Exception e) {
                    listener.onFailure("访问网络失败，请稍后重试！");
                }
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
     * post方法请求数据
     */


    /**
     * post方法请求数据(调试具体某人的)
     *
     * @param apiCode 不同的apiCode得到不同的url头（之针对线上）
     * @param coderUrlDomain  不同的url地址和端口号
     * @param url  具体url地址（不包含地址和端口号）
     * @param jsonObject 传递的参数
     * @param listener 回调监听
     */
    public static void PostDataToWeb(int apiCode,String coderUrlDomain ,String url, JSONObject jsonObject, final HttpClientListener listener) {

        url=UrlAddressUrils.getPath(coderUrlDomain,apiCode,url);

        L.e("http_client____url----->" + url);

        RequestParams params = getParams(jsonObject);
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(PublicSwitch.NETWORK_TIME_OUT_TIME);
        client.addHeader(HTTP.CONTENT_TYPE,
                "application/x-www-form-urlencoded;charset=UTF-8");
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                // TODO Auto-generated method stub
                L.e("http_client____all_result----->" + new String(responseBody));
                try {
                    Gson gson = new Gson();
                    HttpBackData httpBackData = gson.fromJson(new String(responseBody), HttpBackData.class);
                    if (httpBackData == null) {
                        listener.onError();
                        T.s("访问网络失败，请稍后重试！");
                        return;
                    }
                    L.e("http_client____desc_result----->" + httpBackData.getDesc());
                    L.e("http_client____data_result----->" + CMethod.decryptThreeDESECB(httpBackData.getData(), AppConstants.SECRET_KEY));

                    if (httpBackData.getCode() == 200) {
                        if (httpBackData.getData() != null)
                            listener.onSuccess(CMethod.decryptThreeDESECB(httpBackData.getData(), AppConstants.SECRET_KEY));
                    } else {
                        if (httpBackData.getDesc() != null)
                            listener.onFailure(httpBackData.getDesc());
                    }
                } catch (Exception e) {
                    listener.onFailure("访问网络失败，请稍后重试！");
                }
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
     书籍列表（单独）
     */
    public static void PostDataToWebBook(final JSONObject jsonObject, final HttpClientListener listener) {

         String   url=AppConstants.JAVA_EDUCATION_BOOK_URL;
        L.e("http_client____url----->" + url);

        RequestParams params = new RequestParams();
        params.put("platform", AppConstants.PLATFORM);
        L.e("http_client____platform----->"+AppConstants.PLATFORM);
        params.put("app_v", AppInfo.getInstace().getVersionName());
        L.e("http_client____app_v----->" + AppInfo.getInstace().getVersionName());
        params.put("apiversion", AppConstants.API_VERSION);
        L.e("http_client____apiversion----->" + AppConstants.API_VERSION);
        params.put("book_type", "-1");
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(PublicSwitch.NETWORK_TIME_OUT_TIME);
        client.addHeader(HTTP.CONTENT_TYPE,
                "application/x-www-form-urlencoded;charset=UTF-8");
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] responseBody) {
                // TODO Auto-generated method stub
                try {
                    HttpBackData httpBackData=new HttpBackData();
                    JSONObject rootJO = new JSONObject(new String(responseBody));
                    httpBackData.setCode(JSONUtils.getInt(rootJO, "code"));
                    httpBackData.setDesc(JSONUtils.getString(rootJO, "desc"));
                    httpBackData.setTime(JSONUtils.getString(rootJO, "time"));
                    httpBackData.setData(JSONUtils.getString(rootJO, "data"));

                    if (httpBackData == null) {
                        listener.onError();
                        T.s("访问网络失败，请稍后重试！");
                        return;
                    }
                    if (httpBackData.getCode() == 200) {
                        if (httpBackData.getData() != null)
                            listener.onSuccess(httpBackData.getData());
                    } else {
                        if (httpBackData.getDesc() != null)
                            listener.onFailure(httpBackData.getDesc());
                    }
                } catch (Exception e) {
                    listener.onFailure("访问网络失败，请稍后重试！");
                }
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



}
