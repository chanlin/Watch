package com.jajale.watch.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.IntentAction;
import com.jajale.watch.R;
import com.jajale.watch.activity.ChildMsgActivity;
import com.jajale.watch.activity.SplashActivity;
import com.jajale.watch.activity.SystemMsgActivity;
import com.jajale.watch.entity.AppInfo;
import com.jajale.watch.entity.BDPosition;
import com.jajale.watch.entitydb.DbHelper;
import com.jajale.watch.entitydb.Message;
import com.jajale.watch.entitydb.MsgMember;
import com.jajale.watch.entitydb.User;
import com.jajale.watch.helper.DownloadHelper;
import com.jajale.watch.listener.VoiceDownloadListener;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

import static android.content.pm.ApplicationInfo.FLAG_LARGE_HEAP;
import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.HONEYCOMB;

/**
 * Created by athena on 2015/11/11.
 * Email: lizhiqiang@bjjajale.com
 */
public class CMethod {
    private static final int NETWORK_TYPE_UNAVAILABLE = -1;
    // private static final int NETWORK_TYPE_MOBILE = -100;
    private static final int NETWORK_TYPE_WIFI = -101;

    private static final int NETWORK_CLASS_WIFI = -101;
    private static final int NETWORK_CLASS_UNAVAILABLE = -1;
    /**
     * Unknown network class.
     */
    private static final int NETWORK_CLASS_UNKNOWN = 0;
    /**
     * Class of broadly defined "2G" networks.
     */
    private static final int NETWORK_CLASS_2_G = 1;
    /**
     * Class of broadly defined "3G" networks.
     */
    private static final int NETWORK_CLASS_3_G = 2;
    /**
     * Class of broadly defined "4G" networks.
     */
    private static final int NETWORK_CLASS_4_G = 3;


    // 适配低版本手机
    /**
     * Network type is unknown
     */
    private static final int NETWORK_TYPE_UNKNOWN = 0;
    /**
     * Current network is GPRS
     */
    private static final int NETWORK_TYPE_GPRS = 1;
    /**
     * Current network is EDGE
     */
    private static final int NETWORK_TYPE_EDGE = 2;
    /**
     * Current network is UMTS
     */
    private static final int NETWORK_TYPE_UMTS = 3;
    /**
     * Current network is CDMA: Either IS95A or IS95B
     */
    private static final int NETWORK_TYPE_CDMA = 4;
    /**
     * Current network is EVDO revision 0
     */
    private static final int NETWORK_TYPE_EVDO_0 = 5;
    /**
     * Current network is EVDO revision A
     */
    private static final int NETWORK_TYPE_EVDO_A = 6;
    /**
     * Current network is 1xRTT
     */
    private static final int NETWORK_TYPE_1xRTT = 7;
    /**
     * Current network is HSDPA
     */
    private static final int NETWORK_TYPE_HSDPA = 8;
    /**
     * Current network is HSUPA
     */
    private static final int NETWORK_TYPE_HSUPA = 9;
    /**
     * Current network is HSPA
     */
    private static final int NETWORK_TYPE_HSPA = 10;
    /**
     * Current network is iDen
     */
    private static final int NETWORK_TYPE_IDEN = 11;
    /**
     * Current network is EVDO revision B
     */
    private static final int NETWORK_TYPE_EVDO_B = 12;
    /**
     * Current network is LTE
     */
    private static final int NETWORK_TYPE_LTE = 13;
    /**
     * Current network is eHRPD
     */
    private static final int NETWORK_TYPE_EHRPD = 14;
    /**
     * Current network is HSPA+
     */
    private static final int NETWORK_TYPE_HSPAP = 15;

    private static String suffixes = ".amr";
    private final static int[] dayArr = new int[]{20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22};
    public final static String[] constellationArr = new String[]{"摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座"};
    private final static String[] Animals = {"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};

    public static int characterDicLenth = 0;
    public static int interestDicLenth = 0;


//    /**
//     * 获取友盟渠道id
//     *
//     * @param context
//     * @return
//     */
//    public static String getChannel(Context context) {
//        ApplicationInfo appInfo = null;
//        try {
//            appInfo = context.getPackageManager()
//                    .getApplicationInfo(context.getPackageName(),
//                            PackageManager.GET_META_DATA);
//            String msg=appInfo.metaData.getString("UMENG_CHANNEL");
//            L.d( " msg == " + msg );
//
//            return msg;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        return "";
//
//    }


//    public static String getChannel(Context context) {
//        //从apk包中获取
//        ApplicationInfo appinfo = context.getApplicationInfo();
//        String sourceDir = appinfo.sourceDir;
//        //注意这里：默认放在meta-inf/里， 所以需要再拼接一下
//        String key = "META-INF/"+ AppConstants.CHANNEL_KEY;
//        String ret = "";
//        ZipFile zipfile = null;
//        try {
//            zipfile = new ZipFile(sourceDir);
//            Enumeration<?> entries = zipfile.entries();
//            while (entries.hasMoreElements()) {
//                ZipEntry entry = ((ZipEntry) entries.nextElement());
//                String entryName = entry.getName();
//                if (entryName.startsWith(key)) {
//                    String channel = entryName.substring(key.length());
//                    return isEmptyOrZero(channel) ? "" : channel.trim();
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (zipfile != null) {
//                try {
//                    zipfile.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return "";
//    }


    public static Double parseDouble(String string ){

        if (isEmpty(string)){
            return 0.0;
        }else {
            return  Double.parseDouble(string);
        }
    }
    public static int parseInt(String string ){

        if (isEmpty(string)){
            return 0;
        }else {
            return  Integer.parseInt(string);
        }
    }





    /**
     * 将LatLng转化为LatLonPoint
     */
    public static LatLonPoint toLatLonPoint(LatLng latLng) {
        return new LatLonPoint(latLng.latitude, latLng.longitude);
    }

    /**
     * 将LatLonPoint转化为LatLng
     */
    public static LatLng toLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    public static void showToastByJs(int type, String toastString) {

        switch (type) {
            case 1:
                T.s(toastString);
                break;
        }
    }

    public static LatLng getLatAndLng() {
        //获取定位的经纬度
        double lat = AppInfo.getInstace().getLatitude();
        double lon = AppInfo.getInstace().getLongitude();
        //appInfo没有数据，则读内存数据
        User userInfo = BaseApplication.getUserInfo();
        if (userInfo != null && userInfo.latitude > 0) {
            lat = lat == 0 ? userInfo.latitude : lat;
            lon = lon == 0 ? userInfo.longitude : lon;
        }
        //以后没有数据，则读数据库数据
        DbHelper<User> userDbHelper = new DbHelper<User>(BaseApplication.getBaseHelper(), User.class);
        List<User> users = userDbHelper.queryForEq("userID", userInfo.userID);
        if (users != null && users.size() > 0) {
            User user = users.get(0);
            lat = lat == 0 ? user.latitude : lat;
            lon = lon == 0 ? user.longitude : lon;
        }
        return new LatLng(lat, lon);
    }

    /**
     * 判断 字符串是否为空
     *
     * @param src
     * @return
     */
    public static boolean isEmpty(String src) {
        return TextUtils.isEmpty(src) || src.toLowerCase().equals("null");
    }

    /**
     * 判断字符串是否为空/0
     *
     * @param src
     * @return
     */
    public static boolean isEmptyOrZero(String src) {
        return TextUtils.isEmpty(src) || src.trim().toLowerCase().equals("null") || src.trim().toLowerCase().equals("0") || src.trim().equals("");
    }

    public static boolean isEmptyPoi(double src) {
        double empty = 0.0d;
        if (isEmpty(src + "") || src == empty) {
            return true;
        }
        return false;
    }


    public static String getAppNameByPid(Context context) {
        String processName = null;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> l = am.getRunningAppProcesses();
        Iterator<ActivityManager.RunningAppProcessInfo> i = l.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
            }
        }
        return processName;
    }

    public static String getKey(String url) {
        int index = url.lastIndexOf("/");
        return url.substring(index + 1);
    }

    /**
     * 获取网络类型
     *
     * @return
     */
    public static String getCurrentNetworkType(Context context) {
        int networkClass = getNetworkClass(context);
        String type = "未知";
        switch (networkClass) {
            case NETWORK_CLASS_UNAVAILABLE:
                type = "无";
                break;
            case NETWORK_CLASS_WIFI:
                type = "Wi-Fi";
                break;
            case NETWORK_CLASS_2_G:
                type = "2G";
                break;
            case NETWORK_CLASS_3_G:
                type = "3G";
                break;
            case NETWORK_CLASS_4_G:
                type = "4G";
                break;
            case NETWORK_CLASS_UNKNOWN:
                type = "未知";
                break;
        }
        return type;
    }

    private static int getNetworkClass(Context context) {
        int networkType = NETWORK_TYPE_UNKNOWN;
        try {
            final NetworkInfo network = ((ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (network != null && network.isAvailable() && network.isConnected()) {
                int type = network.getType();
                if (type == ConnectivityManager.TYPE_WIFI) {
                    networkType = NETWORK_TYPE_WIFI;
                } else if (type == ConnectivityManager.TYPE_MOBILE) {
                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    networkType = telephonyManager.getNetworkType();
                }
            } else {
                networkType = NETWORK_TYPE_UNAVAILABLE;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return getNetworkClassByType(networkType);

    }

    private static int getNetworkClassByType(int networkType) {
        switch (networkType) {
            case NETWORK_TYPE_UNAVAILABLE:
                return NETWORK_CLASS_UNAVAILABLE;
            case NETWORK_TYPE_WIFI:
                return NETWORK_CLASS_WIFI;
            case NETWORK_TYPE_GPRS:
            case NETWORK_TYPE_EDGE:
            case NETWORK_TYPE_CDMA:
            case NETWORK_TYPE_1xRTT:
            case NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2_G;
            case NETWORK_TYPE_UMTS:
            case NETWORK_TYPE_EVDO_0:
            case NETWORK_TYPE_EVDO_A:
            case NETWORK_TYPE_HSDPA:
            case NETWORK_TYPE_HSUPA:
            case NETWORK_TYPE_HSPA:
            case NETWORK_TYPE_EVDO_B:
            case NETWORK_TYPE_EHRPD:
            case NETWORK_TYPE_HSPAP:
                return NETWORK_CLASS_3_G;
            case NETWORK_TYPE_LTE:
                return NETWORK_CLASS_4_G;
            default:
                return NETWORK_CLASS_UNKNOWN;
        }
    }

    public static boolean isNetWorkEnable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            }
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 判断当前网络是否已经连接
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getAppVersionName(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pInfo = pm.getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "unkonw";
    }


    public static void killBaiduProcess(Context context) {

        String pid = CMethod.getPidByProcessName(context, "com.wctw.date:remote");
        if (!CMethod.isEmptyOrZero(pid)) {
            String command = "kill -9 " + pid;
            try {
                Runtime.getRuntime().exec(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getPidByProcessName(Context context, String name) {

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> l = am.getRunningAppProcesses();
        Iterator<ActivityManager.RunningAppProcessInfo> i = l.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (name.equals(info.processName)) {
                    return info.pid + "";
                }
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 创建快捷方式
     *
     * @param context
     */
    public static void createShortcut(Context context) {
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
        shortcut.putExtra("duplicate", false);//设置是否重复创建
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClass(context, SplashActivity.class);//设置第一个页面
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(context, R.mipmap.ic_launcher);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        context.sendBroadcast(shortcut);
    }


    // 判读是否已经存在快捷方式
    public static boolean isExistShortCut(Context context) {
        boolean isInstallShortcut = false;
        final ContentResolver cr = context.getContentResolver();
        // 本人的2.2系统是”com.android.launcher2.settings”,网上见其他的为"com.android.launcher.settings"
        final String AUTHORITY = "com.android.launcher2.settings";
        final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/favorites?notify=true");
        Cursor c = cr.query(CONTENT_URI, new String[]{"title", "iconResource"}, "title=?", new String[]{context.getString(R.string.app_name)}, null);
        if (c != null && c.getCount() > 0) {
            isInstallShortcut = true;
        }
        return isInstallShortcut;
    }

    /**
     * 用来判断服务是否运行.
     *
     * @param context
     * @param className
     * @return
     */
    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            String name = serviceList.get(i).service.getClassName();
            if (name.equals(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    public static long getTimeMillion(String time) throws ParseException {
        SimpleDateFormat mh = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date temp = mh.parse(time);
        return temp.getTime();
    }

    public static String getToday() {
        long currentSeconds = System.currentTimeMillis();//系统当前时间
        SimpleDateFormat mh = new SimpleDateFormat("dd");
        String temp = mh.format(currentSeconds);
        return temp;
    }

    public static String getDayofIndex(String timestamp) {
        long time = 0;
        try {
            time = Long.parseLong(timestamp);
            SimpleDateFormat mh = new SimpleDateFormat("dd");
            String temp = mh.format(time);
            return temp;
        } catch (Exception e) {
            return "0";
        }
    }


    public static String getFullDay() {
        long currentSeconds = System.currentTimeMillis();//系统当前时间  1432525465208
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time_t = sdf.format(currentSeconds);
        return time_t;
    }

    public static String getCurrentHour() {
        long currentSeconds = System.currentTimeMillis();//系统当前时间
        SimpleDateFormat mh = new SimpleDateFormat("HH");
        String temp = mh.format(currentSeconds);
        return temp;
    }

    public static long getcurrentTime() {
        return System.currentTimeMillis();
    }


    /**
     * 测定两点间距离
     *
     * @param lat_a
     * @param lng_a
     * @param lat_b
     * @param lng_b
     * @return
     */
    public static double getDistanceFromXtoY(double lat_a, double lng_a, double lat_b, double lng_b) {
        double pk = (double) (180 / 3.14159);

        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000 * tt / 1000;  //转换成KM
    }

    public static Bitmap getViewBitmap(View addViewContent) {

        addViewContent.setDrawingCacheEnabled(true);

        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0,
                addViewContent.getMeasuredWidth(),
                addViewContent.getMeasuredHeight());

        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        return bitmap;
    }


    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static float px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (float) (pxValue / fontScale + 0.5f);
    }


    /**
     * 返回年月日
     *
     * @param time
     * @return
     */
    public static String formatDay(String time) {
        String arr[] = time.substring(0, 10).split("-");
        if (arr.length == 3) {
            return arr[0] + "年" + arr[1] + "月" + arr[2] + "日";
        }
        return "";
    }

    public static int changeTimeStr2Int(String timeStr) {
        try {
            String result = timeStr.replace("-", "");
            return Integer.parseInt(result);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 　　 * 取字符串的前toCount个字符
     * 　　 *
     * 　　 * @param str 被处理字符串
     * 　　 * @param toCount 截取长度
     * 　　 * @param more 后缀字符串
     * 　　 * @version 2004.11.24
     * 　　 * @author zhulx
     * 　　 * @return String
     *
     */
    public static String substringByByte(String str, int toCount, String more) throws Exception {
        int reInt = 0;
        String reStr = "";
        if (str == null) return "";
        char[] tempChar = str.toCharArray();
        for (int kk = 0; (kk < tempChar.length && toCount > reInt); kk++) {
            String s1 = str.valueOf(tempChar[kk]);
            byte[] b = s1.getBytes();
            reInt += b.length;
            reStr += tempChar[kk];
        }
        if (toCount == reInt || (toCount == reInt - 1))
            reStr += more;

        L.e("234==toCount=="+toCount+",reInt=="+reInt);

        int length = reStr.getBytes().length;
        if (length>30){
            return substringByByte(str,toCount-1,more);
        }

        return reStr;
    }

    /**
     * 将int类型转换为byte数组 *
     *
     * @param number
     * @return
     */
    public static byte[] int2ByteArray(int number) {
        return new byte[]{(byte) ((number >> 24) & 0xFF), (byte) ((number >> 16) & 0xFF), (byte) ((number >> 8) & 0xFF),
                (byte) (number & 0xFF)};

    }

    public static byte[] int2Byte(int number) {
        return new byte[]{
                (byte) (number & 0xFF)};
    }

    /**
     * 将Byte数组转换为int类型 *
     *
     * @param byteArray 长度必须是4
     * @return
     */
    public static int byteArray2Int(byte[] byteArray) {
        if (byteArray == null || byteArray.length != 4) {
            throw new IllegalArgumentException("The byte arrays length must be 4 !");
        }
        int value = 0;
        value += (byteArray[0] & 0x000000FF) << 24;
        value += (byteArray[1] & 0x000000FF) << 16;
        value += (byteArray[2] & 0x000000FF) << 8;
        value += (byteArray[3] & 0x000000FF) << 0;
        return value;
    }

    /**
     * 返回年月日时分秒
     *
     * @return
     */
    public static String getCurrentDay(SimpleDateFormat formater) {
//        SimpleDateFormat hmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formater.format(System.currentTimeMillis());
    }


    public static String display(Long timestamp) {
        SimpleDateFormat hmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return hmt.format(timestamp);
    }

    public static Long getTimestamp(String time) {

        if (isEmpty(time)){
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    //格式化显示时间
    public static String displayTime(Long timestamp, Long lastTimeStamp) {
//        Long timestamp = Long.parseLong(times);
        if (timestamp < 0) {
            return "";
        }
        SimpleDateFormat hmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time_t = hmt.format(System.currentTimeMillis());
        String arr[] = time_t.split(" ");
//        L.e("displayTime" + arr[0]);
        String zero_time = arr[0] + " 00:00:00";

        try {
            long time_zero = hmt.parse(zero_time).getTime();
            long currentSeconds = System.currentTimeMillis();//系统当前时间
            String timeStr = null;
            long currentStart = currentSeconds - currentSeconds % (24 * 60 * 60 * 1000);
            long timeGap = (currentStart - timestamp) / 1000;// 与当前凌晨时间相差秒数
//            L.e("timeGap" + timeGap);
            Long differ = null;
            if (lastTimeStamp != null) {
                differ = timestamp - lastTimeStamp;
            }

            if (timeGap <= 0)//今天
            {
                if (differ != null && differ < 1000 * 60 * 3) {
                    return "invisiable";
                }
                SimpleDateFormat hms = new SimpleDateFormat("HH:mm:ss");
//              timeStr = hm.format(timestamp);
                timeStr = hms.format(timestamp);
            } else if (timeGap > 0 && timeGap <= 24 * 60 * 60) {
                SimpleDateFormat hm = new SimpleDateFormat("HH:mm:ss");
//            timeStr = "昨天" + hm.format(timestamp);// 昨天
                timeStr = "昨天" + hm.format(timestamp);// 昨天
            } else if (timeGap > 24 * 60 * 60 && timeGap <= 7 * 24 * 60 * 60) {//一星期以内
                SimpleDateFormat ymd = new SimpleDateFormat("yy-MM-dd");
                SimpleDateFormat hm = new SimpleDateFormat("HH:mm:ss");
                String data_str = ymd.format(timestamp);
                String[] data = data_str.split("-");

                String time = hm.format(timestamp);
                // 星期三 21：33
                timeStr = getDayOfWeek(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2])) + time;
            } else {
                SimpleDateFormat hm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                timeStr = hm.format(timestamp);
            }
            return timeStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取日期具体的星期数
     *
     * @param year  年份 例如 15 (切记是2位)
     * @param month 月份
     * @param day   天数
     * @return
     */
    public static String getDayOfWeek(int year, int month, int day) {

        int c = year / 100;
        int w = year + (year / 4) + (c / 4) - 2 * c + (26 * (month + 1)) / 10 + day - 1;//泰勒公式
        if (w < 0) {
            w += 7;
        } else {
            w %= 7;
        }

        String mDayofWeek = "";
        switch (w) {
            case 0:
                mDayofWeek = "星期日";
                break;
            case 1:
                mDayofWeek = "星期一";
                break;
            case 2:
                mDayofWeek = "星期二";
                break;
            case 3:
                mDayofWeek = "星期三";
                break;
            case 4:
                mDayofWeek = "星期四";
                break;
            case 5:
                mDayofWeek = "星期五";
                break;
            case 6:
                mDayofWeek = "星期六";
                break;
        }
        return mDayofWeek;
    }

    public static void ExitApp(Context context) {
//        DateSharePreference.addExitCount();
//        DateAcitivityManager.getInstance().killAllActivity();
//        android.os.Process.killProcess(android.os.Process.myPid());
        //============== 改为注入后台 ==================//
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //如果是服务里调用，必须加入new task标识
        intent.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(intent);

    }

    /**
     * 判断是否有网络连接
     *
     * @return
     */
    public static boolean isNet(Context context) {
        boolean flag = false;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        int leng = 0;
        NetworkInfo[] arrayOfNetworkInfo = manager.getAllNetworkInfo();
        if (arrayOfNetworkInfo != null) {
            leng = arrayOfNetworkInfo.length;
        }
        for (int i = 0; i < leng; i++) {
            if (arrayOfNetworkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * make true current connect service is wifi
     *
     * @param mContext
     * @return
     */
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    public static void recordSendMsgError(Context context, int message_type) {
        recordSendMsgError(context, message_type, null);
    }


    public static void recordSendMsgError(Context context, int message_type, String errorMsg) {
        recordSendMsgError(context, String.valueOf(message_type), errorMsg);
    }

    public static void recordSendMsgError(Context context, String message_type, String errorMsg) {
        HashMap<String, String> hashMap = new HashMap();
        hashMap.put("type", message_type);
        if (!CMethod.isEmptyOrZero(errorMsg)) {
            hashMap.put("err", errorMsg);
        }
//        MobclickAgent.onEvent(context, UMeventId.ERROR_SEND_MSG, hashMap);
    }


    /**
     * 检测Sdcard是否存在
     *
     * @return
     */
    public static boolean isExitsSdcard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    public static int calculateMemoryCacheSize(Application application) {
        ActivityManager am = (ActivityManager) application.getSystemService(application.ACTIVITY_SERVICE);
        boolean largeHeap = (application.getApplicationInfo().flags & FLAG_LARGE_HEAP) != 0;
        int memoryClass = am.getMemoryClass();
        if (largeHeap && SDK_INT >= HONEYCOMB) {
            memoryClass = ActivityManagerHoneycomb.getLargeMemoryClass(am);
        }
        // Target ~15% of the available heap.
        return 1024 * 1024 * memoryClass / 5;//默认是1/7 这里改为1/5是为了减少回收频率
    }

    @TargetApi(HONEYCOMB)
    private static class ActivityManagerHoneycomb {
        static int getLargeMemoryClass(ActivityManager activityManager) {
            return activityManager.getLargeMemoryClass();
        }
    }

    /**
     * 判断是否手机号
     *
     * @param mobile
     * @return
     */
    public static boolean isPhoneNumber(String mobile) {

        String phoneRegex = "1[0-9]{10}";// 手机号格式正则表达式验证

        Pattern regex;
        Matcher matcher;

        regex = Pattern.compile(phoneRegex);
        matcher = regex.matcher(mobile);

        if (matcher.matches()) {
            return true;
        }

        return false;
    }

    /**
     * 检验输入的密码是否为符合要求的
     *
     * @param password
     * @return 符合要求的为true
     */
    public static boolean isEffectivePassword(String password) {
        String str = "^[A-Za-z0-9]{6,16}+$";//只能为26个英文字母或数字组成的字符串
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(password);
        return m.matches();
    }

    public static void downloadVoiceFile(final Context context, final Message msg, final VoiceDownloadListener listener) {
        new Thread() {
            @Override
            public void run() {
                DownloadHelper downloadHelper = new DownloadHelper();
//                String path = AppConstants.QINIU_MEDIA_DOMAIN + msg.content;
                String path = msg.content;
                L.e("downloadVoiceFile --- " + msg.content);

                final String filename = getNetfilePath(context, path);
                try {
                    downloadHelper.download(path, filename, new DownloadHelper.OnProgress() {
                        @Override
                        public void update(int progress) {
                            if (progress == 100) {
                                listener.onSuccess(filename);

                            }
                        }

                        @Override
                        public void onFail() {
                            listener.onFailure();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailure();
                }
            }
        }.start();
    }

    public static String getNetfilePath(Context context, String directoryOrUrl) {
        String md5 = Md5Util.string2MD5(directoryOrUrl);
        String cacheDir = CacheUtils.getExternalCacheDir(context);
        return cacheDir + md5 + suffixes;
    }

    /**
     * 段时间内重复点击
     *
     * @version 2013 Mar 8, 2013 6:31:23 PM
     * @author chunlongyuan chunlongyuan@gmail.com
     * @return
     * @since 2013 Mar 8, 2013 6:31:23 PM
     */
    private static long lastClickTime;

    public static boolean isFastDoubleClick(long maxTime) {

        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        L.e("1234===timeD===" + timeD);
        if (0 < timeD && timeD < maxTime) {
            return true;
        }

        lastClickTime = time;
        return false;
    }

    public static boolean isFastDoubleClick() {

        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;

        if (0 < timeD && timeD < 600) {
            return true;
        }

        lastClickTime = time;
        return false;
    }

    /**
     * <对字符串进行Des加密，将字符串转化为字节数组解密>
     *
     * @return
     */
    public static String desCrypto(String encryptString, String encryptKey) {
        try {
//            SecureRandom random = new SecureRandom();
//            DESKeySpec desKey = new DESKeySpec(password.getBytes());
//            //创建一个密匙工厂，然后用它把DESKeySpec转换成
//            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
//            SecretKey securekey = keyFactory.generateSecret(desKey);
//            //Cipher对象实际完成加密操作
//            Cipher cipher = Cipher.getInstance("DES");
//            //用密匙初始化Cipher对象
//            cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
//            //现在，获取数据并加密
//            //正式执行加密操作
//            return cipher.doFinal(datasource);

            SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);//Cipher.ENCRYPT_MODE（加密标识）
            byte[] encryptedData = cipher.doFinal(encryptString.getBytes("UTF-8"));//DES加密
            return Base64Tools.base64Encode(encryptedData);//Base64加密生成在Http协议中传输的字符串

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @param decryptString 待解密的字符串
     * @param decryptKey    生成秘钥的字符串，例如：机器码
     * @return 解密后的字符串
     */
    public static String decrypt(String decryptString, String decryptKey) {
        byte[] byteMi = new byte[0];
        try {
            byteMi = Base64Tools.base64Decode(decryptString);
            SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptData = cipher.doFinal(byteMi);
            return new String(decryptData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 对字符串进行加密
     *
     * @param src
     * @param password
     * @return
     */
    public static byte[] decrypt(byte[] src, String password) {

        try {
            // DES算法要求有一个可信任的随机数源
            SecureRandom random = new SecureRandom();
            // 创建一个DESKeySpec对象
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            // 创建一个密匙工厂
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // 将DESKeySpec对象转换成SecretKey对象
            SecretKey securekey = keyFactory.generateSecret(desKey);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance("DES");
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, random);
            // 真正开始解密操作
            return cipher.doFinal(src);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }


//    public static byte[] desCrypto(byte[] datasource, String password)
//
//    　　{
//
//        　　try
//
//        　　{
//
//            　　SecureRandom random = new SecureRandom();
//
//            　　DESKeySpec desKey = new DESKeySpec(password.getBytes());
//
//            　　//创建一个密匙工厂，然后用它把DESKeySpec转换成
//
//            　　SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(“DES”);
//
//            　　SecretKey securekey = keyFactory.generateSecret(desKey);
//
//            　　//Cipher对象实际完成加密操作
//
//            　　Cipher cipher = Cipher.getInstance(“DES”);
//
//            　　//用密匙初始化Cipher对象
//
//            　　cipher.init(Cipher.ENCRYPT_MODE, securekey, random);
//
//            　　//现在，获取数据并加密
//
//            　　//正式执行加密操作
//
//            　　return cipher.doFinal(datasource);
//
//            　　}
//
//        　　catch (Throwable e)
//
//        　　{
//
//            　　e.printStackTrace();
//
//            　　}
//
//        　　return null;
//
//        　　}

    /**
     * 应用是否在后台
     *
     * @param context
     * @return
     */
    public static boolean isBackground(Context context) {
//        PreferencesUtils preferencesUtils = new PreferencesUtils(context);
//        boolean isScreenLocked = preferencesUtils.getBoolean("IS_SCREEN_LOCK", false);
        boolean isScreenLocked = !isScreenOn(context);
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = activityManager.getRunningTasks(5).get(0).topActivity.getPackageName();
        L.e(isScreenLocked + "<------>" + packageName);
        if (isScreenLocked || !packageName.equals(context.getPackageName())) {
            return true;
        }
        return false;
    }

    public static boolean isScreenOn(Context context) {
        Method mReflectScreenState;
        try {
            mReflectScreenState = PowerManager.class.getMethod("isScreenOn", new Class[]{});
            PowerManager pm = (PowerManager) context.getSystemService(Activity.POWER_SERVICE);
            boolean isScreenOn = (Boolean) mReflectScreenState.invoke(pm);
            return isScreenOn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

//
//    public static void isOpenHomeMSGReceiver(Context mContext, Boolean isOpen, int index) {
//
//        L.e("------ isOpenHomeMSGReceiver ------"+index+"------"+isOpen);
//
//
//        Intent intent = new Intent();
//        intent.setAction(BroadcastConstants.ACTION_HOME_MSG_NEED_RECEIVER);
//        if (isOpen) {
//            intent.putExtra(IntentAction.HOME_MSG_RECEIVER_ORDER, IntentAction.ACTION_OPEN_HOME_MSG_ABLE);
//        } else {
//            intent.putExtra(IntentAction.HOME_MSG_RECEIVER_ORDER, IntentAction.ACTION_OPEN_HOME_MSG_UNABLE);
//        }
//        mContext.getApplicationContext().sendBroadcast(intent);
//    }

    //3DESECB加密,key必须是长度大于等于 3*8 = 24 位
    public static String encryptThreeDESECB(String src, String key) {
        try {
            DESedeKeySpec dks = new DESedeKeySpec(key.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            SecretKey securekey = keyFactory.generateSecret(dks);

            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, securekey);
            byte[] b = cipher.doFinal(src.getBytes("UTF-8"));

            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(b).replaceAll("\r", "").replaceAll("\n", "");
        }catch (Exception e){
            e.printStackTrace();

            return src;
        }


    }


    //3DESECB解密,key必须是长度大于等于 3*8 = 24 位
    public static String decryptThreeDESECB(String src, String key) {

        try {
            //--通过base64,将字符串转成byte数组
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] bytesrc = decoder.decodeBuffer(src);
            //--解密的key
            DESedeKeySpec dks = new DESedeKeySpec(key.getBytes("UTF-8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
            SecretKey securekey = keyFactory.generateSecret(dks);

            //--Chipher对象解密
            Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, securekey);
            byte[] retByte = cipher.doFinal(bytesrc);

            return new String(retByte);
        } catch (Exception e) {
            e.printStackTrace();
            return src;
        }

    }

    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    public static boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    public static BDPosition bd_encrypt(double gd_lat, double gd_lon) {
        BDPosition position = new BDPosition();
        double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
        double x = gd_lon, y = gd_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        position.setLon(z * Math.cos(theta) + 0.0065);
        position.setLat(z * Math.sin(theta) + 0.006);
        return position;

    }


    /**
     * 弹出通知栏
     *
     * @param context
     */
    public static void showSystemNotification(Context context, MsgMember msgMember) {
        Intent intent = new Intent(context, SystemMsgActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(IntentAction.OPEN_SPLASH_TAB, "notification");
        intent.setAction(IntentAction.GET_SYS_MSG_OK);
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(MsgMember.KEY, msgMember);//传递宝贝信息
        intent.putExtras(mBundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        final NotificationUtils notificationUtils = new NotificationUtils(context, pendingIntent, title, title, content_msg, head_url, index, sex);
        final NotificationUtils notificationUtils = new NotificationUtils(context, pendingIntent, "华英智联", "您收到新的系统消息", "请尽快查看", NotificationUtils.SYSTEM_MESSAGE_ID);

    }


    /**
     * 弹出通知栏
     *
     * @param context
     * @param title
     * @param content
     */
    public static void showChildNotification(Context context, MsgMember msgMember, String title, String content) {
        Intent intent = new Intent(context, ChildMsgActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(IntentAction.OPEN_SPLASH_TAB, "notification");
        intent.setAction(IntentAction.GET_SYS_MSG_OK);
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(MsgMember.KEY, msgMember);//传递宝贝信息
        intent.putExtras(mBundle);
        intent.putExtra("from_tag", "msgcenter");

        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        final NotificationUtils notificationUtils = new NotificationUtils(context, pendingIntent, title, title, content_msg, head_url, index, sex);
        final NotificationUtils notificationUtils = new NotificationUtils(context, pendingIntent, content, title, content,parseInt(msgMember.getUser_id()));

    }

    /**
     * 保存文件
     *
     * @param bm
     * @throws IOException
     */
    public static void saveFile(Bitmap bm, String urlPath, String type) throws IOException {
        String path = getSDPath() + "/" + type + "/";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + getFileName(urlPath));
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
    }

    private static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir.toString();
    }

    /**
     * 获取bitmap
     *
     * @param filePath
     * @return
     */
    public static Bitmap getBitmaptoFile(String filePath, String type) {
        String path = getSDPath() + "/" + type + "/" + getFileName(filePath);
        Bitmap bitmap = null;
        if (fileIsExists(filePath, type)) {
            bitmap = BitmapFactory.decodeFile(path);
        }
        return bitmap;

    }

    /**
     * 文件是否存在
     *
     * @param path
     * @return
     */
    private static boolean fileIsExists(String path, String type) {
        try {
            File f = new File(getSDPath() + "/" + type + "/" + getFileName(path));
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }

    /**
     * 文件名的获取
     *
     * @param url
     * @return
     */
    private static String getFileName(String url) {
        // 从路径中获取
        if (url != null || !"".equals(url)) {
            url = url.substring(url.lastIndexOf("/") + 1);
        }
        return url;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    /**
     * 域名转化为IP
     * @param host
     * @return
     */
    public static String GetInetAddress(String  host){
        String IPAddress = "";
        InetAddress ReturnStr1 = null;
        try {
            ReturnStr1 = java.net.InetAddress.getByName(host);
            IPAddress = ReturnStr1.getHostAddress();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return  IPAddress;
        }
        return IPAddress;
    }

}
