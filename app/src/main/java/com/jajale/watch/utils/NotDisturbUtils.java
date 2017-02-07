package com.jajale.watch.utils;

import com.jajale.watch.BaseApplication;
import com.jajale.watch.dao.SqliteHelper;
import com.jajale.watch.entitydb.DbHelper;
import com.jajale.watch.entitydb.NotDisturb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lilonghui on 2015/12/2.
 * Email:lilonghui@bjjajale.com
 */
public class NotDisturbUtils {


    public static void refreshDB(final List<NotDisturb> notDisturbList, final String watchID) {
        ThreadUtils.getPool().execute(new Runnable() {
            @Override
            public void run() {

                if (notDisturbList != null && notDisturbList.size() > 0) {
                    SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
                    if (sqliteHelper != null) {
                        DbHelper<NotDisturb> dbHelper = new DbHelper<NotDisturb>(sqliteHelper, NotDisturb.class);
                        Map<String, Object> paramets = new HashMap<String, Object>();
                        paramets.put(NotDisturb.WATCHID, watchID);
                        List  list = dbHelper.queryForEq(paramets);

                        if (list != null) {
                            if (list.size() > 0) {
                                dbHelper.remove(list);
                            }
                            for (int i = 0; i < notDisturbList.size(); i++) {
                                dbHelper.createIfNotExists(notDisturbList.get(i));

                            }
                        }

                    }


                }

            }


        });

    }



    private static void clearDB() {
        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        DbHelper dbHelper = new DbHelper<NotDisturb>(sqliteHelper, NotDisturb.class);
        dbHelper.clear();
    }

    public static List<NotDisturb> getNotDisTurbListFromDB(String watchID) {
        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        if (sqliteHelper != null) {
            DbHelper<NotDisturb> familyMemberDbHelper = new DbHelper<NotDisturb>(sqliteHelper, NotDisturb.class);
            Map<String, Object> paramets = new HashMap<String, Object>();
            paramets.put(NotDisturb.WATCHID, watchID);
            List lists = familyMemberDbHelper.queryForEq(paramets);

            return lists;
        }

        return null;
    }

    public static void deleteWatchList(String watchID) {
        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        if (sqliteHelper != null) {
            DbHelper<NotDisturb> dbHelper = new DbHelper<NotDisturb>(sqliteHelper, NotDisturb.class);
            Map<String, Object> paramets = new HashMap<String, Object>();
            paramets.put(NotDisturb.WATCHID, watchID);
            List list = dbHelper.queryForEq(paramets);
            dbHelper.remove(list);
        }
    }
    public static void updateNotDisturbData(NotDisturb notDisturb) {

        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        if (sqliteHelper != null) {
            DbHelper<NotDisturb> dbHelper = new DbHelper<NotDisturb>(sqliteHelper, NotDisturb.class);
            Map<String, Object> paramets = new HashMap<String, Object>();
            paramets.put(NotDisturb.ID, notDisturb.getId());
            List<NotDisturb> list = dbHelper.queryForEq(paramets);
            if (list != null && list.size() > 0) {
                dbHelper.update(notDisturb);
            }
        }

    }



}
