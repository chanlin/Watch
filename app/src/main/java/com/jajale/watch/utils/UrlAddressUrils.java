package com.jajale.watch.utils;

import com.jajale.watch.AppConstants;
import com.jajale.watch.PublicSwitch;

/**
 * Created by llh on 16-5-25.
 */
public class UrlAddressUrils {

    public final static int CODE_API=1001;
    public final static int CODE_CENTER=1002;
    public final static int CODE_CONTROLLER=1003;
    public final static int CODE_OTHER=10004;



    public static String getPath(int code_url,String url){

        if (PublicSwitch.isFormal) {

            switch (code_url){
                case CODE_API:
                    return AppConstants.EXTRA_JAVA_URL_DOMAIN_API + url;
                case CODE_CENTER:
                    return AppConstants.EXTRA_JAVA_URL_DOMAIN_CENTER + url;
                case CODE_CONTROLLER:
                    return AppConstants.EXTRA_JAVA_URL_DOMAIN_CONTROLLER + url;
            }
        } else {
            switch (code_url){
                case CODE_API:
                    return AppConstants.TEST_JAVA_URL_API + url;
                case CODE_CENTER:
                    return AppConstants.TEST_JAVA_URL_CENTER + url;
                case CODE_CONTROLLER:
                    return AppConstants.TEST_JAVA_URL_CONTROLLER + url;
            }
        }
        return AppConstants.EXTRA_JAVA_URL_DOMAIN_API + url;
    }
    public static String getPath(String coderUrlDomain,int code_url,String url){

        if (PublicSwitch.isFormal) {

            switch (code_url){
                case CODE_API:
                    return AppConstants.EXTRA_JAVA_URL_DOMAIN_API + url;
                case CODE_CENTER:
                    return AppConstants.EXTRA_JAVA_URL_DOMAIN_CENTER + url;
                case CODE_CONTROLLER:
                    return AppConstants.EXTRA_JAVA_URL_DOMAIN_CONTROLLER + url;
            }
        } else {
            return coderUrlDomain + url;
        }
        return AppConstants.EXTRA_JAVA_URL_DOMAIN_CENTER + url;
    }


//    public static String getPath(String coderUrlDomain, String url) {
//
//        if (PublicSwitch.isFormal) {
//            return AppConstants.EXTRA_JAVA_URL_DOMAIN_API + url;
//        } else {
//            return coderUrlDomain + url;
//        }
//
//    }
//
//    public static String getPath(String url) {
//
//        if (PublicSwitch.isFormal) {
//            return AppConstants.EXTRA_JAVA_URL_DOMAIN_API + url;
//        } else {
//            return AppConstants.TEST_JAVA_URL_API + url;
//        }
//
//    }
//
//    public static String getHasCodePath(String url) {
//
//        if (PublicSwitch.isFormal) {
//            return AppConstants.EXTRA_JAVA_URL_DOMAIN_API + url;
//        } else {
//            return AppConstants.TEST_JAVA_URL_CONTROLLER + url;
//        }
//
//    }

}
