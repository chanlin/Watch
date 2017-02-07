package com.jajale.watch;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.jajale.watch.entity.SPKeyConstants;
import com.jajale.watch.utils.PhoneSPUtils;

/**
 * Created by athena on 2015/11/11.
 * Email: lizhiqiang@bjjajale.com
 */
public class PhoneParameter {
    //---------------single instance-------------------------//
    private static PhoneParameter instance = null;
    private static PhoneSPUtils spUtils ;


    private PhoneParameter() {
    }

    public static PhoneParameter getInstances(Context context) {
        if (instance == null) {
            instance = new PhoneParameter();
            spUtils = new PhoneSPUtils(context);
        }
        return instance;
    }
    //-----------------single instance------------------------//

    public float DENSITY = -1;//屏幕密度
    public int SCREEN_WIDTH = -1;//屏幕宽
    public int SCREEN_HEIGHT = -1;//屏幕高
    public float XDIP = -1;
    public float YDIP = -1;

    public int getApi_Vsersion() {
        return Build.VERSION.SDK_INT;
    }

    public String SPLASH_VERSION = "";//广告图

    public String getSplashVersion() {
        if (TextUtils.isEmpty(SPLASH_VERSION)) {
//            SPLASH_VERSION = AppSharePreferenceManager.getInstance().open(AppSharePreferenceManager.JJL_APP_SHAREPREFERENCE).getString(SPKeyConstants.SPLASH_BANNER_VERSION);
            spUtils.getString(SPKeyConstants.SPLASH_BANNER_VERSION);
        }
        return SPLASH_VERSION;
    }

    public void setSplashVersion(String version) {
        SPLASH_VERSION = version;
//        AppSharePreferenceManager.getInstance().open(AppSharePreferenceManager.JJL_APP_SHAREPREFERENCE).save(SPKeyConstants.SPLASH_BANNER_VERSION, version);
        spUtils.save(SPKeyConstants.SPLASH_BANNER_VERSION, version);

    }

    public float getDensity() {
        if (DENSITY == -1) {
//            DENSITY = AppSharePreferenceManager.getInstance().open(AppSharePreferenceManager.JJL_APP_SHAREPREFERENCE).getFloat(SPKeyConstants.CONFIG_PHONE_DENSITY);
            DENSITY =spUtils.getFloat(SPKeyConstants.CONFIG_PHONE_DENSITY);

        }
        return DENSITY;
    }

    public void setDensity(float density) {
        DENSITY = density;
//        AppSharePreference.savePhoneDensity(density);
//        AppSharePreferenceManager.getInstance().open(AppSharePreferenceManager.JJL_APP_SHAREPREFERENCE).save(SPKeyConstants.CONFIG_PHONE_DENSITY, density);
        spUtils.save(SPKeyConstants.CONFIG_PHONE_DENSITY, density);

    }

    public int getScreen_Width() {
        if (SCREEN_WIDTH == -1) {
//            SCREEN_WIDTH =  AppSharePreferenceManager.getInstance().open(AppSharePreferenceManager.JJL_APP_SHAREPREFERENCE).getInt(SPKeyConstants.CONFIG_PHONE_SCREEN_WIDTH);
            spUtils.getInt(SPKeyConstants.CONFIG_PHONE_SCREEN_WIDTH);
        }
        return SCREEN_WIDTH;
    }

    public void setScreen_Width(int width) {
        SCREEN_WIDTH = width;
//        AppSharePreferenceManager.getInstance().open(AppSharePreferenceManager.JJL_APP_SHAREPREFERENCE).save(SPKeyConstants.CONFIG_PHONE_SCREEN_WIDTH,width);
        spUtils.save(SPKeyConstants.CONFIG_PHONE_SCREEN_WIDTH,width);

    }

    public int getScreen_Height() {
        if (SCREEN_HEIGHT == -1) {
//            SCREEN_WIDTH =  AppSharePreferenceManager.getInstance().open(AppSharePreferenceManager.JJL_APP_SHAREPREFERENCE).getInt(SPKeyConstants.CONFIG_PHONE_SCREEN_HEIGHT);
            SCREEN_WIDTH =spUtils.getInt(SPKeyConstants.CONFIG_PHONE_SCREEN_HEIGHT);
        }
        return SCREEN_HEIGHT;
    }

    public void setScreen_Height(int height) {
        SCREEN_HEIGHT = height;
//        AppSharePreferenceManager.getInstance().open(AppSharePreferenceManager.JJL_APP_SHAREPREFERENCE).save(SPKeyConstants.CONFIG_PHONE_SCREEN_HEIGHT,height);
        spUtils.save(SPKeyConstants.CONFIG_PHONE_SCREEN_HEIGHT,height);
    }

    public float getXDIP() {
		if(XDIP == -1){
//            XDIP  =  AppSharePreferenceManager.getInstance().open(AppSharePreferenceManager.JJL_APP_SHAREPREFERENCE).getFloat(SPKeyConstants.CONFIG_PHONE_SCREEN_XDIP);
            XDIP  = spUtils.getFloat(SPKeyConstants.CONFIG_PHONE_SCREEN_XDIP);
        }
        return XDIP;
    }

    public void setXDIP(float xDIP) {
        XDIP = xDIP;
//        AppSharePreferenceManager.getInstance().open(AppSharePreferenceManager.JJL_APP_SHAREPREFERENCE).save(SPKeyConstants.CONFIG_PHONE_SCREEN_HEIGHT,XDIP);
        spUtils.save(SPKeyConstants.CONFIG_PHONE_SCREEN_XDIP,XDIP);
    }

    public float getYDIP() {
		if(YDIP == -1){
//            YDIP  =  AppSharePreferenceManager.getInstance().open(AppSharePreferenceManager.JJL_APP_SHAREPREFERENCE).getFloat(SPKeyConstants.CONFIG_PHONE_SCREEN_YDIP);
            spUtils.getFloat(SPKeyConstants.CONFIG_PHONE_SCREEN_YDIP);
        }
        return YDIP;
    }

    public void setYDIP(float yDIP) {
        YDIP = yDIP;
//        AppSharePreferenceManager.getInstance().open(AppSharePreferenceManager.JJL_APP_SHAREPREFERENCE).save(SPKeyConstants.CONFIG_PHONE_SCREEN_HEIGHT,yDIP);
        spUtils.save(SPKeyConstants.CONFIG_PHONE_SCREEN_YDIP,yDIP);
    }

}
