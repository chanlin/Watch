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
import com.jajale.watch.entity.ResultEntity;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.listener.OnFinishListener;
import com.jajale.watch.listener.SendMessageListener;
import com.jajale.watch.utils.AccountUtils;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.InputMethodUtils;
import com.jajale.watch.utils.LastLoginUtils;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.Md5Util;
import com.jajale.watch.utils.PhoneSPUtils;
import com.jajale.watch.utils.SendMessageCodeUtils;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;
import com.jajale.watch.utils.UserUtils;
import com.jajale.watch.utils.WhiteLoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 重置密码
 * <p/>
 * Created by lilonghui on 2015/11/17.
 * Email:lilonghui@bjjajale.com
 */
public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener {
    private boolean isDestroy = false;
    private Button reset_pwd_btn_getsmscode;
    private Button reset_pwd_btn_login;
    private EditText reset_pwd_edit_pwd;
    private EditText reset_pwd_edit_smscode;
    private ImageView reset_pwd_iv_deletesmscode;
    private ImageView reset_pwd_iv_deletepwd;
//    private boolean timeLock = true;

    private final int MAX_TIMER_INDEX = 60;
    private int secondsInt = MAX_TIMER_INDEX;

    private PhoneSPUtils appSP;

    private LoadingDialog loadingDialog;
    private LastLoginUtils lastLoginUtils;

    private TextView reset_pwd_tv_top;

    private SendMessageCodeUtils sendMessageCodeUtils;

    private String getCodeStr = "请稍后...";
    private String countDownStr = "#SED#s";

    private boolean codeLock = true;
    private boolean passwordLock = true;
    private WhiteLoadingDialog whiteLoadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);

        loadingDialog = new LoadingDialog(this);
        whiteLoadingDialog = new WhiteLoadingDialog(this);
        appSP = new PhoneSPUtils(ResetPasswordActivity.this);
        lastLoginUtils = new LastLoginUtils(ResetPasswordActivity.this);
        initView();


        sendMessageCodeUtils = new SendMessageCodeUtils(new SendMessageListener() {
            @Override
            public void onError(String message) {
                T.s(message);
                reset_pwd_btn_getsmscode.setEnabled(true);
                reset_pwd_btn_getsmscode.setText(getResources().getString(R.string.again_sms_code));
                reset_pwd_btn_getsmscode.setTextColor(getResources().getColor(R.color.white));
                reset_pwd_btn_getsmscode.setBackgroundResource(R.drawable.button_common_on_selector_green);

            }

            @Override
            public void onReadSecond(int second) {
                reset_pwd_tv_top.setVisibility(View.VISIBLE);
                reset_pwd_btn_getsmscode.setText(countDownStr.replace("#SED#", second + ""));
                reset_pwd_btn_getsmscode.setBackgroundResource(R.drawable.shape_button_countdown_unable);
                reset_pwd_btn_getsmscode.setTextColor(getResources().getColor(R.color.green_normal_color));
                reset_pwd_btn_getsmscode.setEnabled(false);

            }

            @Override
            public void onStop() {
                reset_pwd_btn_getsmscode.setEnabled(true);
                reset_pwd_btn_getsmscode.setText(getResources().getString(R.string.again_sms_code));
                reset_pwd_btn_getsmscode.setTextColor(getResources().getColor(R.color.white));
                reset_pwd_btn_getsmscode.setBackgroundResource(R.drawable.button_common_on_selector_green);
            }
        });

        //刚进入页面就发送短信
        sendMessageCodeUtils.send(this, lastLoginUtils.getPhone(), SendMessageCodeUtils.SEND_TYPE_RESETPSSSWORD);


    }

    private void bindListener() {

        reset_pwd_edit_pwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {
                    if (s.toString().trim().length() > 5 && s.toString().trim().length() < 17) {
                        passwordLock = false;
                        reset_pwd_iv_deletepwd.setVisibility(View.VISIBLE);

                        if (!codeLock) {
                            reset_pwd_btn_login.setEnabled(true);
                        }


                    } else {
                        codeLock = true;
                        reset_pwd_iv_deletepwd.setVisibility(View.GONE);
                        reset_pwd_btn_login.setEnabled(false);

                    }

                }
            }
        });


        reset_pwd_edit_smscode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {
                    if (s.toString().trim().length() == 6) {
                        codeLock = false;
                        reset_pwd_iv_deletesmscode.setVisibility(View.VISIBLE);
                        if (!passwordLock) {
                            reset_pwd_btn_login.setEnabled(true);
                        }

                    } else if (s.toString().trim().length() < 6) {
                        codeLock = true;
                        reset_pwd_iv_deletesmscode.setVisibility(View.GONE);
                        reset_pwd_btn_login.setEnabled(false);
                    }

                }
            }
        });


    }


    /**
     * 初始化view
     */
    private void initView() {
        initTitleView();
        reset_pwd_btn_getsmscode = (Button) findViewById(R.id.reset_pwd_btn_getsmscode);
//        reset_pwd_btn_getsmscode.setEnabled();
        reset_pwd_btn_login = (Button) findViewById(R.id.reset_pwd_btn_login);
        reset_pwd_edit_pwd = (EditText) findViewById(R.id.reset_pwd_edit_pwd);
        reset_pwd_edit_smscode = (EditText) findViewById(R.id.reset_pwd_edit_smscode);
        reset_pwd_iv_deletesmscode = (ImageView) findViewById(R.id.reset_pwd_iv_deletesmscode);
        reset_pwd_iv_deletepwd = (ImageView) findViewById(R.id.reset_pwd_iv_deletepwd);

        reset_pwd_tv_top = (TextView) findViewById(R.id.reset_pwd_tv_top);

        reset_pwd_edit_pwd.addTextChangedListener(mTextwatcher_pwd);
        reset_pwd_edit_smscode.addTextChangedListener(mTextwatcher_smscode);
        reset_pwd_btn_getsmscode.setOnClickListener(this);
        reset_pwd_btn_login.setOnClickListener(this);
        reset_pwd_iv_deletepwd.setOnClickListener(this);
        reset_pwd_iv_deletesmscode.setOnClickListener(this);
        reset_pwd_btn_login.setClickable(false);

        reset_pwd_tv_top.setText("验证码已发送到" + lastLoginUtils.getPhone());
        reset_pwd_tv_top.setVisibility(View.INVISIBLE);

        reset_pwd_btn_getsmscode.setText(getCodeStr);


        InputMethodUtils.openInputMethod(this, reset_pwd_edit_smscode);
    }

    /**
     * 初始化title view
     */
    private void initTitleView() {
        View title = findViewById(R.id.title);
        TextView midTitle = (TextView) title.findViewById(R.id.tv_middle);
        midTitle.setText(getResources().getString(R.string.reset_pwd_title_text));
        ImageView iv_left = (ImageView) title.findViewById(R.id.iv_left);
        iv_left.setImageResource(R.drawable.title_goback_selector);
        iv_left.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (CMethod.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {

            case R.id.reset_pwd_btn_login://点击登录

                InputMethodUtils.closeInputMethod(this, reset_pwd_edit_smscode);
                String sms_code = reset_pwd_edit_smscode.getText().toString();
                String pwd = reset_pwd_edit_pwd.getText().toString();

                LastLoginUtils utils = new LastLoginUtils(ResetPasswordActivity.this);

                if (CMethod.isNet(ResetPasswordActivity.this)) {
                    userResetPassword(utils.getPhone(), Md5Util.string2MD5(pwd), sms_code);

                } else {
                    T.s("网络不给力");
                }


                break;

            case R.id.reset_pwd_btn_getsmscode://获取短信验证码
//                if (timeLock) {
                reset_pwd_btn_getsmscode.setText(getCodeStr);
                reset_pwd_btn_getsmscode.setBackgroundResource(R.drawable.shape_button_gray_normal);
                sendMessageCodeUtils.send(this, lastLoginUtils.getPhone(), SendMessageCodeUtils.SEND_TYPE_RESETPSSSWORD);
//                    sendMessageCodeUtils.send();
//                }
                break;
            case R.id.reset_pwd_iv_deletesmscode://清空短信验证码
                reset_pwd_edit_smscode.setText("");
                break;
            case R.id.reset_pwd_iv_deletepwd://清空密码
                reset_pwd_edit_pwd.setText("");
                break;
            case R.id.iv_left://返回键
                finish();
                break;
        }
    }












    /**
     * java重置密码
     */
    private void userResetPassword(final String userName, final String passWord, String num) {
        loadingDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_name", userName);
            jsonObject.put("password", passWord);
            jsonObject.put("num", num);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API,AppConstants.JAVA_RESET_PASSWORD_URL, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                final ResultEntity fromJson = gson.fromJson(result, ResultEntity.class);

                //存储账号信息
                AccountUtils.updateAccount(userName, passWord);

                //存储user
                UserUtils.updateUser(fromJson.getUser_id(), fromJson.getWatch_bind() + "", new OnFinishListener() {
                    @Override
                    public void onFinish() {
                        lastLoginUtils.setPhone(userName);
                        lastLoginUtils.setPassword(passWord);
                        lastLoginUtils.setUserId(fromJson.getUser_id());
                        lastLoginUtils.login();

                        if (fromJson.getWatch_bind() == 0) {
                            loadingDialog.dismiss();
                            Intent intent = new Intent(ResetPasswordActivity.this, BindWatchActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            //获取手表信息，无信息，则进入编辑
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    SmartWatchUtils.getWatchListFromNetWork(fromJson.getUser_id(), new ListListener<SmartWatch>() {
//                                        @Override
//                                        public void onError(String message) {
//                                            loadingDialog.dismiss();
//                                        }
//
//                                        @Override
//                                        public void onSuccess(List<SmartWatch> smartWatches) {
//                                            Intent intent = new Intent(ResetPasswordActivity.this, HomeSecActivity.class);
//                                            intent.putExtra(IntentAction.OPEN_HONE_FROM,"resetpw");
//                                            intent.putExtra(IntentAction.OPEN_HONE_ID,id);
//                                            intent.putExtra(IntentAction.OPEN_HONE_PW,pw);
//                                            startActivity(intent);
//                                            finish();
//                                        }
//                                    });

                                    Intent intent = new Intent(ResetPasswordActivity.this, HomeSecActivity.class);
                                    intent.putExtra(IntentAction.OPEN_HONE_FROM, "resetpw");
                                    startActivity(intent);
                                    finish();


                                }
                            });
                        }
                    }
                });

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


    //短信验证码编辑框监听
    TextWatcher mTextwatcher_smscode = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
            // mTextView.setText(s);//将输入的内容实时显示
        }

        @SuppressWarnings("deprecation")
        public void afterTextChanged(Editable s) {

            if (s.toString().equals("")) {
                reset_pwd_iv_deletesmscode.setVisibility(View.INVISIBLE);
            } else {
                reset_pwd_iv_deletesmscode.setVisibility(View.VISIBLE);
            }

            String edit_pwd_text = reset_pwd_edit_pwd.getText().toString();
            if (isEffectivePassword(edit_pwd_text)) {
                if (isprovingNumber(s.toString())) {
                    reset_pwd_btn_login.setBackgroundResource(R.drawable.button_common_on_selector_green);
                    reset_pwd_btn_login.setTextColor(getResources().getColor(R.color.common_button_middle_text_on_color));
                    reset_pwd_btn_login.setClickable(true);

                } else {
                    reset_pwd_btn_login.setBackgroundResource(R.drawable.shape_button_gray_normal);
                    reset_pwd_btn_login.setTextColor(getResources().getColor(R.color.common_button_middle_text_off_color));
                    reset_pwd_btn_login.setClickable(false);
                }
            } else {
                reset_pwd_btn_login.setClickable(false);
            }

        }
    };
    //密码编辑框监听
    TextWatcher mTextwatcher_pwd = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // TODO Auto-generated method stub
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // TODO Auto-generated method stub
            // mTextView.setText(s);//将输入的内容实时显示
        }

        @SuppressWarnings("deprecation")
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

            if (s.toString().equals("")) {
                reset_pwd_iv_deletepwd.setVisibility(View.INVISIBLE);
            } else {
                reset_pwd_iv_deletepwd.setVisibility(View.VISIBLE);
            }
            String edit_name_text = reset_pwd_edit_smscode.getText().toString();
            if (isprovingNumber(edit_name_text)) {
                if (isEffectivePassword(s.toString())) {
                    reset_pwd_btn_login.setBackgroundResource(R.drawable.button_common_on_selector_green);
                    reset_pwd_btn_login.setTextColor(getResources().getColor(R.color.common_button_middle_text_on_color));
                    reset_pwd_btn_login.setClickable(true);
                } else {
                    reset_pwd_btn_login.setBackgroundResource(R.drawable.shape_button_gray_normal);
                    reset_pwd_btn_login.setTextColor(getResources().getColor(R.color.common_button_middle_text_off_color));
                    reset_pwd_btn_login.setClickable(false);
                }
            } else {
                reset_pwd_btn_login.setClickable(false);
            }
        }
    };

    /**
     * 检查是否为有效的验证码
     *
     * @param mobile
     * @return
     */
    public boolean isprovingNumber(String mobile) {

        String phoneRegex = "[0-9]{6}";// 验证码格式正则表达式验证

        Pattern regex;
        Matcher matcher;

        regex = Pattern.compile(phoneRegex);
        matcher = regex.matcher(mobile);

        if (matcher.matches()) {
            return true;
        }

        return false;
    }

    /**
     * 检验输入的密码是否为符合要求的
     *
     * @param password
     * @return 符合要求的为true
     */
    private boolean isEffectivePassword(String password) {
        String str = "^[A-Za-z0-9]{6,16}+$";//只能为26个英文字母或数字组成的字符串
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(password);
        return m.matches();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }


}
