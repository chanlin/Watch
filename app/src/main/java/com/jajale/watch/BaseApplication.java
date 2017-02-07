package com.jajale.watch;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.igexin.sdk.PushManager;
import com.jajale.watch.activity.HomeSecActivity;
import com.jajale.watch.dao.SqliteHelper;
import com.jajale.watch.entity.AppInfo;
import com.jajale.watch.entity.SPKeyConstants;
import com.jajale.watch.entitydb.User;
import com.jajale.watch.entityno.UMeventId;
import com.jajale.watch.helper.ConfigHelper;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.ChannelUtil;
import com.jajale.watch.utils.DisplayUtil;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.PhoneSPUtils;
import com.jajale.watch.utils.StandardGrowTool;
import com.jajale.watch.utils.ThreadUtils;
import com.jajale.watch.utils.UserUtils;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

import org.wlf.filedownloader.FileDownloadConfiguration;
import org.wlf.filedownloader.FileDownloader;

import java.io.File;


/**
 * Created by athena on 2015/11/11.
 * Email: lizhiqiang@bjjajale.com
 */
public class BaseApplication extends Application implements AMapLocationListener {

    private static Context mContext;
    private static User mUserInfo;
    private static HomeSecActivity mHomeActivity;

    //用于用户数据的管理
    private static SqliteHelper mSqliteHelper;

//    public LocationClient mLocationClient;

    public static AMapLocationClient mapClient;
    private AMapLocationClientOption options;

//    public static boolean mapBroadCastEnable = false ;

    private int present_position = 0;

    public int getPresent_position() {
        return present_position;
    }

    public void setPresent_position(int present_position) {
        this.present_position = present_position;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        initDownLoad();

//        // for umeng start //
        AnalyticsConfig.setAppkey("5641d04ee0f55aea5900138e");
        String channel = ChannelUtil.getChannel(mContext, "8612#jajale_0006");
        AnalyticsConfig.setChannel(channel);
//        if (CMethod.isEmptyOrZero(channel)) {
//            AnalyticsConfig.setChannel("2100000");
//        } else {
//            AnalyticsConfig.setChannel(channel);
//        }
        MobclickAgent.setDebugMode(true);
//        // for umeng end //

        AppInfo.init(this);

        PushManager.getInstance().initialize(this.getApplicationContext());

        String appName = CMethod.getAppNameByPid(this);
        if (!AppInfo.getInstace().getPackageName().equals(appName)) {
            return;
        }

//        SocketTools socketTools = new SocketTools(getApplicationContext(), "004802365279");//换成watchID
//        socketTools.login();


        ThreadUtils.getPool().execute(new Runnable() {
            @Override
            public void run() {
                int memoryCacheSize = CMethod.calculateMemoryCacheSize(BaseApplication.this);
                Picasso picasso = new Picasso.Builder(BaseApplication.this).memoryCache(new LruCache(memoryCacheSize)).build();
                Picasso.setSingletonInstance(picasso);
                if (!PublicSwitch.isLog) {
                    CrashHandler crashHandler = CrashHandler.getInstance();
                    crashHandler.init(getApplicationContext());
                }
                /* 通过接口 获取 通用的应用配置 config */
                new ConfigHelper(BaseApplication.this).start();
            }
        });

//        initLocationOptions();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
//        SDKInitializer.initialize(this);


        final PhoneSPUtils phoneSPUtils = new PhoneSPUtils(this);
        phoneSPUtils.save(SPKeyConstants.GET_NEW_MSG_TIME_STAMP, Long.parseLong("-1"));

        boolean isAppStarted = phoneSPUtils.getBoolean(SPKeyConstants.IS_APP_STARTED, false);
        if (!isAppStarted) {
            MobclickAgent.onEvent(this, UMeventId.UMENG_ACTIVATION_NUMBER);
        }

        StandardGrowTool.copyGrowDB(this);


//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //测试修改
////                String s = CMethod.GetInetAddress(AppConstants.EXTRA_SOCKET_HOST);
//                String s = AppConstants.SOCKET_IP;
//                if (!CMethod.isEmpty(s)) {
//                    L.e("socket==通过域名--" + AppConstants.EXTRA_SOCKET_HOST + "获取的IP==" + s);
//                    phoneSPUtils.save(SPKeyConstants.SP_SOCKET_IP, s);
//                }
//            }
//        }).start();


        DisplayUtil.init(getApplicationContext());//获取屏幕参数
    }



    public  void initDownLoad() {
        // 1、创建Builder
        FileDownloadConfiguration.Builder builder = new FileDownloadConfiguration.Builder(this);

        // 2.配置Builder
        // 配置下载文件保存的文件夹
        builder.configFileDownloadDir(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator +
                "FileDownloader");



        // 配置同时下载任务数量，如果不配置默认为2
        builder.configDownloadTaskSize(3);
        // 配置失败时尝试重试的次数，如果不配置默认为0不尝试
        builder.configRetryDownloadTimes(5);
        // 开启调试模式，方便查看日志等调试相关，如果不配置默认不开启
        builder.configDebugMode(true);
        // 配置连接网络超时时间，如果不配置默认为15秒
        builder.configConnectTimeout(25000);// 25秒

        // 3、使用配置文件初始化FileDownloader
        FileDownloadConfiguration configuration = builder.build();
        FileDownloader.init(configuration);
    }


    public static Context getContext() {
        return mContext;
    }


    public static SqliteHelper getBaseHelper() {
        return mSqliteHelper;
    }

    public static void setBaseHelper(SqliteHelper helper) {
        mSqliteHelper = helper;
    }


//    private void initLocationOptions() {
//        // ------ for baidu map start ------//
//
////        LocationClientOption option = new LocationClientOption();
//////        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);//设置定位模式
////        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
////        option.setCoorType("gcj02");//返回的定位结果是百度经纬度，默认值gcj02
////        int span = 1000 * 60 * 30;
////        option.setScanSpan(0);//
////        option.setIsNeedAddress(true);//false为不需要详细的街道地址
////        option.setAddrType("all");
////        option.setOpenGps(false);
////        mLocationClient = new LocationClient(this, option);
////        mLocationClient.registerLocationListener(new LocationListener());
////        mLocationClient.start();
//
//        // ------ for baidu map end ------//
//
//
//        // for gaode map start ------//
//
//
//        mapClient = new AMapLocationClient(getApplicationContext());
//        options = new AMapLocationClientOption();
//        options.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        options.setNeedAddress(true);
////        options.setKillProcess(true);//退出时是否杀死Service
//        options.setInterval(PublicSwitch.LOCATION_CALLBACK_FREQUENCY_HIGH);//设置请求时间间隔 单位毫秒
//
//        mapClient.setLocationOption(options);
//        mapClient.setLocationListener(this);
//        mapClient.startLocation();
//
//
//        // for gaode map end   ------//
//
//
//    }


//    public class LocationListener implements BDLocationListener {
//
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            //Receive Location
//            L.e("BaseApplication", location.getLatitude() + "<--->" + location.getLongitude());
//            AppInfo.getInstace().setLatitude(location.getLatitude());
//            AppInfo.getInstace().setLongitude(location.getLongitude());
////            CMethod.killBaiduProcess(DateApplication.this);
//        }
//
//    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        //高德地图的回调
        //116.423205,40.037556
        L.e("333333333333333333333__lat_base_gaode==" + aMapLocation.getLatitude() + "__lon_base_gaode===" + aMapLocation.getLongitude());
        L.e("GaoDeMap===>>>" + aMapLocation.getAddress() + "<====>" + aMapLocation.getLongitude() + "<====>" + aMapLocation.getLatitude());
//        if (PublicSwitch.isLog) {
//            LocationUtils.saveMsg2DB(aMapLocation.getLongitude() + "", aMapLocation.getLatitude() + "", aMapLocation.getAddress());
//        }

        if (aMapLocation.getLatitude() != 0) {
            L.e("GaoDeMap===============================000000000000");
            options.setInterval(PublicSwitch.LOCATION_CALLBACK_FREQUENCY_LOW);
        }
        L.e("GaoDeMap---isFirst==" + PublicSwitch.isPositionFirst);
//        if (!PublicSwitch.isPositionFirst) {
//            TextData textData = getTestLatLng(num);
//
//            AppInfo.getInstace().setLatitude(textData.lat);
//            AppInfo.getInstace().setLongitude(textData.lon);
//            AppInfo.getInstace().setAddrStr(textData.address);
//            Intent intent = new Intent();
//            intent.setAction(BroadcastConstants.POSITION_REFRESH_RECEIVER);
//            intent.putExtra("position_lat", textData.lat);
//            intent.putExtra("position_lon", textData.lon);
//            sendBroadcast(intent);
//        } else {
//            PublicSwitch.isPositionFirst = false;
//            L.e("GaoDeMap---定位第一条略过");
//        }
        if (!PublicSwitch.isPositionFirst) {
            AppInfo.getInstace().setLatitude(aMapLocation.getLatitude());
            AppInfo.getInstace().setLongitude(aMapLocation.getLongitude());
            AppInfo.getInstace().setAddrStr(aMapLocation.getAddress());
            Intent intent = new Intent();
            intent.setAction(BroadcastConstants.POSITION_REFRESH_RECEIVER);
            intent.putExtra("position_lat", aMapLocation.getLatitude());
            intent.putExtra("position_lon", aMapLocation.getLongitude());
//            if (mapBroadCastEnable){
            sendBroadcast(intent);
//            }


        } else {
            PublicSwitch.isPositionFirst = false;
            L.e("定位第一条略过");
        }
//                num=num+1;
    }


//    private int num=0;
//    private TextData getTestLatLng(int position) {
//
//        String jsonString = "[{\"address\":\"北京市朝阳区北苑路靠近北辰泰岳\",\"lat\":\"40.031664\",\"lon\":\"116.416847\"},{\"address\":\"北京市朝阳区北苑路靠近北辰泰岳\",\"lat\":\"40.031664\",\"lon\":\"116.416847\"},{\"address\":\"北京市朝阳区北苑路靠近北辰泰岳\",\"lat\":\"40.03178\",\"lon\":\"116.416784\"},{\"address\":\"北京市朝阳区北苑路靠近北辰泰岳\",\"lat\":\"40.03178\",\"lon\":\"116.416784\"},{\"address\":\"北京市朝阳区北苑路靠近北辰泰岳\",\"lat\":\"40.03178\",\"lon\":\"116.416784\"},{\"address\":\"北京市朝阳区北苑路靠近北辰泰岳\",\"lat\":\"40.031664\",\"lon\":\"116.416847\"},{\"address\":\"北京市朝阳区北苑路靠近北辰新纪元2\",\"lat\":\"40.031372\",\"lon\":\"116.417055\"},{\"address\":\"北京市朝阳区北苑路靠近北辰新纪元2\",\"lat\":\"40.031372\",\"lon\":\"116.417055\"},{\"address\":\"北京市朝阳区红军营南路靠近卡尔安娜国际儿童会馆\",\"lat\":\"40.031326\",\"lon\":\"116.415261\"},{\"address\":\"北京市朝阳区北苑路靠近北辰新纪元2\",\"lat\":\"40.03113\",\"lon\":\"116.41616\"},{\"address\":\"北京市朝阳区北苑路靠近北辰新纪元2\",\"lat\":\"40.03113\",\"lon\":\"116.41616\"},{\"address\":\"北京市朝阳区红军营南路靠近傲城融富中心\",\"lat\":\"40.03147\",\"lon\":\"116.414807\"},{\"address\":\"北京市朝阳区红军营南路靠近傲城融富中心\",\"lat\":\"40.03147\",\"lon\":\"116.414807\"},{\"address\":\"\",\"lat\":\"40.03032701982222\",\"lon\":\"116.41549952042217\"},{\"address\":\"\",\"lat\":\"40.031134776633294\",\"lon\":\"116.41483025606016\"},{\"address\":\"北京市朝阳区红军营南路靠近卡尔安娜国际儿童会馆\",\"lat\":\"40.031239579308696\",\"lon\":\"116.41527581699508\"},{\"address\":\"\",\"lat\":\"40.031323\",\"lon\":\"116.414991\"},{\"address\":\"\",\"lat\":\"40.03131260606323\",\"lon\":\"116.41526467106385\"},{\"address\":\"\",\"lat\":\"40.03132146102462\",\"lon\":\"116.41524872954557\"},{\"address\":\"\",\"lat\":\"40.031286903201114\",\"lon\":\"116.41523506167894\"},{\"address\":\"\",\"lat\":\"40.03127097189634\",\"lon\":\"116.41520148526362\"},{\"address\":\"\",\"lat\":\"40.031265332548145\",\"lon\":\"116.41519818609943\"},{\"address\":\"\",\"lat\":\"40.031262903146015\",\"lon\":\"116.41519563697929\"},{\"address\":\"\",\"lat\":\"40.03126666445726\",\"lon\":\"116.41519162928401\"},{\"address\":\"\",\"lat\":\"40.03126579556048\",\"lon\":\"116.41518755105707\"},{\"address\":\"\",\"lat\":\"40.03127600714331\",\"lon\":\"116.41503953984717\"},{\"address\":\"\",\"lat\":\"40.03137115520109\",\"lon\":\"116.41487923318003\"},{\"address\":\"\",\"lat\":\"40.031394319015924\",\"lon\":\"116.41467862374706\"},{\"address\":\"\",\"lat\":\"40.03139994947014\",\"lon\":\"116.41467793472941\"},{\"address\":\"\",\"lat\":\"40.031384875768914\",\"lon\":\"116.4146893979435\"},{\"address\":\"\",\"lat\":\"40.03131251309395\",\"lon\":\"116.41472457389804\"},{\"address\":\"\",\"lat\":\"40.03118052530524\",\"lon\":\"116.414729625648\"},{\"address\":\"\",\"lat\":\"40.031103785226776\",\"lon\":\"116.41471546246316\"},{\"address\":\"\",\"lat\":\"40.03096709507525\",\"lon\":\"116.41484286010883\"},{\"address\":\"\",\"lat\":\"40.03093778950976\",\"lon\":\"116.41485839968375\"},{\"address\":\"\",\"lat\":\"40.031016278786296\",\"lon\":\"116.41476206154168\"},{\"address\":\"\",\"lat\":\"40.03095940797562\",\"lon\":\"116.41479263125498\"},{\"address\":\"\",\"lat\":\"40.03096226683377\",\"lon\":\"116.41479749946886\"},{\"address\":\"\",\"lat\":\"40.03096075626211\",\"lon\":\"116.41479938845674\"},{\"address\":\"\",\"lat\":\"40.03095371429151\",\"lon\":\"116.41480555489466\"},{\"address\":\"\",\"lat\":\"40.030952884126954\",\"lon\":\"116.4148060245875\"},{\"address\":\"\",\"lat\":\"40.03095041352343\",\"lon\":\"116.4148078534841\"},{\"address\":\"\",\"lat\":\"40.03095012366486\",\"lon\":\"116.41480726370777\"},{\"address\":\"\",\"lat\":\"40.03094969380206\",\"lon\":\"116.41480666391878\"},{\"address\":\"\",\"lat\":\"40.0309490938017\",\"lon\":\"116.4148065538941\"},{\"address\":\"\",\"lat\":\"40.03087069178694\",\"lon\":\"116.41479963739204\"},{\"address\":\"北京市朝阳区北苑路靠近卡尔安娜国际儿童会馆\",\"lat\":\"40.03073577153897\",\"lon\":\"116.41473724823302\"},{\"address\":\"\",\"lat\":\"40.030294\",\"lon\":\"116.416184\"},{\"address\":\"\",\"lat\":\"40.030576141888076\",\"lon\":\"116.41470589242915\"},{\"address\":\"\",\"lat\":\"40.03059394323769\",\"lon\":\"116.41474248870024\"},{\"address\":\"\",\"lat\":\"40.03045669624308\",\"lon\":\"116.41478178485407\"},{\"address\":\"\",\"lat\":\"40.03071417676978\",\"lon\":\"116.41467444281828\"},{\"address\":\"北京市朝阳区红军营南路靠近卡尔安娜国际儿童会馆\",\"lat\":\"40.03063287150648\",\"lon\":\"116.41475639737489\"},{\"address\":\"\",\"lat\":\"40.030735\",\"lon\":\"116.414582\"},{\"address\":\"\",\"lat\":\"40.03093938105281\",\"lon\":\"116.4150380495733\"},{\"address\":\"\",\"lat\":\"40.03080580459607\",\"lon\":\"116.41507411698225\"},{\"address\":\"\",\"lat\":\"40.03063359269089\",\"lon\":\"116.41501393326315\"},{\"address\":\"\",\"lat\":\"40.03062678192408\",\"lon\":\"116.41501552171867\"},{\"address\":\"\",\"lat\":\"40.03061989083365\",\"lon\":\"116.41501827963691\"},{\"address\":\"\",\"lat\":\"40.03063876728389\",\"lon\":\"116.41499802105747\"},{\"address\":\"\",\"lat\":\"40.03065456965883\",\"lon\":\"116.41499213562612\"},{\"address\":\"\",\"lat\":\"40.030642878037334\",\"lon\":\"116.4149959924699\"},{\"address\":\"\",\"lat\":\"40.03064463839006\",\"lon\":\"116.41499501312443\"},{\"address\":\"\",\"lat\":\"40.030644438516354\",\"lon\":\"116.41499451332484\"},{\"address\":\"\",\"lat\":\"40.030645408735175\",\"lon\":\"116.41499388372593\"},{\"address\":\"\",\"lat\":\"40.03065477998189\",\"lon\":\"116.41499098616833\"},{\"address\":\"\",\"lat\":\"40.03065737068763\",\"lon\":\"116.41498885744005\"},{\"address\":\"\",\"lat\":\"40.0306596412362\",\"lon\":\"116.4149872484391\"},{\"address\":\"\",\"lat\":\"40.03069731033395\",\"lon\":\"116.41496050501446\"},{\"address\":\"\",\"lat\":\"40.03072278479585\",\"lon\":\"116.41494861343304\"},{\"address\":\"\",\"lat\":\"40.030717883151354\",\"lon\":\"116.41495381051281\"},{\"address\":\"\",\"lat\":\"40.03071466195296\",\"lon\":\"116.41495765839933\"},{\"address\":\"\",\"lat\":\"40.030713680657676\",\"lon\":\"116.41496226621719\"},{\"address\":\"\",\"lat\":\"40.03071232818896\",\"lon\":\"116.41497113208032\"},{\"address\":\"\",\"lat\":\"40.030717206976554\",\"lon\":\"116.41497648027539\"},{\"address\":\"\",\"lat\":\"40.03071862595489\",\"lon\":\"116.41498049864487\"},{\"address\":\"\",\"lat\":\"40.03072098502464\",\"lon\":\"116.41498434720405\"},{\"address\":\"\",\"lat\":\"40.030724784087994\",\"lon\":\"116.41498847581175\"},{\"address\":\"\",\"lat\":\"40.030754344343315\",\"lon\":\"116.41524483236869\"},{\"address\":\"\",\"lat\":\"40.03093767131331\",\"lon\":\"116.41535675240388\"},{\"address\":\"\",\"lat\":\"40.03115370474996\",\"lon\":\"116.41545019450241\"},{\"address\":\"\",\"lat\":\"40.03119833003957\",\"lon\":\"116.41554147629384\"},{\"address\":\"\",\"lat\":\"40.03126870173438\",\"lon\":\"116.41561439965365\"},{\"address\":\"\",\"lat\":\"40.031314117728165\",\"lon\":\"116.41573411673609\"},{\"address\":\"\",\"lat\":\"40.03120333071335\",\"lon\":\"116.41602770649514\"},{\"address\":\"\",\"lat\":\"40.03126830789202\",\"lon\":\"116.4162624635233\"},{\"address\":\"\",\"lat\":\"40.0312483512509\",\"lon\":\"116.41624926797313\"},{\"address\":\"\",\"lat\":\"40.03135667517268\",\"lon\":\"116.41631411725587\"},{\"address\":\"\",\"lat\":\"40.03144026117433\",\"lon\":\"116.41621721798025\"},{\"address\":\"\",\"lat\":\"40.031455905150715\",\"lon\":\"116.41641868417041\"},{\"address\":\"\",\"lat\":\"40.031399707819546\",\"lon\":\"116.41625188502115\"},{\"address\":\"\",\"lat\":\"40.0314569367311\",\"lon\":\"116.41607910114878\"},{\"address\":\"\",\"lat\":\"40.031465537723555\",\"lon\":\"116.41613941126424\"},{\"address\":\"北京市朝阳区北苑路靠近北辰新纪元2\",\"lat\":\"40.031283\",\"lon\":\"116.416781\"},{\"address\":\"北京市朝阳区北苑路靠近北辰新纪元2\",\"lat\":\"40.031244\",\"lon\":\"116.416784\"},{\"address\":\"北京市朝阳区北苑路靠近北辰新纪元2\",\"lat\":\"40.031244\",\"lon\":\"116.416784\"},{\"address\":\"北京市朝阳区北苑路靠近北辰新纪元2\",\"lat\":\"40.031244\",\"lon\":\"116.416784\"},{\"address\":\"北京市朝阳区北苑路靠近北辰新纪元2\",\"lat\":\"40.031247\",\"lon\":\"116.416796\"},{\"address\":\"北京市朝阳区北苑路靠近北辰新纪元2\",\"lat\":\"40.031102\",\"lon\":\"116.416715\"},{\"address\":\"北京市朝阳区红军营南路靠近北辰新纪元2\",\"lat\":\"40.031763\",\"lon\":\"116.416008\"},{\"address\":\"北京市朝阳区北苑路靠近北辰泰岳\",\"lat\":\"40.031664\",\"lon\":\"116.416847\"},{\"address\":\"北京市朝阳区北苑路靠近北辰泰岳\",\"lat\":\"40.031664\",\"lon\":\"116.416847\"},{\"address\":\"北京市朝阳区北苑路靠近北辰新纪元2\",\"lat\":\"40.031352\",\"lon\":\"116.417059\"}]";
//        Gson gson = new Gson();
//        JsonParser parser = new JsonParser();
//        JsonArray jsonArray = parser.parse(jsonString).getAsJsonArray();
//        List<TextData> lcs = new ArrayList<TextData>();
//        for (JsonElement obj : jsonArray) {
//            TextData cse = gson.fromJson(obj, TextData.class);
//            lcs.add(cse);
//        }
//        return lcs.get(position);
//    }
//
//
//    class  TextData{
//
//        private String address;
//        private double lat;
//        private double lon;
//
//        public String getAddress() {
//            return address;
//        }
//
//        public void setAddress(String address) {
//            this.address = address;
//        }
//    }

    public static User getUserInfo() {
        if (mUserInfo == null) {

            mUserInfo = UserUtils.getUserInfo();
        }
        return mUserInfo;
    }

    public static void setUserInfo(User userInfo) {
        mUserInfo = userInfo;
    }


    public static HomeSecActivity getHomeActivity() {
        return mHomeActivity;
    }

    public static void setHomeActivity(HomeSecActivity homeActivity) {
        mHomeActivity = homeActivity;
    }

    public static AMapLocationClient getMapClient() {
        if (mapClient == null) {
            mapClient = new AMapLocationClient(mContext);
        }
        return mapClient;
    }


}
