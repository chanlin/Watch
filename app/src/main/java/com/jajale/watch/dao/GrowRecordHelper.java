package com.jajale.watch.dao;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.entitydb.StandardGrow;

import java.sql.SQLException;

/**
 * Created by chunlongyuan on 12/7/15.
 */
public class GrowRecordHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "grow_record_data";
    private static final int DATABASE_VERSION = 2;

    public GrowRecordHelper() {
        super(BaseApplication.getContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, StandardGrow.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
    }
}

