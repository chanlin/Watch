package com.jajale.watch.utils;

import com.google.gson.Gson;
import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.R;
import com.jajale.watch.dao.SqliteHelper;
import com.jajale.watch.entity.SmartWatchListData;
import com.jajale.watch.entitydb.DbHelper;
import com.jajale.watch.entitydb.SmartWatch;
import com.jajale.watch.factory.MsgMemberFactory;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.listener.ListListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lilonghui on 2016/2/25.
 * Email:lilonghui@bjjajale.com
 */
public class SmartWatchUtils {

    /**
     * 开线程处理创建或更新watch信息到数据库的child表
     */
    public static void refreshDb(final List<SmartWatch> watchList) {
        ThreadUtils.getPool().execute(new Runnable() {
            @Override
            public void run() {
                clearDB();
                for (SmartWatch watch : watchList) {
                    createIfNotExists(watch, 0);
                }
            }
        });
    }

    private static void clearDB() {

        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        DbHelper dbHelper = new DbHelper<SmartWatch>(sqliteHelper, SmartWatch.class);
        dbHelper.clear();
    }

    /**
     * Watch转Child
     *
     * @param watch
     */
    public static void createIfNotExists(SmartWatch watch, int index) {
        L.e(index + "<----------------------->");
        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        DbHelper dbHelper = new DbHelper<SmartWatch>(sqliteHelper, SmartWatch.class);
        dbHelper.createIfNotExists(watch);
    }


    /**
     * watch是否存在在child表里
     *
     * @param watch
     * @return
     */
    private static boolean isWatchExit(SmartWatch watch) {
        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        DbHelper dbHelper = new DbHelper<SmartWatch>(sqliteHelper, SmartWatch.class);
        Map<String, Object> paramets = new HashMap<String, Object>();
        paramets.put(SmartWatch.ID, watch.getUser_id());
        List forAll = dbHelper.queryForEq(paramets);
        return forAll != null && forAll.size() > 0;
    }

    public static void updateWatch(SmartWatch watch) {
        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        DbHelper dbHelper = new DbHelper<SmartWatch>(sqliteHelper, SmartWatch.class);
        Map<String, Object> paramets = new HashMap<String, Object>();
        paramets.put(SmartWatch.ID, watch.getUser_id());
        List list = dbHelper.queryForEq(paramets);
        if (list != null && list.size() > 0) {
            dbHelper.update(watch);
        }
    }

    public static void deleteWatch(SmartWatch watch) {
        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        DbHelper dbHelper = new DbHelper<SmartWatch>(sqliteHelper, SmartWatch.class);
        Map<String, Object> paramets = new HashMap<String, Object>();
        paramets.put(SmartWatch.ID, watch.getUser_id());
        List list = dbHelper.queryForEq(paramets);
        if (list != null && list.size() > 0) {
            //手表数据改变 tag

            if (BaseApplication.getHomeActivity()!=null){

                BaseApplication.getHomeActivity().watchDataHasChanged=true;
            }


            dbHelper.delete(watch);
        }
    }


    public static List<SmartWatch> getWatchList() {
        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        if (sqliteHelper != null) {
            DbHelper<SmartWatch> dbHelper = new DbHelper<SmartWatch>(sqliteHelper, SmartWatch.class);
            List<SmartWatch> lists = dbHelper.queryForAll();
            return lists;
        }

        return null;
    }


    /**
     * java获取手表列表信息
     */
    public static void getWatchListFromNetWork(String userID, final ListListener<SmartWatch> listener) {

        List<SmartWatch> list0=SmartWatchUtils.getWatchList();
        L.e("123======2222——list0.size()=="+list0.size());

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", userID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API, AppConstants.JAVA_GET_WATCH_LIST_URL, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                try {
                    List<SmartWatch> list0=SmartWatchUtils.getWatchList();
                    L.e("123======原来——list0.size()=="+list0.size());

                    Gson gson = new Gson();
                    SmartWatchListData fromJson = gson.fromJson(result, SmartWatchListData.class);

                    refreshDb(fromJson.getWatchList());//数据库刷新

                    //数据库添加系统消息
                    MessageUtils.refreshMsgMemberDb(MsgMemberFactory.getInstance().createMsgMemberListBySmartWatchList(fromJson.getWatchList()));



                    L.e("123======网络数据——list1.size()==" + fromJson.getWatchList().size());
                    listener.onSuccess(fromJson.getWatchList());


                } catch (Exception e) {
                    L.e("123====e=="+e.toString());
                }
            }

            @Override
            public void onFailure(String result) {
                T.s(result);
                listener.onError("");
            }

            @Override
            public void onError() {
                listener.onError("");
            }
        });


    }



    public static int getElectricityView(int electricity) {
        int electrictyView;

        if (electricity <= 33) {
            electrictyView =R.mipmap.battery_one;
        } else if (electricity > 33 && electricity <= 66) {
            electrictyView =R.mipmap.battery_two;
        } else if (electricity > 66 && electricity <= 99) {
            electrictyView =R.mipmap.battery_three;
        }else{
            electrictyView = R.mipmap.battery_one;
        }

        return electrictyView;
    }

}
