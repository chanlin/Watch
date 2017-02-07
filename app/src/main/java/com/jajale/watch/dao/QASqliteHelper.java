package com.jajale.watch.dao;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.AndroidConnectionSource;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.R;
import com.jajale.watch.utils.L;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 对应于趣味问答数据库操作
 */
public class QASqliteHelper {
//    private static final String DB_DIR= Environment.getExternalStorageDirectory().getAbsolutePath()+"/QAEntity/";
//    private static final String DB_DIR= BaseApplication.getContext().getFilesDir().getAbsolutePath()+"/DBCache/";
private static final String DB_DIR= BaseApplication.getContext().getFilesDir()+"/database/";
    public static String DB_NAME="QAEntity.db";
    private AndroidConnectionSource connectionSource;
    private static QASqliteHelper qaSqliteHelper;

    public static QASqliteHelper getInstance(Context context) {
        if (qaSqliteHelper == null) {
            qaSqliteHelper = new QASqliteHelper(context);
        }
        return qaSqliteHelper;
    }



    public QASqliteHelper(Context context) {
        File dir = new File(DB_DIR);
        L.i("guokm",DB_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, DB_NAME);
        if (!file.exists()) {
            try {
                loadFile(context, file, R.raw.qadata);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        SQLiteDatabase db = SQLiteDatabase.openDatabase(file.getPath(), null,
                SQLiteDatabase.OPEN_READWRITE);
        connectionSource = new AndroidConnectionSource(db);
    }
    /**
     * 下在文件到指定目录
     *
     * @param context
     * @param file
     *            sd卡中的文件
     * @param id
     *            raw中的文件id
     * @throws IOException
     */
    public static void loadFile(Context context, File file, int id)
            throws IOException {
        InputStream is = context.getResources().openRawResource(id);
        FileOutputStream fos = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int count = 0;
        while ((count = is.read(buffer)) > 0) {
            fos.write(buffer, 0, count);
        }
        fos.close();
        is.close();
    }

    /**
     * 获取dao
     *
     * @param clazz
     * @return
     * @throws SQLException
     */
    public <D extends Dao<T, ?>, T> D getDao(Class<T> clazz) throws Exception {
        if (connectionSource != null) {
            return DaoManager.createDao(connectionSource, clazz);
        }
        return null;
    }

}
