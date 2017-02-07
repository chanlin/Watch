/*
 * @Date: 3/19/15 2:56 PM
 * @Copyright: 2015 Inc. All rights reserved.
 * 注意：本内容仅限于公司内部传阅，禁止外泄以及用于其他的商业目的
 */

package com.jajale.watch.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.jajale.watch.entitydb.Config;


/**
 * Created by pig on 3/19/15.
 * 只处理Config
 */
public class ConfigPreferencesUtils {
    private Context mContext;
    private String PREFERENCES_NAME = "config-preferences";
    private final SharedPreferences sp;

    public ConfigPreferencesUtils(Context context) {
        this.mContext = context;
        this.sp = mContext.getSharedPreferences(PREFERENCES_NAME, Context.MODE_MULTI_PROCESS + Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE);
    }

    public String getCowPicToken() {
        return sp.getString("cowPicToken", null);
    }

    public String getCowMediaToken() {
        return sp.getString("cowMediaToken", null);
    }

    /**
     * 7牛音视频域名
     *
     * @return
     */
    public String getCowMediaUrl() {
        return sp.getString("cow_media_url", null);
    }


    /**
     * 处理服务器返回的数据
     * @param config
     */
    public void handle(Config config) {


        //如果是启动页图片
        String imageUrl = "";
        new StartUpUtils(mContext).handle(imageUrl);
    }
}
