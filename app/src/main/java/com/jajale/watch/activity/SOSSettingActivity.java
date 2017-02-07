package com.jajale.watch.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.jajale.watch.entity.SOSData;
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

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by lilonghui on 2015/12/8.
 * Email:lilonghui@bjjajale.com
 */
public class SOSSettingActivity extends BaseActivity implements View.OnClickListener {
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

    @InjectView(R.id.sos_setting_et_number_1)
    EditText sosSettingEtNumber1;
    @InjectView(R.id.sos_setting_et_number_2)
    EditText sosSettingEtNumber2;
    @InjectView(R.id.sos_setting_et_number_3)
    EditText sosSettingEtNumber3;
    private SmartWatch watch;
    private LoadingDialog loadingDialog;
    RequestQueue queue;
    private String[] phone;
    private String SOS, imei, sign, URL, service_sosnumber, sostelphone1, sostelphone2, sostelphone3;
    private String[] number = new String[3];
    SharedPreferences sp;
    SharedPreferences.Editor editor;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos_setting);
        ButterKnife.inject(this);
        MobclickAgent.onEvent(SOSSettingActivity.this, UMeventId.UMENG_SOS_INTO_NUMBER);
        tvMiddle.setText(getResources().getString(R.string.sos_setting_title_text));
        ivLeft.setImageResource(R.drawable.title_goback_selector);
        ivLeft.setOnClickListener(this);
        tvRight.setText(getResources().getString(R.string.save));
        tvRight.setOnClickListener(this);

        loadingDialog = new LoadingDialog(this);

        watch = getIntent().getParcelableExtra(SmartWatchListData.KEY);
        //loadingDialog.show("请稍后...", AppConstants.MaxIndex);

        queue = Volley.newRequestQueue(getApplicationContext());
        Bundle bundle=getIntent().getExtras();
        service_sosnumber = bundle.getString("service_sosnumber");//读出数据
        URL = bundle.getString("URL");
        imei = bundle.getString("imei");
        sp = getApplicationContext().getSharedPreferences("NotDisturb", getApplicationContext().MODE_PRIVATE);// 创建对象
        editor = sp.edit();
        sostelphone1 = sp.getString("sostelphone1", "");
        sostelphone2 = sp.getString("sostelphone2", "");
        sostelphone3 = sp.getString("sostelphone3", "");
        sosSettingEtNumber1.setText(sostelphone1);
        sosSettingEtNumber2.setText(sostelphone2);
        sosSettingEtNumber3.setText(sostelphone3);
        //getSosListFromNetWork();
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
                if (!CMethod.isNet(getApplicationContext())) {
                    T.s(getResources().getString(R.string.no_network));
                    return;
                }
                loadingDialog.show(getResources().getString(R.string.loading), AppConstants.MaxIndex);
                phone = new String[]{sosSettingEtNumber1.getText().toString(), sosSettingEtNumber2.getText().toString(), sosSettingEtNumber3.getText().toString()};
                number = new String[]{"1", "2", "3"};
                for (int i = 0; i <phone.length ; i++) {
                    if (!CMethod.isEmptyOrZero(phone[i]) && !CMethod.isPhoneNumber(phone[i])) {
                        T.s(getResources().getString(R.string.edittelepone) + ( i + 1 ) + getResources().getString(R.string.edittelepone_));
                        loadingDialog.dismiss();
                        return;
                    }
                }
                if (CMethod.isEmptyOrZero(phone[0])&&CMethod.isEmptyOrZero(phone[1])&&CMethod.isEmptyOrZero(phone[2])){
                    T.s(getResources().getString(R.string.not_null_empty));
                    loadingDialog.dismiss();
                    return;
                }
                setSosPhonetoNetWork();
                break;

        }

    }


    private void getSosListFromNetWork() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", BaseApplication.getUserInfo().userID);//用户ID
            jsonObject.put("friend_id", watch.getUser_id());//手表ID
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API, AppConstants.JAVA_WATCH_SOS_LIST_URL, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                loadingDialog.dismissAndStopTimer();
                Gson gson = new Gson();
                SOSData fromJson = gson.fromJson(result, SOSData.class);
                if (fromJson == null)
                    return;
                List<SOSData.SosListEntity> sosListEntities = fromJson.getSosList();
                if (sosListEntities == null)
                    return;
                if (sosListEntities.size() == 3) {
                    sosSettingEtNumber1.setText(sosListEntities.get(0).getPhone());
                    sosSettingEtNumber2.setText(sosListEntities.get(1).getPhone());
                    sosSettingEtNumber3.setText(sosListEntities.get(2).getPhone());
                }
            }

            @Override
            public void onFailure(String result) {
                loadingDialog.dismissAndStopTimer();
                T.s(result);
            }

            @Override
            public void onError() {
                loadingDialog.dismissAndStopTimer();
            }
        });
    }

    private void setSosPhonetoNetWork() {
        SOS = phone[0] + "," + phone[1] + "," + phone[2];
        sign = Md5Util.stringmd5(imei, service_sosnumber, SOS);
        String path = URL + "service=" + service_sosnumber + "&imei=" + imei + "&sos=" + SOS + "&sign=" + sign;
        L.e("sign===" + path);
        StringRequest request = new StringRequest(path, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loadingDialog.dismissAndStopTimer();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsondata = jsonObject.getJSONObject("data");
                    int code = jsondata.getInt("code");
                    if (code == 0) {
                        T.s(getResources().getString(R.string.send_code_success));
                        for (int i = 1; i <phone.length + 1; i++) {
                            editor.putString("sostelphone" + i, phone[i - 1]);
                        }
                        editor.commit();
                    } else if (code == 1) {
                        T.s(getResources().getString(R.string.not_online));
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismissAndStopTimer();
            }
        });
        queue.add(request);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {

        super.onPause();
    }



}
