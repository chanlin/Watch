package com.jajale.watch.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseActivity;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.IntentAction;
import com.jajale.watch.R;
import com.jajale.watch.entity.BindWatchData;
import com.jajale.watch.entity.SmartWatchListData;
import com.jajale.watch.entitydb.Message;
import com.jajale.watch.entitydb.MsgMember;
import com.jajale.watch.entitydb.SmartWatch;
import com.jajale.watch.entityno.MessageContentType;
import com.jajale.watch.factory.MessageFactory;
import com.jajale.watch.factory.MsgMemberFactory;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.DataConversionUtils;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.InputMethodUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.Md5Util;
import com.jajale.watch.utils.MessageUtils;
import com.jajale.watch.utils.PhoneSPUtils;
import com.jajale.watch.utils.SmartWatchUtils;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 使用IMEI号绑定手表
 * <p/>
 * Created by lilonghui on 2015/11/17.
 * Email:lilonghui@bjjajale.com
 */
public class BindWatchOfIMEIActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private boolean isDestroy = false;
    private Button bindwatch_imei_btn_bind;
    private ImageView bindwatch_imei_iv_deletepwd;
    private EditText bindwatch_imei_edit;
    private LoadingDialog loadingDialog;
    private PhoneSPUtils appSP;
    private String user_id, URL, service_bind;
    RequestQueue queue;
    SharedPreferences sp;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bindwatch_imei);
        loadingDialog = new LoadingDialog(this);
        appSP = new PhoneSPUtils(BindWatchOfIMEIActivity.this);
        queue = Volley.newRequestQueue(getApplicationContext());
        Bundle bundle=getIntent().getExtras();
        user_id = bundle.getString("user_id");//读出数据
        URL = bundle.getString("URL");
        service_bind = bundle.getString("service_bind");
        sp = getApplicationContext().getSharedPreferences("NotDisturb", getApplicationContext().MODE_PRIVATE);// 创建对象
        editor = sp.edit();

        initTitleView();
        initView();
    }

    /**
     * 初始化title view
     */
    private void initTitleView() {
        View title = findViewById(R.id.title);
        TextView midTitle = (TextView) title.findViewById(R.id.tv_middle);
        midTitle.setText(getResources().getString(R.string.bindwatch_title_text));
        ImageView iv_left = (ImageView) title.findViewById(R.id.iv_left);
        iv_left.setImageResource(R.drawable.title_goback_selector);
        iv_left.setOnClickListener(this);
    }

    /**
     * 初始化view
     */
    private void initView() {

        bindwatch_imei_btn_bind = (Button) findViewById(R.id.bindwatch_imei_btn_bind);
        bindwatch_imei_iv_deletepwd = (ImageView) findViewById(R.id.bindwatch_imei_iv_deletepwd);
        bindwatch_imei_edit = (EditText) findViewById(R.id.bindwatch_imei_edit);
        bindwatch_imei_edit.addTextChangedListener(this);//输入框输入监听
        bindwatch_imei_btn_bind.setOnClickListener(this);//button点击监听
        bindwatch_imei_iv_deletepwd.setOnClickListener(this);//清空输入框
        bindwatch_imei_btn_bind.setClickable(false);


        InputMethodUtils.openInputMethod(this, bindwatch_imei_edit);
    }

    @Override
    public void onClick(View v) {
        if (CMethod.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.bindwatch_imei_btn_bind://通过IMEI绑定手表
                InputMethodUtils.closeInputMethod(this, bindwatch_imei_edit);

                if (CMethod.isNet(BindWatchOfIMEIActivity.this)) {
                    String IMEI = bindwatch_imei_edit.getText().toString();
//                    bindWatch(IMEI, BaseApplication.getUserInfo().userID);
                    bindWatch(IMEI, user_id);
                    Intent intent = new Intent(getApplicationContext(), HomeSecActivity.class);
                    startActivity(intent);
                } else {
                    T.s(getResources().getString(R.string.no_network));
                }


                break;
            case R.id.bindwatch_imei_iv_deletepwd://清空输入框
                bindwatch_imei_edit.setText("");
                bindwatch_imei_btn_bind.setClickable(false);
                break;
            case R.id.iv_left://返回键
                InputMethodUtils.closeInputMethod(this, bindwatch_imei_edit);
                finish();
                break;


        }

    }


    /**
     * JAVA绑定手表
     */
    private void bindWatch(final String imei, final String userID) {
        loadingDialog.show();
        editor.putString("IMEI", imei);
        editor.putString("Telephone_Number", user_id);
        editor.commit();
        String sign = Md5Util.stringmd5(imei, service_bind, user_id);
        String path = URL + "service=" + service_bind + "&imei=" + imei + "&user_id=" + user_id + "&sign=" +sign;
        L.e("sign===" + path);
        StringRequest request = new StringRequest(path, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loadingDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsondata = jsonObject.getJSONObject("data");
                    int code = jsondata.getInt("code");
                    if (code == 0) {
                        T.s(getResources().getString(R.string.bind_success));
                    } else if (code == 1) {
                        T.s(getResources().getString(R.string.is_binding));
//                        T.s(response);
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
    public void afterTextChanged(Editable s) {
        if (s.toString().equals("")) {
            bindwatch_imei_iv_deletepwd.setVisibility(View.GONE);
        } else {
            bindwatch_imei_iv_deletepwd.setVisibility(View.VISIBLE);
        }
        if (!isImeiNumber(s.toString())) {
            bindwatch_imei_btn_bind.setBackgroundResource(R.drawable.shape_button_gray_normal);
            bindwatch_imei_btn_bind.setTextColor(getResources().getColor(R.color.common_button_middle_text_off_color));
            bindwatch_imei_btn_bind.setClickable(false);
        } else {
            bindwatch_imei_btn_bind.setBackgroundResource(R.drawable.button_common_on_selector_green);
            bindwatch_imei_btn_bind.setTextColor(getResources().getColor(R.color.common_button_middle_text_on_color));
            bindwatch_imei_btn_bind.setClickable(true);
        }


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    /**
     * 检查是否为有效的IMEI码
     *
     * @param mobile
     * @return
     */
    public boolean isImeiNumber(String mobile) {

        String phoneRegex = "[0-9]{15}";// 验证码格式正则表达式验证

        Pattern regex;
        Matcher matcher;

        regex = Pattern.compile(phoneRegex);
        matcher = regex.matcher(mobile);

        if (matcher.matches()) {
            return true;
        }

        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }

}
