package com.jajale.watch.utils;

import com.jajale.watch.entity.BindWatchData;
import com.jajale.watch.entitydb.SmartWatch;

/**
 * Created by llh on 16-5-26.
 */
public class DataConversionUtils {

    public static SmartWatch getSmartWatch4BindWatchData(BindWatchData bindWatchData){
        SmartWatch smartWatch=new SmartWatch();
        smartWatch.setBirthday(bindWatchData.getBirthday());
        smartWatch.setGps_lat(bindWatchData.getGps_lat());
        smartWatch.setGps_lon(bindWatchData.getGps_lon());
        smartWatch.setIs_manage(bindWatchData.getIs_manage());
        smartWatch.setNick_name(bindWatchData.getNick_name());
        smartWatch.setPhone_num_binded(bindWatchData.getPhone_num_binded());
        smartWatch.setRelation(bindWatchData.getRelation());
        smartWatch.setSex(bindWatchData.getSex());
        smartWatch.setSwitch_step(bindWatchData.getSwitch_step());
        smartWatch.setUser_id(bindWatchData.getUser_id());
        smartWatch.setWatch_imei_binded(bindWatchData.getWatch_imei_binded());
        smartWatch.setWork_mode(CMethod.parseInt(bindWatchData.getWork_mode()));
        return smartWatch;

    }
}
