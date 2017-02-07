package com.jajale.watch.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.jajale.watch.AppConstants;
import com.jajale.watch.entity.VersionUpdateData;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.listener.SimpleClickListener;
import com.jajale.watch.listener.VersionUpdateListener;

/**
 * 版本更新
 * Created by lilonghui on 2015/12/8.
 * Email:lilonghui@bjjajale.com
 */
public class VersionUpdateUtils {


    public static void update(final Context context, final VersionUpdateListener listener)

    {


        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API, AppConstants.JAVA_APP_UPDATE_URL, null, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {

                Gson gson = new Gson();
                final VersionUpdateData data = gson.fromJson(result, VersionUpdateData.class);
                if (data != null)
                    if (CMethod.isEmpty(data.getVersion())) {
                        if (listener != null) {
                            listener.showToast("已经是最新版本！");
                            listener.next();
                        }
                    } else if (data.getForcedState().equals("1")) {
                        //强制更新

                        DialogUtils.versionForceUpdateDialog(context, data.getVersion(), data.getDescribe(), new SimpleClickListener() {
                            @Override
                            public void ok() {
                                if (listener != null && !data.getUrl().equals("")) {
                                    listener.next();
                                    listener.downLoad(data.getUrl());
                                }
                            }

                            @Override
                            public void cancle() {
                                listener.next();
                            }
                        });
                    } else {
                        DialogUtils.versionUpdateDialog(context, data.getVersion(), data.getDescribe(), new SimpleClickListener() {
                            @Override
                            public void ok() {
                                if (listener != null && !data.getUrl().equals("")) {
                                    listener.next();
                                    listener.downLoad(data.getUrl());
                                }

                            }

                            @Override
                            public void cancle() {
                                listener.next();
                            }
                        });
                    }


            }

            @Override
            public void onFailure(String result) {
                listener.next();
            }

            @Override
            public void onError() {
                listener.next();
            }
        });
    }

}
