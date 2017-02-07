package com.jajale.watch.dao;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.entity.AppInfo;
import com.jajale.watch.entitydb.Account;


import java.sql.SQLException;

/**
 * 账号表
 * Created by athena on 15-5-24.
 */
public class AccountHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "account_db";
    private static final int DATABASE_VERSION = AppInfo.getInstace().getVersionCode();

    public AccountHelper() {
        super(BaseApplication.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Account.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
    }

}
