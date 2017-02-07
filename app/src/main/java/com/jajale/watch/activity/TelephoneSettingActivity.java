package com.jajale.watch.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseActivity;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.R;
import com.jajale.watch.entity.PhoneBookData;
import com.jajale.watch.entity.SmartWatchListData;
import com.jajale.watch.entitydb.SmartWatch;
import com.jajale.watch.entityno.UMeventId;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.Md5Util;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lilonghui on 2015/12/8.
 * Email:lilonghui@bjjajale.com
 */
public class TelephoneSettingActivity extends BaseActivity implements View.OnClickListener {
    @InjectView(R.id.iv_left)
    ImageView ivLeft;
    @InjectView(R.id.tv_left_2)
    TextView tvLeft2;
    @InjectView(R.id.tv_middle)
    TextView tvMiddle;
    @InjectView(R.id.tv_right)
    TextView tvRight;
    @InjectView(R.id.iv_right)
    ImageView ivRight;
    private SmartWatch watch;
    private LoadingDialog loadingDialog;

    private Gson gson = new Gson();


    private List<LinearLayout> dictionary = new ArrayList<LinearLayout>();

    RequestQueue queue;
    String URL, url, url2, imei, sign, service_contact_a, service_contact_b;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private String[] watch_telphone = new String[10];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teltphone_setting);
        ButterKnife.inject(this);

        tvMiddle.setText(getResources().getString(R.string.telephone_setting_title_text));
        ivLeft.setImageResource(R.drawable.title_goback_selector);
        ivLeft.setOnClickListener(this);
        tvRight.setText(getResources().getString(R.string.save));
        tvRight.setOnClickListener(this);
        sp = getApplicationContext().getSharedPreferences("NotDisturb", getApplicationContext().MODE_PRIVATE);// 创建对象
        editor = sp.edit();
        for (int i = 1; i < watch_telphone.length + 1; i++) {
            String s = sp.getString("watch_telphone" + i, "");
            watch_telphone[i - 1] = s;
            Log.e("src",watch_telphone[i-1]);
        }


        initListView();

        loadingDialog = new LoadingDialog(this);
        watch = getIntent().getParcelableExtra(SmartWatchListData.KEY);
        //loadingDialog.show("请稍后...", AppConstants.MaxIndex);

        queue = Volley.newRequestQueue(getApplicationContext());
        Bundle bundle=getIntent().getExtras();
        service_contact_a = bundle.getString("service_contact_a");//读出数据
        service_contact_b = bundle.getString("service_contact_b");
        URL = bundle.getString("URL");
        imei = bundle.getString("imei");


        //getPhoneBookListFromNetWork();
    }


    private void initListView() {

        LinearLayout count_1 = (LinearLayout) findViewById(R.id.laytou_1);
        dictionary.add(count_1);
        LinearLayout count_2 = (LinearLayout) findViewById(R.id.laytou_2);
        dictionary.add(count_2);
        LinearLayout count_3 = (LinearLayout) findViewById(R.id.laytou_3);
        dictionary.add(count_3);
        LinearLayout count_4 = (LinearLayout) findViewById(R.id.laytou_4);
        dictionary.add(count_4);
        LinearLayout count_5 = (LinearLayout) findViewById(R.id.laytou_5);
        dictionary.add(count_5);
        LinearLayout count_6 = (LinearLayout) findViewById(R.id.laytou_6);
        dictionary.add(count_6);
        LinearLayout count_7 = (LinearLayout) findViewById(R.id.laytou_7);
        dictionary.add(count_7);
        LinearLayout count_8 = (LinearLayout) findViewById(R.id.laytou_8);
        dictionary.add(count_8);
        LinearLayout count_9 = (LinearLayout) findViewById(R.id.laytou_9);
        dictionary.add(count_9);
        LinearLayout count_10 = (LinearLayout) findViewById(R.id.laytou_10);
        dictionary.add(count_10);

        for (int i = 0; i < dictionary.size(); i++) {
            TextView tv_name = (TextView) dictionary.get(i).findViewById(R.id.tv_telephone_title_name);
            TextView tv_phone = (TextView) dictionary.get(i).findViewById(R.id.tv_telephone_title_phone);
            int number = i + 1;
            tv_name.setText(getResources().getString(R.string.telephone_name) + number + ":");
            tv_phone.setText(getResources().getString(R.string.telephone_number) + number + ":");

            LinearLayout layout = dictionary.get(i);// 获得子item的layout
            EditText et_name = (EditText) layout.findViewById(R.id.tel_setting_et_name);
            EditText et_phone = (EditText) layout.findViewById(R.id.tel_setting_et_number);
            String[] strings = watch_telphone[i].split(",");
            if (strings.length == 2) {
                et_name.setText(strings[1]);
                et_phone.setText(strings[0]);
                Log.e("src",strings[0] + "," + strings[1]);
            }
        }
    }


    @Override
    public void onClick(View v) {

        if (CMethod.isFastDoubleClick()) {
            return;
        }

        switch (v.getId()) {
            case R.id.iv_left://返回键
                finish();
                break;
            case R.id.tv_right://保存
//                if (!isManager()) {
//                    return;
//                }
                if (!CMethod.isNet(getApplicationContext())) {
                    T.s(getResources().getString(R.string.no_network));
                    return;
                }
                loadingDialog.show(getResources().getString(R.string.loading), AppConstants.MaxIndex);
//                tvRight.setVisibility(View.GONE);
//                boolean isIdenticalNameOrPhone=false;

                JSONArray arr = new JSONArray();
                JSONArray arr_post = new JSONArray();
                for (int i = 0; i < dictionary.size(); i++) {
                    int num = i + 1;
                    LinearLayout layout = dictionary.get(i);// 获得子item的layout

                    if (layout != null) {
                        EditText et_name = (EditText) layout.findViewById(R.id.tel_setting_et_name);
                        EditText et_phone = (EditText) layout.findViewById(R.id.tel_setting_et_number);

                        String send_name = et_name.getText().toString().trim();
                        String send_phone = et_phone.getText().toString().trim();

                        JSONObject obj_post = new JSONObject();
                        try {
                            obj_post.put("phone", send_phone);
                            obj_post.put("nickName", send_name);
                            obj_post.put("number", num + "");
                            arr_post.put(obj_post);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (!CMethod.isEmptyOrZero(send_name) && !CMethod.isEmptyOrZero(send_phone)) {

                            if (isIdenticalNameOrPhone(i, send_phone, send_name)) {
                                T.s(getResources().getString(R.string.repeattelephone));
                                loadingDialog.dismiss();
                                return;
                            }
                            JSONObject obj = new JSONObject();
                            try {
                                obj.put("phone", send_phone);
                                obj.put("nickName", send_name);
                                obj.put("number", num + "");
                                arr.put(obj);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (CMethod.isEmptyOrZero(send_name) && !CMethod.isEmptyOrZero(send_phone)) {
                            T.s(getResources().getString(R.string.editname) + num + getResources().getString(R.string.editname_));
                            loadingDialog.dismiss();
                            return;
                        } else if (!CMethod.isEmptyOrZero(send_name) && CMethod.isEmptyOrZero(send_phone)) {
                            T.s(getResources().getString(R.string.edittelepone) + num + getResources().getString(R.string.edittelepone_));
                            loadingDialog.dismiss();
                            return;
                        }
                    }
                }


                if (arr.length() > 0) {
                    uploadUserPhoneBook(arr_post);
                } else {
                    loadingDialog.dismiss();
                    T.s(getResources().getString(R.string.edit_history));
                }
                MobclickAgent.onEvent(this, UMeventId.UMENG_TELEPHONE_SETTINGS_NUMBER);
                break;

        }

    }


    private boolean isIdenticalNameOrPhone(int num, String phone, String name) {
        for (int i = 0; i < dictionary.size(); i++) {
            LinearLayout layout = dictionary.get(i);// 获得子item的layout

            if (layout != null) {
                EditText et_name = (EditText) layout.findViewById(R.id.tel_setting_et_name);
                EditText et_phone = (EditText) layout.findViewById(R.id.tel_setting_et_number);

                String send_name = et_name.getText().toString().trim();
                String send_phone = et_phone.getText().toString().trim();

                if (num != i && (phone.equals(send_phone) || name.equals(send_name))) {
                    return true;
                }
            }
        }
        return false;
    }

    public void uploadUserPhoneBook(JSONArray src) {
        final String[] p = new String[10];
        final String[] p2 = new String[10];
        String[] num = new String[10];
        try {
            JSONArray array = new JSONArray(src.toString());
            for (int i = 0; i < src.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                String phone = jsonObject.getString("phone");
                String nickName = jsonObject.getString("nickName");
                String number = jsonObject.getString("number");
                String str = "";
                char[] utfBytes = nickName.toCharArray();
                for (int j = 0; j < nickName.length(); j++) {
                    String name = Integer.toHexString(utfBytes[j]);
                    if (name.length() <= 2) {
                        name = "00" + name;
                    }
                    str = str + name;
                }
                if (!phone.equals("")) {
                    p2[i] = phone + "," + "feff" + str;
                } else {
                    p2[i] = phone + "," + str;
                }
                p[i] = phone + "," + nickName;
                num[i] = number;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(true){
            String contacta = p2[0];
            for (int i = 1; i < 5; i++){
                contacta = contacta + "," + p2[i];
            }
            sign = Md5Util.stringmd5(contacta, imei, service_contact_a);
            url = URL + "service=" + service_contact_a + "&imei=" + imei + "&contacta=" + contacta +"&sign=" +sign;
            L.e("urla=" + url);
            StringRequest request1 = new StringRequest(url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject jsondata = jsonObject.getJSONObject("data");
                        int code = jsondata.getInt("code");
                        if(code == 0){

                            for (int i = 1; i < p.length + 1; i++) {
                                editor.putString("watch_telphone" + i, p[i - 1]);
                            }
                            editor.commit();
                            T.s(getResources().getString(R.string.send_code_success));
                        }else if(code == 1){
                            T.s(getResources().getString(R.string.not_online));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            queue.add(request1);
        }
        if(true){
            String contactb = p2[5];
            for (int i = 6; i < 10; i++){
                contactb = contactb + "," + p2[i];
            }
            sign = Md5Util.stringmd5(contactb, imei, service_contact_b);
            url2 = URL + "service=" + service_contact_b + "&imei=" + imei + "&contactb=" + contactb +"&sign=" +sign;
            L.e("urlb=" + url2);
            StringRequest request2 = new StringRequest(url2, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject jsondata = jsonObject.getJSONObject("data");
                        int code = jsondata.getInt("code");
                        if(code == 0){

                        }else if(code == 1){
//                            T.s(response);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            queue.add(request2);
            new Handler().postDelayed((new Runnable() {
                @Override
                public void run() {
                    loadingDialog.dismiss();
//                    tvRight.setVisibility(View.VISIBLE);
                }
            }), 5000);
        }
    }

    private void getPhoneBookListFromNetWork() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", BaseApplication.getUserInfo().userID);//用户ID
            jsonObject.put("friend_id", watch.getUser_id());//手表ID
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API, AppConstants.JAVA_WATCH_PHONE_BOOK_LIST_URL, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                PhoneBookData fromJson = gson.fromJson(result, PhoneBookData.class);
                List<PhoneBookData.PhoneListEntity> lists = fromJson.getPhoneList();
                if (lists.size() > dictionary.size()) {
                    lists = lists.subList(0, dictionary.size());
                }

                for (int i = 0; i < lists.size(); i++) {
                    PhoneBookData.PhoneListEntity entity = lists.get(i);
                    int num=CMethod.parseInt(entity.getNumber())-1;

                    if (num>=0&&num<dictionary.size()){
                        LinearLayout content = dictionary.get(num);
                        EditText et_name = (EditText) content.findViewById(R.id.tel_setting_et_name);
                        EditText et_phone = (EditText) content.findViewById(R.id.tel_setting_et_number);
                        et_name.setText(entity.getNickName());
                        et_phone.setText(entity.getPhone());
                    }
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(String result) {
                loadingDialog.dismiss();
                T.s(result);
            }

            @Override
            public void onError() {
                loadingDialog.dismiss();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    private boolean isManager() {
        if (watch.getIs_manage() == 0) {
            T.s(getResources().getString(R.string.admin_privileges));
        }
        return watch.getIs_manage() == 1;
    }
}
