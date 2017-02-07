package com.jajale.watch.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by athena on 2015/11/11.
 * Email: lizhiqiang@bjjajale.com
 */
public class ThreadUtils {
    private static ThreadUtils instance = null;
    //线程池中线程最大个数
    private static final int THREAD_MAX_NUMBER = 10;
    //采用线程池来管理线程
    private static ExecutorService executorService = Executors.newFixedThreadPool(THREAD_MAX_NUMBER);

    private ThreadUtils(){}

    public static ThreadUtils getInstances(){
        if(instance == null){
            instance = new ThreadUtils();
        }
        return instance;
    }

    /**
     * 获取线程池
     * @return
     */
    public static ExecutorService getPool() {
        return  ThreadUtils.executorService;
    }


}
