package com.jajale.watch.manager;

/**
 * 已废弃
 *
 * Created by athena on 2015/11/11.
 * Email: lizhiqiang@bjjajale.com
 */
public class AppSharePreferenceManager {

//    public static final String JJL_ACCOUNT_SHAREPREFERENCE = "jjl_account_sf";
//    public static final String JJL_APP_SHAREPREFERENCE = "jjl_app_sf";
//    public static final String JJL_APP_LOCK_SHAREPERFERENCE = "jjl_lock_sf";

//    public PreferencesUtils spUtils ;

    //----------------single instance start-----------------//
    private static AppSharePreferenceManager instance = null;

    private AppSharePreferenceManager() {
    }

    public static AppSharePreferenceManager getInstance() {
        if (instance == null) {
            instance = new AppSharePreferenceManager();
        }
        return instance;
    }
    //----------------single instance end  -----------------//

//    public PreferencesUtils open(String spName){
//        return  new PreferencesUtils(BaseApplication.getContext(),spName);
//    }
//    public void cloce(){
//        spUtils = null;
//    }

}
