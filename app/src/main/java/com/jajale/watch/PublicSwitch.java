package com.jajale.watch;

/**
 * 公共开关类，用于控制项目中相关开关操作，例如log打印等，上线打包前必须检查该类参数
 *
 * Created by athena on 2015/11/11.
 * Email: lizhiqiang@bjjajale.com
 */
public class PublicSwitch {
    /**
     * 是否打开Log开关，上线前需要将其设为false*
     */
    public static final boolean isLog =true;



    /**
     * 接口选择，true为线上接口，false为测试接口，上线前需该给true
     */
    public static final boolean isFormal =false;


    /**
     * 高德地图 地点轮训频率(高)
     */
    public static final long LOCATION_CALLBACK_FREQUENCY_HIGH = 5 * 1000;

    /**
     * 高德地图 地点轮训频率(低)
     */
    public static final long LOCATION_CALLBACK_FREQUENCY_LOW =10 * 1000;


    /**
     * 定位是否是首次
     */
    public static boolean isPositionFirst = true;

    /**
     * 网络获取超时时间
     */
    public static final int NETWORK_TIME_OUT_TIME = 15 * 1000;
}
