package com.jajale.watch.utils;

import android.app.Activity;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.jajale.watch.AppConstants;
import com.jajale.watch.listener.SimpleResponseListener;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by athena on 2015/12/6.
 * Email: lizhiqiang@bjjajale.com
 */
public class InsuranceUtils {

    public static String checkInsuranceDeatails(List<EditText> filds_et , List<TextView> filds_text){
        if (CMethod.isEmpty(filds_et.get(0).getText().toString().trim())){
            return "请填写你的姓名";
        }
        if (CMethod.isEmpty(filds_text.get(0).getText().toString().trim())){
            return "请选择您的生日";
        }
        if (CMethod.isEmpty(filds_text.get(1).getText().toString().trim())){
            return "请选择您的证件类型";
        }
        if (CMethod.isEmpty(filds_et.get(1).getText().toString().trim())){
            return "请填写您的证件号码";
        }
        if (CMethod.isEmpty(filds_et.get(2).getText().toString().trim())){
            return "请填写您的移动电话";
        }
        if (CMethod.isEmpty(filds_et.get(4).getText().toString().trim())){
            return "请填写您的邮箱地址";
        }
        if (CMethod.isEmpty(filds_text.get(2).getText().toString().trim())){
            return "请选择您的地址";
        }
        if (CMethod.isEmpty(filds_et.get(5).getText().toString().trim())){
            return "请填写您所在的街道";
        }
        if (CMethod.isEmpty(filds_et.get(6).getText().toString().trim())){
            return "请填写您的小区门牌";
        }
        if (CMethod.isEmpty(filds_et.get(7).getText().toString().trim())){
            return "请填写宝贝的姓名";
        }
        if (CMethod.isEmpty(filds_text.get(3).getText().toString().trim())){
            return "请选择宝贝的出生日期";
        }
        if (CMethod.isEmpty(filds_text.get(4).getText().toString().trim())){
            return "请选择宝贝的证件类型";
        }
        if (CMethod.isEmpty(filds_et.get(8).getText().toString().trim())){
            return "请填写宝贝的证件号";
        }
        if (CMethod.isEmpty(filds_text.get(5).getText().toString().trim())){
            return "请选择宝贝所在城市";
        }

        if (!CMethod.isPhoneNumber(filds_et.get(2).getText().toString().trim())){
            return "请填写有效的移动电话";
        }

        String rest_me = checkIdSer(filds_text.get(1).getText().toString().trim(),filds_et.get(1).getText().toString().trim(),1);
        if (!rest_me.equals("ok")){
            return rest_me;
        }
        String rest_child = checkIdSer(filds_text.get(4).getText().toString().trim(),filds_et.get(8).getText().toString().trim(),0);
        if (!rest_child.equals("ok")){
            return rest_child;
        }



        return "success";
    };

    private static String checkIdSer(String type, String result,int from){
        if (type.equals("身份证")){
            if (result.length() == 15 || result.length() == 18){

            }else {
                String title = from == 1 ? "您的":"宝贝的";
                return "请正确输入"+title+"身份证";
            }
        }

        if (type.equals("护照")){
            if (result.length() < 8  || result.length() > 9){
                String title = from == 1 ? "您的":"宝贝的";
                return "请正确输入"+title+"护照号";
            }
        }
        if (type.equals("军官证")){
            if (result.length() < 7  || result.length() > 8){
                String title = from == 1 ? "您的":"宝贝的";
                return "请正确输入"+title+"军官证号";
            }
        }
        if (type.equals("港澳通行证")){
            if (result.length() != 9){
                String title = from == 1 ? "您的":"宝贝的";
                return "请正确输入"+title+"港澳通行证号";
            }
        }
        if (type.trim().equals("台湾通行证")){
            if (result.length() <8 || result.length() >10 ){
                if (result.length() != 9){
                    String title = from == 1 ? "您的":"宝贝的";
                    return "请正确输入"+title+"台湾通行证号";
                }
            }
        }

        return "ok";

    }



    public static void getInsurance( JSONObject dataObj ,SimpleResponseListener rListener, Response.ErrorListener eListener, Activity activity) {
        if (CMethod.isNet(activity)){
//            try {
                String url = AppConstants.INSURANCE_INSERT;
//                JSONObject paramJO = new JSONObject();
//                paramJO.put("code", CodeConstants.SEND_MSG_CODE); // 这里要改
//                paramJO.put("data", dataObj);
//                dataObj.put("")


//                RequestManager.getInstance().addRequest(new SimpleStringRequest(activity.getApplicationContext(), url, dataObj, rListener, eListener), null);
                // 这里要改
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
        }else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    T.s("网络不给力");
                }
            });
        }
    }








}
