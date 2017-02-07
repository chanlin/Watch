package com.jajale.watch.message;

import android.content.Context;

import com.jajale.watch.dao.SocketRecordHelper;
import com.jajale.watch.entitydb.DbHelper;
import com.jajale.watch.entitydb.SocketEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by chunlongyuan on 12/9/15.
 */
public class SocketRecordManager {

    private Context mContext;
    private final DbHelper<SocketEntity> entityDbHelper;

    public SocketRecordManager(Context context) {
        this.mContext = context;
        entityDbHelper = new DbHelper<SocketEntity>(new SocketRecordHelper(mContext), SocketEntity.class);
    }

    public List<SocketEntity> getRecords() {
        return entityDbHelper.queryForAll();
    }

    private int getInt(JSONObject jsonObject, String key) {
        try {
            if (jsonObject != null && jsonObject.has(key)) {
                return jsonObject.getInt(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private String getString(JSONObject jsonObject, String key) {
        try {
            if (jsonObject != null && jsonObject.has(key)) {
                return jsonObject.getString(key);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveRecoder(final SocketEntity socketEntity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                entityDbHelper.createIfNotExists(socketEntity);
            }
        }).start();

    }

    public void deleteRecoder(final SocketEntity socketEntity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                entityDbHelper.delete(socketEntity);
//                entityDbHelper.createIfNotExists(socketEntity);
            }
        }).start();

    }

}
