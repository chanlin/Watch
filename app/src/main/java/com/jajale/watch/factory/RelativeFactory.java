package com.jajale.watch.factory;

/**
 * Created by athena on 2015/11/26.
 * Email: lizhiqiang@bjjajale.com
 */
public class RelativeFactory {

    private static RelativeFactory instance = null;

    //    private static BaseArea baseArea ;
    private RelativeFactory() {
    }

    public static RelativeFactory getInstance() {
        if (instance == null) {
            instance = new RelativeFactory();
        }
        return instance;
    }

}
