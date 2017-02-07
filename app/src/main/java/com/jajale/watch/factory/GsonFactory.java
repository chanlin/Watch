package com.jajale.watch.factory;

import com.google.gson.Gson;


public class GsonFactory {

    private static Gson gson;
    public static Gson newInstance(){
        if (gson==null){
            synchronized (GsonFactory.class){
                if (gson==null){
                    gson = new Gson();
                }
            }
        }
        return gson;
    }

}
