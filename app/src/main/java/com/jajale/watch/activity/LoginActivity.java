package com.jajale.watch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseActivity;
import com.jajale.watch.IntentAction;
import com.jajale.watch.R;
import com.jajale.watch.dialog.MoveDataDialog;
import com.jajale.watch.entity.IntentExtra;
import com.jajale.watch.entity.MoveData;
import com.jajale.watch.entity.ResultEntity;
import com.jajale.watch.entity.SmartWatchListData;
import com.jajale.watch.entitydb.SmartWatch;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.listener.ListListener;
import com.jajale.watch.listener.OnFinishListener;
import com.jajale.watch.listener.SimpleClickListener;
import com.jajale.watch.utils.AccountUtils;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.InputMethodUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LastLoginUtils;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.Md5Util;
import com.jajale.watch.utils.MoveDataUtils;
import com.jajale.watch.utils.PhoneSPUtils;
import com.jajale.watch.utils.SmartWatchUtils;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;
import com.jajale.watch.utils.UserUtils;
import com.jajale.watch.utils.WhiteLoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 登录页面
 * <p/>
 * Created by lilonghui on 2015/11/17.
 * Email:lilonghui@bjjajale.com
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    private Button login_btn_login;
    private ImageView login_iv_deletepwd;
    private EditText login_edit;
    private TextView login_tv_forget;
    private LoadingDialog loadingDialog;
    private LastLoginUtils lastLoginUtils;
    private boolean needMoveData;
    private WhiteLoadingDialog whiteLoadingDialog;
    private MoveDataDialog resetPassWordmoveDataDialog;
    private MoveDataDialog moveDataDialog;
    private PhoneSPUtils phoneSPUtils;

    public static final String TAG = "DearDetailFragment";
    String URL = "http://lib.huayinghealth.com/lib-x/?";
    String service_bind = "watch.bind_watch";//绑定手表


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loadingDialog = new LoadingDialog(this);
        whiteLoadingDialog = new WhiteLoadingDialog(this);
        lastLoginUtils = new LastLoginUtils(LoginActivity.this);
        phoneSPUtils=new PhoneSPUtils(this);
        needMoveData = getIntent().getBooleanExtra(MoveDataUtils.NEED_CHECK_USER, true);
        L.e("123==needMoveData==" + needMoveData);
        initTitleView();
        initView();

    }

    /**
     * 初始化title view
     */
    private void initTitleView() {
        View title = findViewById(R.id.title);
        TextView midTitle = (TextView) title.findViewById(R.id.tv_middle);
        midTitle.setText(getResources().getString(R.string.login_button_text));
        ImageView iv_left = (ImageView) title.findViewById(R.id.iv_left);
        iv_left.setImageResource(R.drawable.title_goback_selector);
        iv_left.setOnClickListener(this);
    }

    /**
     * 初始化view
     */
    private void initView() {

        login_btn_login = (Button) findViewById(R.id.login_btn_login);
        login_iv_deletepwd = (ImageView) findViewById(R.id.login_iv_deletepwd);
        login_tv_forget = (TextView) findViewById(R.id.login_tv_forget);
        login_edit = (EditText) findViewById(R.id.login_edit_pwd);
        login_edit.addTextChangedListener(this);//输入框输入监听
        login_btn_login.setOnClickListener(this);//button点击监听
        login_iv_deletepwd.setOnClickListener(this);//清空输入框
        login_tv_forget.setOnClickListener(this);//忘记密码监听
        login_btn_login.setClickable(false);

        InputMethodUtils.openInputMethod(this, login_edit);
    }

    @Override
    public void onClick(View v) {

        if (CMethod.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.login_btn_login://登录
                InputMethodUtils.closeInputMethod(this, login_edit);
                String pwd = login_edit.getText().toString();
                String phone = getIntent().getStringExtra(IntentExtra.PHONE);//必须有
                String imei = getIntent().getStringExtra(IntentExtra.IMEI);//必须有
                if (!CMethod.isEmpty(phone)) {
                    if ("123456".equals(pwd)) {
//                        if (true) { //判断是否绑定手表imei
                        if (!"".equals(imei)) { //判断是否绑定手表imei
                            Intent intent = new Intent(LoginActivity.this, HomeSecActivity.class);
                            intent.putExtra(IntentAction.OPEN_HONE_FROM, "login");
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent_bind_watch = new Intent(this, BindWatchActivity.class);//跳转到绑定手表imei页面
                            intent_bind_watch.putExtra(IntentAction.KEY_CLASS_NAME, TAG);
                            Bundle mBundle_bind = new Bundle();
                            mBundle_bind.putString("service_bind", service_bind);
                            mBundle_bind.putString("user_id", phone);
                            mBundle_bind.putString("URL", URL);
                            intent_bind_watch.putExtras(mBundle_bind);
                            startActivity(intent_bind_watch);
                        }
                    } else {
                        T.s(getResources().getString(R.string.password_incorrect));
                    }
//                    if (needMoveData) {
//                        moveData(phone, Md5Util.string2MD5(pwd));
//                    } else {
//                        userLoginFromWeb(phone, Md5Util.string2MD5(pwd), false);
//                    }
                } else {
                    finish();
                }
                break;
            case R.id.login_iv_deletepwd://清空输入框
                login_edit.setText("");
                login_btn_login.setClickable(false);
                break;
            case R.id.login_tv_forget://忘记密码
                if (!needMoveData){
                    Intent intent_reset_password = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                    intent_reset_password.putExtra(MoveDataUtils.NEED_CHECK_USER,needMoveData);
                    startActivity(intent_reset_password);
                }else{

                    resetPassWordmoveDataDialog = new MoveDataDialog(LoginActivity.this, "提示", "数据尚未迁移", "", "取消", "联系客服", new SimpleClickListener() {
                        @Override
                        public void ok() {
                            MoveDataUtils.callPhone(LoginActivity.this);
                        }

                        @Override
                        public void cancle() {
                            resetPassWordmoveDataDialog.dismiss();
                        }
                    });
                    resetPassWordmoveDataDialog.show();
                }

                break;
            case R.id.iv_left://返回键
                finish();
                break;


        }

    }


    private void moveData(final String user_name, final String pass) {
        whiteLoadingDialog.show("数据更新中...");
        MoveDataUtils.moveData(user_name, pass, "", new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                whiteLoadingDialog.dismiss();
                Gson gson = new Gson();
                MoveData moveData = gson.fromJson(result, MoveData.class);

                if (moveData != null) {
                    int code = moveData.getCode();
                    if (code == 200) {
                        phoneSPUtils.save("userDataCanMove"+user_name, false);
                        userLoginFromWeb(user_name, pass,true);
                    } else {

                        moveDataDialog = new MoveDataDialog(LoginActivity.this, "提示", "数据更新失败，请重试", "", "重试", "联系客服", new SimpleClickListener() {
                            @Override
                            public void ok() {
                                MoveDataUtils.callPhone(LoginActivity.this);
                            }

                            @Override
                            public void cancle() {
                                moveDataDialog.dismiss();
                                moveData(user_name, pass);
                            }
                        });
                        moveDataDialog.show();

                    }
                }


            }

            @Override
            public void onFailure(String result) {
                whiteLoadingDialog.dismiss();
                T.s(result);
            }

            @Override
            public void onError() {
                whiteLoadingDialog.dismiss();
            }
        });


    }




    /**
     * java登录访问网络
     */
    private void userLoginFromWeb(final String user_name, final String password, boolean fromMoveData) {
        if (fromMoveData){
            whiteLoadingDialog.show("更新成功，正在登录");
        }else{
            loadingDialog.show("正在登录,请稍后...");
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_name", user_name);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API, AppConstants.JAVA_LOGIN_URL, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                final ResultEntity fromJson = gson.fromJson(result, ResultEntity.class);

                //存储账号信息
                AccountUtils.updateAccount(user_name, password);

                //存储user
                UserUtils.updateUser(fromJson.getUser_id(), fromJson.getWatch_bind() + "", new OnFinishListener() {
                    @Override
                    public void onFinish() {
                        lastLoginUtils.setPhone(user_name);
                        lastLoginUtils.setPassword(password);
                        lastLoginUtils.setUserId(fromJson.getUser_id());
                        lastLoginUtils.login();

                        if (fromJson.getWatch_bind() == 0) {//手表信息为空时进入绑定手表界面
                            loadingDialog.dismiss();
                            whiteLoadingDialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this, BindWatchActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            //获取手表信息，无信息，则进入编辑
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SmartWatchUtils.getWatchListFromNetWork(fromJson.getUser_id(), new ListListener<SmartWatch>() {
                                        @Override
                                        public void onError(String message) {
                                            loadingDialog.dismiss();
                                            whiteLoadingDialog.dismiss();
                                        }

                                        @Override
                                        public void onSuccess(List<SmartWatch> smartWatches) {
                                            toWatchActivity(smartWatches);
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void onFailure(String result) {
                loadingDialog.dismiss();
                whiteLoadingDialog.dismiss();
                T.s(result);
            }

            @Override
            public void onError() {
                loadingDialog.dismiss();
                whiteLoadingDialog.dismiss();
            }
        });
    }


    /**
     * 从手表列表判断进入哪一个activity
     *
     * @param watches
     */
    private void toWatchActivity(List<SmartWatch> watches) {
        {
            loadingDialog.dismiss();
            whiteLoadingDialog.dismiss();
            //watchid为列表为0则进入绑定手表页面
            if (watches.size() != 0) {
                //判断对首个手表是否有管理员权限,没有权限则直接进入主界面
                SmartWatch watch = watches.get(0);
                if (watch.getRelation().equals("")) {
                    Intent intent = new Intent(LoginActivity.this, FamilySelectActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable(SmartWatchListData.KEY, watch);//传递宝贝信息
                    intent.putExtras(mBundle);
                    startActivity(intent);
                    finish();
                } else {
                    if (watch.getIs_manage() == 0) {
                        Intent intent = new Intent(LoginActivity.this, HomeSecActivity.class);
                        intent.putExtra(IntentAction.OPEN_HONE_FROM, "login");
                        startActivity(intent);
                        finish();
                    } else {
                        //判断对首个手表是否编辑过信息，没有编辑的话进入编辑页面
                        if (CMethod.isEmpty(watch.getPhone_num_binded())) {
                            Intent intent = new Intent(LoginActivity.this, BabyInfoActivity.class);
                            intent.putExtra(IntentAction.KEY_WATCH_ID, watch.getUser_id());
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(LoginActivity.this, HomeSecActivity.class);
                            intent.putExtra(IntentAction.OPEN_HONE_FROM, "login");
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            } else {
                Intent intent = new Intent(LoginActivity.this, BindWatchActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }


    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().equals("")) {
            login_iv_deletepwd.setVisibility(View.GONE);
        } else {
            login_iv_deletepwd.setVisibility(View.VISIBLE);
        }
        if (!CMethod.isEffectivePassword(s.toString())) {
            login_btn_login.setBackgroundResource(R.drawable.shape_button_gray_normal);
            login_btn_login.setTextColor(getResources().getColor(R.color.common_button_middle_text_off_color));
            login_btn_login.setClickable(false);
        } else {
            login_btn_login.setBackgroundResource(R.drawable.button_common_on_selector_green);
            login_btn_login.setTextColor(getResources().getColor(R.color.common_button_middle_text_on_color));
            login_btn_login.setClickable(true);
        }


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }


}
