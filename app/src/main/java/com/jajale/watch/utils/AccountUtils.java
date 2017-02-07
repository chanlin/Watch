package com.jajale.watch.utils;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;
import com.jajale.watch.dao.AccountHelper;
import com.jajale.watch.entitydb.Account;
import com.jajale.watch.entitydb.DbHelper;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by athena on 2015/11/13.
 * Email: lizhiqiang@bjjajale.com
 */
public class AccountUtils {

    public static void updateAccount(Account account){
        try {
            AccountHelper accountHelper = new AccountHelper();
            DbHelper<Account> accountDbHelper = new DbHelper<Account>(accountHelper, Account.class);
//            Dao dao  = accountDbHelper.getDao();
//            dao.update(account);
            accountDbHelper.update(account);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 存储或更新account数据库的account表
     * 更新当前账号为登录状态，其余为非登录状态
     *
     * @param phone    账号
     * @param password 密码
     */
    public static void updateAccount(final String phone, final String password) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                AccountHelper accountHelper = new AccountHelper();
                DbHelper<Account> dbHelper = new DbHelper<Account>(accountHelper, Account.class);
                List<Account> results =  dbHelper.queryForEq("phone", phone);
                if (results.size()>0){
                    Account temp = results.get(0);
                    temp.phone = phone ;
                    temp.password = password;
                    temp.updateTime = System.currentTimeMillis()+"";
                    updateAccount(temp);
                }else {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
                    Account account = new Account();
                    account.phone = phone;
                    account.password = password;
                    account.loginTime = simpleDateFormat.format(new Date());
                    account.updateTime = System.currentTimeMillis()+"";
                    account.exten_1 = "" ;
                    DbHelper<Account> accountDbHelper = new DbHelper<Account>(accountHelper, Account.class);

                    List<Account> accounts = accountDbHelper.queryForAll();
                    if (accounts != null && accounts.size() > 0) {
                        for (Account acc : accounts) {
                            if (phone.equals(acc.phone)) {
                                accountDbHelper.remove(acc);//找到一样的将其删掉
                                break;
                            }
                        }
                    }
                    accountDbHelper.createIfNotExists(account);
                }
            }
        }).start();
    }

    public static Account getAccountByPhone(String phone){
        try {
            AccountHelper accountHelper = new AccountHelper();
            DbHelper<Account> accountDbHelper = new DbHelper<Account>(accountHelper, Account.class);
            List<Account> lists =  accountDbHelper.queryForEq("phone", phone);
            return lists.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Account getLastAccount() {
        try {
            AccountHelper accountHelper = new AccountHelper();
            DbHelper<Account> accountDbHelper = new DbHelper<Account>(accountHelper, Account.class);
            Dao dao = accountDbHelper.getDao();
            String orderBy = "updateTime desc ";
            String sql = "select * from Account order by " + orderBy;
            GenericRawResults<Account> grr = null;
            L.e("SQL 语句:" + sql);
            grr = dao.queryRaw(sql, new RawRowMapper<Account>() {
                @Override
                public Account mapRow(String[] strings, String[] strings1) throws SQLException {
                    Account account = new Account();
                    account = gerParamsFromArray(account,strings,strings1);
                    return account;
                }
            });
            ArrayList<Account> relationList = new ArrayList<Account>();
            Iterator<Account> iterator = grr.iterator();
            while (iterator.hasNext()) {
                Account account = iterator.next();
                relationList.add(account);
            }
//        return relationList;
            if (relationList.size()>0){
                return relationList.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }



//    public static void insertAccountDate(User user) {
//        AccountHelper sqliteHelper = BaseApplication.getAccountHelper();
//        if (sqliteHelper != null) {
//            DbHelper dbHelper = new DbHelper<Account>(BaseApplication.getAccountHelper(), Account.class);
//            Account entity = new Account();
//            entity.setUser_name(user.getUser_name());
//            entity.setUser_password(user.getPassword());
//            entity.setUser_insert_time(System.currentTimeMillis());
//            entity.setUser_sex("1");
//            entity.setUser_token("00000000");
//            entity.setUser_create_time(System.currentTimeMillis() + "");
//            entity.setLast_login_time(System.currentTimeMillis()+"");
//            entity.setUser_permission_level(1);
//            entity.setUser_relation_type("1");
//
//            entity.setExit_count("0");
//            entity.setExit_count_day("1");
//
//            entity.setPull_morning_day("1");
//            entity.setPull_noon_day("2");
//            entity.setPull_night_day("3");
//
//            entity.setRecommend_dialog_day("1");
//
//
//            dbHelper.createIfNotExists(entity);
//        }
//    }
//
//    public static void updateAccountDate(Account entity) {
//        AccountHelper sqliteHelper = BaseApplication.getAccountHelper();
//        if (sqliteHelper != null) {
//            DbHelper dbHelper = new DbHelper<Account>(BaseApplication.getAccountHelper(), Account.class);
//            entity.setUser_insert_time(System.currentTimeMillis());
////            if(entity.free_dialog_day == null){
////                entity.free_dialog_day = "";
////            }
//
//            dbHelper.update(entity);
//        }
//    }
//
//    public static void updateAccountDate(Account entity, AccountHelper sqliteHelper) {
//        DbHelper dbHelper = new DbHelper<Account>(sqliteHelper, Account.class);
//        entity.setUser_insert_time(System.currentTimeMillis());
//        dbHelper.update(entity);
//    }
//
//
//    public static void updateAccount(String user_phone, String user_password,ResultEntity response) {
//        AccountHelper sqliteHelper = BaseApplication.getAccountHelper();
//        if (sqliteHelper != null) {
//            DbHelper dbHelper = new DbHelper<Account>(BaseApplication.getAccountHelper(), Account.class);
//            Map<String, String> map = new HashMap<String, String>();
//            map.put("user_phone", user_phone);
//            List<Account> result = dbHelper.queryForEq(map);
//            if (result.size() > 0) {
//                Account entity = result.get(0);
//                entity.setUser_name(response.getUserID());
//                entity.setUser_permission_level(response.getIsManage());
//                entity.setIsBindWatch(response.getWatchBind());
//                entity.setUser_password(user_password);
//                entity.setLast_login_time(System.currentTimeMillis()+"");
//                dbHelper.update(entity);
//            }
//        }
//    }
//
//    public static int updateAccountDate(User user) {
//        AccountHelper sqliteHelper = BaseApplication.getAccountHelper();
//        if (sqliteHelper != null) {
//            DbHelper dbHelper = new DbHelper(BaseApplication.getAccountHelper(), Account.class);
//            Map<String, String> map = new HashMap();
//            map.put("user_name", user.getUser_name());
//            List<Account> result = dbHelper.queryForEq(map);
//
//            if (result.size() == 0) {
//                insertAccountDate(user);
//                return 1;
//            } else {
//                Account entity = result.get(0);
//                entity.setUser_password(user.getPassword());
//                entity.setUser_name(user.getUser_name());
//                entity.setUser_insert_time(System.currentTimeMillis());
//
//                dbHelper.update(entity);
//
//            }
//        }
//        return 0;
//    }
//
//
//    public static Account getAccountDateByUserName(String user_name, AccountHelper sqliteHelper) {
//        if (!CMethod.isEmptyOrZero(user_name)) {
//            DbHelper dbHelper = new DbHelper<Account>(sqliteHelper, Account.class);
//            List<Account> result = dbHelper.queryForEq("user_name", user_name);
//            if (result.size() > 0) {
//                return result.get(0);
//            }
//        }
//        return null;
//    }
//
//
//    public static Account getAccountDateByUserName(String user_name) {
//        if (!CMethod.isEmptyOrZero(user_name)) {
//            AccountHelper sqliteHelper = new AccountHelper(AppConstants.ACCOUNT_HELPER_NAME);
////        AccountHelper sqliteHelper = DateApplication.getAccountHelper();
//            DbHelper dbHelper = new DbHelper<Account>(sqliteHelper, Account.class);
//            List<Account> result = dbHelper.queryForEq("user_name", user_name);
//            if (result.size() > 0) {
//                return result.get(0);
//            }
//        }
//        return null;
//    }
//
//    public static Account getLastAccountDate() {
//        AccountHelper sqliteHelper = BaseApplication.getAccountHelper();
//        if (sqliteHelper != null) {
//            DbHelper dbHelper = new DbHelper<Account>(BaseApplication.getAccountHelper(), Account.class);
//            String orderBy = "Order by last_login_time desc ";
//            String sql = "select * from Account " + orderBy;
//            L.e(sql);
//            try {
//                Dao dao = dbHelper.getDao();
//                GenericRawResults<Account> grr = dao.queryRaw(sql, new RawRowMapper() {
//                    @Override
//                    public Account mapRow(String[] strings, String[] strings2) throws SQLException {
//                        Account entity = new Account();
//                        entity = analysisSQLResult(entity, strings, strings2);
//                        return entity;
//                    }
//                });
//
//                ArrayList<Account> list = new ArrayList<Account>();
//                Iterator<Account> iterator = grr.iterator();
//
//                while (iterator.hasNext()) {
//                    Account entity = iterator.next();
//                    list.add(entity);
//                }
//                return list.get(0);
//            } catch (Exception e) {
//                return null;
//            }
//        }
//        return null;
//    }
//
//
//    private static Account analysisSQLResult(Account msg, String[] column_arr, String[] value_arr) {
//
//        for (int i = 0; i < column_arr.length; i++) {
//            try {
//                Field content = Account.class.getDeclaredField(column_arr[i]);
//                if (content.getType().toString().equals("int")) {
//                    content.set(msg, Integer.parseInt(value_arr[i]));
//                } else if (content.getType().toString().equals("long")) {
//                    content.set(msg, Long.parseLong(value_arr[i]));
//                } else {
//                    content.set(msg, value_arr[i]);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return msg;
//    }
//
//    public static void addAccountEntityIfNotExit(String user_phone) {
//        Account entity = AccountFactory.getInstance().initAccountEntityByPhone(user_phone);
//        AccountHelper sqliteHelper = BaseApplication.getAccountHelper();
//        if (sqliteHelper != null) {
//            DbHelper dbHelper = new DbHelper<Account>(BaseApplication.getAccountHelper(), Account.class);
//            dbHelper.createIfNotExists(entity);
////            L.e("addChildIfNotExit reuslt " + );
//        }
//    }
    /**
     * 整理成一个方法,因为都是全查  字段一致
     *
     * @param strings2
     * @return
     */
    private static Account gerParamsFromArray(Account r, String[] strings, String[] strings2) {

        for (int i = 0; i < strings.length; i++) {

//            L.e("gerRelationFromArray === > " + strings[i] + "<``==``>" + strings2[i]);

            try {
                Field content = Account.class.getDeclaredField(strings[i]);

                if (content.getType().getCanonicalName().equals(Integer.class.getCanonicalName())) {
                    if (strings2[i] != null) {
                        content.set(r, Integer.parseInt(strings2[i]));
                    } else {
                        content.set(r, strings2[i]);
                    }

                } else if (content.getType().getCanonicalName().equals(Long.class.getCanonicalName())) {
                    if (strings2[i] != null) {
                        content.set(r, Long.parseLong(strings2[i]));
                    } else {
                        content.set(r, strings2[i]);
                    }
                }else if (content.getType().getCanonicalName().equals(Integer.class.getCanonicalName())){
                    if (strings2[i] != null) {
                        content.set(r, Integer.parseInt(strings2[i]));
                    } else {
                        content.set(r, strings2[i]);
                    }
                }else {
                    content.set(r, strings2[i]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return r;
    }
}
