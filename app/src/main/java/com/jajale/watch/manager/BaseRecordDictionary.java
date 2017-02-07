package com.jajale.watch.manager;

import android.content.Context;

import com.google.gson.Gson;
import com.jajale.watch.entityno.RecordDictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by athena on 2015/12/7.
 * Email: lizhiqiang@bjjajale.com
 */
public class BaseRecordDictionary {
    private static RecordDictionary dictionary = null;

    private BaseRecordDictionary() {
    }

    public static RecordDictionary getBaseDictionary(Context context) {
        if (dictionary == null) {
            try {
                InputStream is = context.getAssets().open("record", 0);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                String areas = bufferedReader.readLine();
                dictionary = new Gson().fromJson(areas, RecordDictionary.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dictionary;
    }


}
