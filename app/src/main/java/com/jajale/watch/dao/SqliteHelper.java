package com.jajale.watch.dao;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.entity.AppInfo;
import com.jajale.watch.entitydb.Clock;
import com.jajale.watch.entitydb.FamilyMember;
import com.jajale.watch.entitydb.Message;
import com.jajale.watch.entitydb.MsgMember;
import com.jajale.watch.entitydb.NotDisturb;
import com.jajale.watch.entitydb.Relative;
import com.jajale.watch.entitydb.SmartWatch;
import com.jajale.watch.entitydb.User;
import com.jajale.watch.entitydb.Vaccine;
import com.jajale.watch.utils.L;

import java.sql.SQLException;

//import GDLocation;

/**
 * Created by athena on 2015/11/11.
 * Email: lizhiqiang@bjjajale.com
 */
public class SqliteHelper extends OrmLiteSqliteOpenHelper {

    private static final int DATABASE_VERSION = AppInfo.getInstace().getVersionCode();

    public SqliteHelper(String uidDB) {
        super(BaseApplication.getContext(), uidDB, null, DATABASE_VERSION);
        L.e("SqliteHelper-------- DATA_VERSION:"+DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
//            要创建的表写在这里 以用户表为例
            TableUtils.createTableIfNotExists(connectionSource, User.class);
            TableUtils.createTableIfNotExists(connectionSource, Message.class);
            TableUtils.createTableIfNotExists(connectionSource, Relative.class);
            TableUtils.createTableIfNotExists(connectionSource, Vaccine.class);
            TableUtils.createTableIfNotExists(connectionSource, FamilyMember.class);
            TableUtils.createTableIfNotExists(connectionSource, Clock.class);
            TableUtils.createTableIfNotExists(connectionSource, NotDisturb.class);
            TableUtils.createTableIfNotExists(connectionSource, SmartWatch.class);
            TableUtils.createTableIfNotExists(connectionSource, MsgMember.class);

//            if (PublicSwitch.isLog){
//                TableUtils.createTableIfNotExists(connectionSource, GDLocation.class);
//                TableUtils.createTableIfNotExists(connectionSource,SocketResultEntity.class);
//            }



        } catch (SQLException e) {
            e.printStackTrace();
            L.e(SqliteHelper.class.getName(), "Unable to onCreate datbases", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            L.e("SQLiteHelper onUpgrade -----------------");

            if (oldVersion <= 19 && newVersion >= 20) {
                database.execSQL("ALTER TABLE User ADD user_sex TEXT");
                database.execSQL("ALTER TABLE Watch ADD baseType TEXT");
                database.execSQL("ALTER TABLE Watch ADD address TEXT");
                database.execSQL("ALTER TABLE Watch ADD baseTime TEXT");
            }
        } catch (Exception e) {
            dropTableAndReCreate(database, connectionSource);
            e.printStackTrace();
            L.e(SqliteHelper.class.getName(), "Unable to onUpgrade datbases", e);
        }
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
            TableUtils.dropTable(connectionSource, User.class, true);
            TableUtils.dropTable(connectionSource, Message.class, true);
            TableUtils.dropTable(connectionSource,Relative.class,true);
            TableUtils.dropTable(connectionSource, Vaccine.class,true);
            TableUtils.dropTable(connectionSource, FamilyMember.class,true);
            TableUtils.dropTable(connectionSource, Clock.class,true);
            TableUtils.dropTable(connectionSource, NotDisturb.class,true);
            TableUtils.dropTable(connectionSource, SmartWatch.class,true);
            TableUtils.dropTable(connectionSource, MsgMember.class,true);
//            if (PublicSwitch.isLog){
//                TableUtils.dropTable(connectionSource, GDLocation.class,true);
//            }

            onCreate(database, connectionSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
