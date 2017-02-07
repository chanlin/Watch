package com.jajale.watch.utils;

import com.jajale.watch.BaseApplication;
import com.jajale.watch.dao.SqliteHelper;
import com.jajale.watch.entitydb.Clock;
import com.jajale.watch.entitydb.DbHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lilonghui on 2015/12/2.
 * Email:lilonghui@bjjajale.com
 */
public class ClockUtils {


    public static void refreshDB(final List<Clock> clockDatas, final String watchID) {
        ThreadUtils.getPool().execute(new Runnable() {
            @Override
            public void run() {

                if (clockDatas != null && clockDatas.size() > 0) {
                    SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
                    if (sqliteHelper != null) {
                        DbHelper<Clock> dbHelper = new DbHelper<Clock>(sqliteHelper, Clock.class);
                        Map<String, Object> paramets = new HashMap<String, Object>();
                        paramets.put(Clock.WATCHID, watchID);
                        List list = dbHelper.queryForEq(paramets);

                        if (list != null) {
                            if (list.size() > 0) {
                                dbHelper.remove(list);
                            }
                            for (int i = 0; i < clockDatas.size(); i++) {
                                dbHelper.createIfNotExists(clockDatas.get(i));

                            }
                        }
                    }
                }

            }


        });

    }

    private static void clearDB() {
        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        DbHelper dbHelper = new DbHelper<Clock>(sqliteHelper, Clock.class);
        dbHelper.clear();
    }

    public static List<Clock> getClockListFromDB(String watchID) {
        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        if (sqliteHelper != null) {
            DbHelper<Clock> familyMemberDbHelper = new DbHelper<Clock>(sqliteHelper, Clock.class);
            Map<String, Object> paramets = new HashMap<String, Object>();
            paramets.put(Clock.WATCHID, watchID);
            List lists = familyMemberDbHelper.queryForEq(paramets);
            return lists;
        }

        return null;
    }

    public static void deleteWatchList(String watchID) {
        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        if (sqliteHelper != null) {
            DbHelper<Clock> dbHelper = new DbHelper<Clock>(sqliteHelper, Clock.class);
            Map<String, Object> paramets = new HashMap<String, Object>();
            paramets.put(Clock.WATCHID, watchID);
            List list = dbHelper.queryForEq(paramets);
            dbHelper.remove(list);
        }
    }

    public static void updateClockData(Clock clockData) {

        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        if (sqliteHelper != null) {
            DbHelper<Clock> dbHelper = new DbHelper<Clock>(sqliteHelper, Clock.class);
            Map<String, Object> paramets = new HashMap<String, Object>();
            paramets.put(Clock.ID, clockData.getId());
            List list = dbHelper.queryForEq(paramets);
            if (list != null && list.size() > 0) {
                dbHelper.update(clockData);
            }
        }

    }


}
