package com.jajale.watch.utils;


//import GDLocation;

import java.util.UUID;

/**
 * Created by athena on 2015/11/24.
 * Email: lizhiqiang@bjjajale.com
 */
public class LocationUtils {

    /**
     * 增加一条消息记录
     *
     * @return
     */
//    public static int saveMsg2DB(GDLocation location) {
//
//        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
//        if (sqliteHelper != null) {
//            DbHelper dbHelper = new DbHelper<GDLocation>(BaseApplication.getBaseHelper(), GDLocation.class);
//            return dbHelper.createIfNotExists(location);
//        }
//        return -1;
//    }
//
//    public static List<GDLocation> getAllRecord(){
//
//        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
//        if (sqliteHelper != null) {
//            DbHelper dbHelper = new DbHelper<GDLocation>(BaseApplication.getBaseHelper(), GDLocation.class);
//            return dbHelper.queryForAll();
//        }
//        return null;
//    }
//
//    public static void saveMsg2DB(String lon ,String lat ,String address) {
//        GDLocation location = new GDLocation();
//        location.red_id = getUUID();
//        location.lat = lat;
//
//        location.lon = lon;
//        location.address = address;
//        long time_million = System.currentTimeMillis() ;
//        location.millions = time_million+"";
//        location.time = CMethod.display(time_million);
//
//        saveMsg2DB(location);
//    }




//    public static String saveSocketEntity2DB(SocketResultEntity location) {
//
//        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
//        if (sqliteHelper != null) {
//            DbHelper dbHelper = new DbHelper<SocketResultEntity>(BaseApplication.getBaseHelper(), SocketResultEntity.class);
//            String uuid = getUUID();
//            location.setRed_id(uuid);
//            dbHelper.createIfNotExists(location);
//            return uuid ;
//        }
//        return "";
//    }


//    public static List<SocketResultEntity> getAllRecord(){
//
//        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
//        if (sqliteHelper != null) {
//            DbHelper dbHelper = new DbHelper<SocketResultEntity>(BaseApplication.getBaseHelper(), SocketResultEntity.class);
//            return dbHelper.queryForAll();
//        }
//        return null;
//    }

//    public static int updateSocketResultEntityByUUID(String uuid, String jsonStr) {
//        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
//        if (sqliteHelper != null) {
//            DbHelper<SocketResultEntity> dbHelper = new DbHelper<SocketResultEntity>(BaseApplication.getBaseHelper(), SocketResultEntity.class);
//            List<SocketResultEntity> list = dbHelper.queryForEq("red_id", uuid);
//            SocketResultEntity result = list.get(0);
//            result.jsonstr = jsonStr;
//            return dbHelper.update(result);
//        }
//        return -1;
//    }


    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
//        return UUID.randomUUID().toString() ;
    }








}
