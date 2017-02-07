package com.jajale.watch.utils;

import com.jajale.watch.BaseApplication;
import com.jajale.watch.dao.SqliteHelper;
import com.jajale.watch.entitydb.DbHelper;
import com.jajale.watch.entitydb.User;
import com.jajale.watch.listener.OnFinishListener;

import java.util.List;

/**
 * 处理User
 * Created by chunlongyuan on 12/1/15.
 */
public class UserUtils {

    public static void updateUser(final String userID, final String watchBind, final OnFinishListener listener) {

        new Thread(new Runnable() {

            @Override
            public void run() {

                SqliteHelper sqliteHelper = new SqliteHelper(userID);
                DbHelper<User> userDbHelper = new DbHelper<User>(sqliteHelper, User.class);
                BaseApplication.setBaseHelper(sqliteHelper);

                List<User> users = userDbHelper.queryForAll();
                if (users != null && users.size() > 0) {
                    User oldUser = users.get(0);
                    oldUser.watchBind = watchBind;
                    userDbHelper.update(oldUser);
                    BaseApplication.setUserInfo(oldUser);
                } else {
                    User user = new User();
                    user.userID = userID;
                    user.watchBind = watchBind;
                    userDbHelper.createIfNotExists(user);
                    BaseApplication.setUserInfo(user);
                }

                if (listener != null) {
                    listener.onFinish();
                }
            }
        }).start();

    }

    /**
     * 更新user，可以只更新部分字段
     *
     * @param user
     */
    public static void updateUser(final User user) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                DbHelper<User> userDbHelper = new DbHelper<User>(BaseApplication.getBaseHelper(), User.class);
                List<User> users = userDbHelper.queryForEq("userID", user.userID);
                if (users != null && users.size() > 0) {

                    User oldUser = users.get(0);

                    replaceUser(oldUser, user);

                    BaseApplication.setUserInfo(oldUser);
                    userDbHelper.update(oldUser);
                }
            }
        }).start();
    }

    /**
     * 用user里有用的参数替换descUser
     *
     * @param descUser
     * @param user
     */
    public static void replaceUser(User descUser, User user) {
        if (user.latitude > 0) {
            descUser.latitude = user.latitude;
        }
        if (user.longitude > 0) {
            descUser.longitude = user.longitude;
        }
        if (!CMethod.isEmpty(user.avatar)) {
            descUser.avatar = user.avatar;
        }
        if (!CMethod.isEmpty(user.birthday)) {
            descUser.birthday = user.birthday;
        }
        if (!CMethod.isEmpty(user.gender)) {
            descUser.gender = user.gender;
        }
        if (!CMethod.isEmpty(user.watchBind)) {
            descUser.watchBind = user.watchBind;
        }
    }


    public static User getUserInfo() {
        DbHelper<User> userDbHelper = new DbHelper<User>(BaseApplication.getBaseHelper(), User.class);
        try {
            List<User> users = userDbHelper.queryForAll();
            if (users.size()>0){
                return users.get(0);
            }
        }catch (Exception e){

        }
        return null;
    }



}
