package com.jajale.watch.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.jajale.watch.entity.AppInfo;
import com.jajale.watch.entitydb.SocketEntity;
import com.jajale.watch.utils.L;

import java.sql.SQLException;

/**
 * Created by athena on 2015/11/11.
 * Email: lizhiqiang@bjjajale.com
 */
public class SocketRecordHelper extends OrmLiteSqliteOpenHelper {

    private static final int DATABASE_VERSION = AppInfo.getInstace().getVersionCode();
    private static final String DB_NAME = "socket_record";

    public SocketRecordHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
//            要创建的表写在这里 以用户表为例
            TableUtils.createTableIfNotExists(connectionSource, SocketEntity.class);

        } catch (SQLException e) {
            e.printStackTrace();
            L.e(SocketRecordHelper.class.getName(), "Unable to onCreate datbases", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
    }


    /**
     * 删表，重建表
     *
     * @param database
     * @param connectionSource
     */
    private final void dropTableAndReCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {

            //要声明销毁的表 放在这里 以用户表为例
            TableUtils.dropTable(connectionSource, SocketEntity.class, true);

            onCreate(database, connectionSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
