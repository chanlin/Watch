package com.jajale.watch.fragment;

import android.content.Context;

import com.google.gson.Gson;
import com.jajale.watch.entityno.BaseArea;
import com.jajale.watch.utils.L;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by athena on 2015/12/5.
 * Email: lizhiqiang@bjjajale.com
 */
public class BaseAreaManager {
    private static BaseArea baseArea = null;

    private BaseAreaManager() {
    }

    public static BaseArea getBaseArea(Context context) {
        if (baseArea == null) {
            InputStream is = null;
            try {
                is = context.getAssets().open("area", 0);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                String result = bufferedReader.readLine();
//            String result = DataOptionUtils.readJsonFromRaw(context, R.raw.city_dic);
                baseArea = new Gson().fromJson(result, BaseArea.class);
                L.e(baseArea.provinces.size()+"");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return baseArea;
    }


}
