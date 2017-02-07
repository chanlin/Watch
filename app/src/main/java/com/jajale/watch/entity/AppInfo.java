package com.jajale.watch.entity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.jajale.watch.BaseApplication;
import com.jajale.watch.utils.CacheUtils;
import com.umeng.analytics.AnalyticsConfig;

public class AppInfo {

    private String versionName;
    private String key;
    private String channel;
    private int versionCode;
//    private int userDBCode ;
//    private int accountDBCode ;
    private String packageName;
    private int screenWidth;
    private int screenHeight;
    private float density;
    private double latitude;
    private double longitude;
    private String city_code;
    private int statusBarHeight;
    private String district;
    private String street;
    private String city = "北京市";
    private float radius;
    private float derect;
    private String addrStr;
    private String adCode ;


    /**
     * 默认拍照后存在这里
     */
    private String cameraTempPath = CacheUtils.getExternalCacheDir(BaseApplication.getContext()) + "tmp.jpg";
    private String rootCacheDir = CacheUtils.getExternalCacheDir(BaseApplication.getContext());
    /**
     * imei deviceid
     */
    private String deviceid;
    /**
     * imei deviceid
     */
    private String imei;
    /**
     * 固件
     */
    private String firmware;
    /**
     * 手机型号
     */
    private String phone_model;
    /**
     * 手机分辨率
     */
    private String resolution;
    /**
     * mac地址
     */
    private String mac;
    /**
     * 系统版本号
     */
    private String system_version;
    /**
     * 平台
     */
    private String CLIENT_PLATFORM = "1"; //1:android 2:ios
    /**
     * 产品
     */
    private String CLINET_APPID = "1";     //1:watch_v_1

    private static AppInfo mAppInfo;
    private String appName;

    /**
     * 规定：只在application里初始化
     */
    public static void init(Context context) {
        if (mAppInfo == null) {
            mAppInfo = new AppInfo(context);
        }
    }

    public static AppInfo getInstace() {
        if (mAppInfo == null) {
            throw new NullPointerException("AppInfo必须在application里init!");
        }
        return mAppInfo;
    }

    private AppInfo() {
    }

    public String getCameraTempPath() {
        return cameraTempPath;
    }

    /**
     * 所有的应用启动之后获得的信息放在这里
     *
     * @param context
     */
    private AppInfo(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
//			this.channel = applicationInfo.metaData.getString("UMENG_CHANNEL");
            this.channel = AnalyticsConfig.getChannel(context);
            this.packageName = context.getPackageName();
            PackageManager pm = context.getPackageManager();
            PackageInfo pInfo = pm.getPackageInfo(context.getPackageName(), 0);
            this.versionCode = pInfo.versionCode;
            this.versionName = pInfo.versionName;
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            this.screenWidth = displayMetrics.widthPixels;
            this.screenHeight = displayMetrics.heightPixels;
            this.density = displayMetrics.density;

            this.appName = pm.getApplicationLabel(applicationInfo).toString();

            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            this.imei = telephonyManager.getDeviceId();
            this.deviceid = telephonyManager.getDeviceId();
            this.firmware = Build.VERSION.RELEASE;
            this.phone_model = Build.MODEL;
            this.resolution = this.screenHeight + "*" + this.screenWidth;
            this.mac = getLocalMacAddress(context);
            this.system_version = Build.VERSION.SDK_INT + "";

            StringBuffer buffer = new StringBuffer();
            buffer.append(getCLIENT_PLATFORM()).append("_")
                    .append(getCLINET_APPID()).append("_")
                    .append(getChannel()).append("_")
                    .append(getVersionCode()).append("_")
                    .append(getVersionName());
            this.key = buffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * 谨慎调用 不一定有 自己确定有的时候再调用
     *
     * @return
     * @author yuancl Jun 12, 2014
     */
    public int getStatusBarHeight() {
        return statusBarHeight;
    }

    public int getStatusBarHeight(Activity activity) {
        if (statusBarHeight == 0) {
            Rect frame = new Rect();
            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            this.statusBarHeight = frame.top;
        }
        return statusBarHeight;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getAddrStr() {
        return addrStr;
    }

    public void setAddrStr(String addrStr) {
        this.addrStr = addrStr;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getCityCode() {
        return city_code;
    }

    public void setCityCode(String city_code) {
        this.city_code = city_code;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStatusBarHeight(int statusBarHeight) {
        this.statusBarHeight = statusBarHeight;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getDerect() {
        return derect;
    }

    public void setDerect(float derect) {
        this.derect = derect;
    }

    public float getDensity() {
        return density;
    }

    public String getImei() {
        return imei;
    }

    public String getFirmware() {
        return firmware;
    }

    public String getPhone_model() {
        return phone_model;
    }

    public String getResolution() {
        return resolution;
    }

    public String getMac() {
        return mac;
    }

    public String getSystem_version() {
        return system_version;
    }

    public String getDeviceid() {
        return deviceid;
    }


    public String getRootCacheDir() {
        return rootCacheDir;
    }


    public String getCLINET_APPID() {
        return CLINET_APPID;
    }

    public void setCLINET_APPID(String CLINET_APPID) {
        this.CLINET_APPID = CLINET_APPID;
    }

    public String getCLIENT_PLATFORM() {
        return CLIENT_PLATFORM;
    }

    public void setCLIENT_PLATFORM(String CLIENT_PLATFORM) {
        this.CLIENT_PLATFORM = CLIENT_PLATFORM;
    }

    public String getKey() {
        return key;
    }

    public String getAppName() {
        return appName;
    }

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }
}
