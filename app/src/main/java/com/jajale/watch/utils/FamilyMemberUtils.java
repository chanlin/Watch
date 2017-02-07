package com.jajale.watch.utils;

import com.jajale.watch.BaseApplication;
import com.jajale.watch.dao.SqliteHelper;
import com.jajale.watch.entitydb.DbHelper;
import com.jajale.watch.entitydb.FamilyMember;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lilonghui on 2015/12/2.
 * Email:lilonghui@bjjajale.com
 */
public class FamilyMemberUtils {


    public static void refreshDB(final List<FamilyMember> familyMembers, final String wachID) {
        ThreadUtils.getPool().execute(new Runnable() {
            @Override
            public void run() {

                if (familyMembers != null && familyMembers.size() > 0) {
                    SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
                    if (sqliteHelper != null) {
                        DbHelper<FamilyMember> dbHelper = new DbHelper<FamilyMember>(sqliteHelper, FamilyMember.class);
                        Map<String, Object> paramets = new HashMap<String, Object>();
                        paramets.put(FamilyMember.WATCHID, wachID);
                        List list = dbHelper.queryForEq(paramets);
                        if (list != null) {
                            if (list.size() > 0) {
                                dbHelper.remove(list);
                            }
                            for (int i = 0; i < familyMembers.size(); i++) {
                                dbHelper.createIfNotExists(familyMembers.get(i));
                            }
                        }
                    }
                }
            }
        });

    }


    private static void clearDB() {
        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        DbHelper dbHelper = new DbHelper<FamilyMember>(sqliteHelper, FamilyMember.class);
        dbHelper.clear();
    }




    public static List<FamilyMember> getFamilyMemberListFromDB(String watchID) {
        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        if (sqliteHelper != null) {

            DbHelper<FamilyMember> familyMemberDbHelper = new DbHelper<FamilyMember>(sqliteHelper, FamilyMember.class);
            Map<String, Object> paramets = new HashMap<String, Object>();
            paramets.put(FamilyMember.WATCHID, watchID);
            List lists = familyMemberDbHelper.queryForEq(paramets);
            return lists;
        }

        return null;
    }

    private static void createIfNotExists(FamilyMember familyMember) {

        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        if (sqliteHelper != null) {
            DbHelper dbHelper = new DbHelper<FamilyMember>(BaseApplication.getBaseHelper(), FamilyMember.class);
            dbHelper.createIfNotExists(familyMember);
        }

    }

    public static void updateFamilyMemberData(FamilyMember familyMember) {

        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        if (sqliteHelper != null) {
            DbHelper dbHelper = new DbHelper<FamilyMember>(sqliteHelper, FamilyMember.class);
            Map<String, Object> paramets = new HashMap<String, Object>();
            paramets.put(FamilyMember.ID, familyMember.getId());
            List list = dbHelper.queryForEq(paramets);
            if (list != null && list.size() > 0) {
                dbHelper.update(familyMember);
            }
        }

    }

    public static void deleteWatch(FamilyMember familyMember) {
        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        DbHelper dbHelper = new DbHelper<FamilyMember>(sqliteHelper, FamilyMember.class);
        Map<String, Object> paramets = new HashMap<String, Object>();
        paramets.put(FamilyMember.ID, familyMember.getId());
        List list = dbHelper.queryForEq(paramets);
        if (list != null && list.size() > 0) {
            dbHelper.remove(familyMember);
        }
    }

    public static void deleteWatchList(String watchID) {
        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        if (sqliteHelper != null) {

            DbHelper<FamilyMember> dbHelper = new DbHelper<FamilyMember>(sqliteHelper, FamilyMember.class);
            Map<String, Object> paramets = new HashMap<String, Object>();
            paramets.put(FamilyMember.WATCHID, watchID);
            List list = dbHelper.queryForEq(paramets);
            dbHelper.remove(list);
        }
    }

    private static boolean isFamilyMemberExit(FamilyMember familyMember) {
        SqliteHelper sqliteHelper = BaseApplication.getBaseHelper();
        DbHelper dbHelper = new DbHelper<FamilyMember>(sqliteHelper, FamilyMember.class);
        Map<String, Object> paramets = new HashMap<String, Object>();
        paramets.put(FamilyMember.ID, familyMember.getId());
        List forAll = dbHelper.queryForEq(paramets);
        return forAll != null && forAll.size() > 0;
    }

    /**
     * 整理成一个方法,因为都是全查  字段一致
     *
     * @param strings2
     * @return
     */
    private static FamilyMember analysisSQLResult(FamilyMember msg, String[] strings, String[] strings2) {

        for (int i = 0; i < strings.length; i++) {

//            L.e("gerRelationFromArray === > " + strings[i] + "<``==``>"+strings2[i]);

            try {
                Field content = FamilyMember.class.getDeclaredField(strings[i]);

                if (content.getType().getCanonicalName().equals(Integer.class.getCanonicalName())){
                    content.set(msg, Integer.parseInt(strings2[i]));
                }else if (content.getType().getCanonicalName().equals(Long.class.getCanonicalName())) {
                    content.set(msg, Long.parseLong(strings2[i]));
                }else {
                    content.set(msg, strings2[i]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return msg;
    }

}
