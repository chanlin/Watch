package com.jajale.watch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
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
import com.jajale.watch.entity.IntentExtra;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 注册界面
 * <p/>
 * Created by lilonghui on 2015/11/16.
 * Email:lilonghui@bjjajale.com
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private Button register_btn_getsmscode;
    private Button register_btn_register;
    private EditText register_edit_pwd;
    private EditText register_edit_smscode;
    private ImageView register_iv_deletesmscode;
    private ImageView register_iv_deletepwd;
    private TextView tv_register_artical ;

    private LoadingDialog loadingDialog;

    private SendMessageCodeUtils sendMessageCodeUtils;
    private LastLoginUtils lastLoginUtils;

    private PhoneSPUtils phoneSPUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        lastLoginUtils = new LastLoginUtils(RegisterActivity.this);
        phoneSPUtils=new PhoneSPUtils(this);
        loadingDialog = new LoadingDialog(this);
        initView();


        sendMessageCodeUtils = new SendMessageCodeUtils(new SendMessageListener() {
            @Override
            public void onError(String message) {
                T.s(message);
                register_btn_getsmscode.setEnabled(true);
                register_btn_getsmscode.setText(getResources().getString(R.string.again_sms_code));
                register_btn_getsmscode.setTextColor(getResources().getColor(R.color.white));
                register_btn_getsmscode.setBackgroundResource(R.drawable.button_common_on_selector_green);
            }

            @Override
            public void onReadSecond(int second) {
                register_btn_getsmscode.setText(second + "s");
                register_btn_getsmscode.setEnabled(false);
                register_btn_getsmscode.setTextColor(getResources().getColor(R.color.get_msg_code_bg_color));
            }

            @Override
            public void onStop() {
                register_btn_getsmscode.setEnabled(true);
                register_btn_getsmscode.setText(getResources().getString(R.string.again_sms_code));
                register_btn_getsmscode.setTextColor(getResources().getColor(R.color.white));
                register_btn_getsmscode.setBackgroundResource(R.drawable.button_common_on_selector_green);
            }
        });
    }

    /**
     * 初始化view
     */
    private void initView() {
        initTitleView();
        register_btn_getsmscode = (Button) findViewById(R.id.register_btn_getsmscode);
        register_btn_register = (Button) findViewById(R.id.register_btn_register);
        register_edit_pwd = (EditText) findViewById(R.id.register_edit_pwd);
        register_edit_smscode = (EditText) findViewById(R.id.register_edit_smscode);
        register_iv_deletesmscode = (ImageView) findViewById(R.id.register_iv_deletesmscode);
        register_iv_deletepwd = (ImageView) findViewById(R.id.register_iv_deletepwd);
        register_edit_pwd.addTextChangedListener(mTextwatcher_pwd);
        register_edit_smscode.addTextChangedListener(mTextwatcher_smscode);
        tv_register_artical = (TextView) findViewById(R.id.tv_register_artical);
        String  text_accident="点击“完成注册”表示您同意<font color=\"#2fbbef\">" + "《用户使用协议》"+ "</font>";
        tv_register_artical.setText(Html.fromHtml(text_accident));
        tv_register_artical.setOnClickListener(this);
        register_btn_getsmscode.setOnClickListener(this);
        register_btn_register.setOnClickListener(this);
        register_iv_deletepwd.setOnClickListener(this);
        register_iv_deletesmscode.setOnClickListener(this);
        register_btn_register.setClickable(false);

        InputMethodUtils.openInputMethod(this, register_edit_smscode);
    }

    /**
     * 初始化title view
     */
    private void initTitleView() {
        View title = findViewById(R.id.title);
        TextView midTitle = (TextView) title.findViewById(R.id.tv_middle);
        midTitle.setText(getResources().getString(R.string.register_title_text));
        ImageView iv_left = (ImageView) title.findViewById(R.id.iv_left);
        iv_left.setImageResource(R.drawable.title_goback_selector);
        iv_left.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (CMethod.isFastDoubleClick()){
            return;
        }
        switch (v.getId()) {
            case R.id.tv_register_artical:
                //打开条款页

                Intent i = new Intent(RegisterActivity.this,InsuranceArticleActivity.class);
                i.putExtra("openID",5);
//                i.putExtra("from","register");
                i.putExtra("openTitle","用户使用协议");
                startActivity(i);

                break;
            case R.id.register_btn_register://点击注册按钮

                InputMethodUtils.closeInputMethod(this, register_edit_smscode);
                String sms_code = register_edit_smscode.getText().toString();
                String pwd = register_edit_pwd.getText().toString();

                String phone = getIntent().getStringExtra(IntentExtra.PHONE);
                if (!CMethod.isEmpty(phone)) {
                    userRegister(phone, Md5Util.string2MD5(pwd), sms_code, "2000");
                } else {
                    finish();
                }
                break;

            case R.id.register_btn_getsmscode://获取短信验证码
                    register_btn_getsmscode.setBackgroundResource(R.drawable.shape_button_gray_normal);
                    sendMessageCodeUtils.send(this, lastLoginUtils.getPhone(), SendMessageCodeUtils.SEND_TYPE_REGISTER);// 读秒（在发送短信验证码成功后开始）
                    sendMessageCodeUtils.send();
                break;
            case R.id.register_iv_deletesmscode://清空短信验证码
                register_edit_smscode.setText("");
                break;
            case R.id.register_iv_deletepwd://清空密码
                register_edit_pwd.setText("");
                break;
            case R.id.iv_left://返回键
                finish();
                break;
        }
    }



    /**
     * java注册
     */
    private void userRegister(final String userName, final String passWord, String num, String channel) {
        loadingDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_name", userName);
            jsonObject.put("password", passWord);
            jsonObject.put("num", num);
            jsonObject.put("channel", channel);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API, AppConstants.JAVA_REGISTER_URL, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                final ResultEntity fromJson = gson.fromJson(result, ResultEntity.class);
                //存储账号信息
                AccountUtils.updateAccount(userName, passWord);
                phoneSPUtils.save("userDataCanMove"+userName, false);
                //存储user
                UserUtils.updateUser(fromJson.getUser_id(), fromJson.getWatch_bind() + "", new OnFinishListener() {
                    @Override
                    public void onFinish() {


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (loadingDialog != null && loadingDialog.isShowing()) {
                                    loadingDialog.dismiss();
                                }
                            }
                        });

                        //保存用户注册状态
                        //保存用户密码(加密后)
                        LastLoginUtils lastLoginUtils = new LastLoginUtils(RegisterActivity.this);
                        lastLoginUtils.setUserId(fromJson.getUser_id());
                        lastLoginUtils.setPassword(passWord);
                        lastLoginUtils.login();

                        Intent intent = new Intent(RegisterActivity.this, BindWatchActivity.class);
                        intent.putExtra(IntentAction.KEY_USER_ID, fromJson.getUser_id());
                        startActivity(intent);
                        finish();
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
                register_iv_deletesmscode.setVisibility(View.INVISIBLE);
            } else {
                register_iv_deletesmscode.setVisibility(View.VISIBLE);
            }

            String edit_pwd_text = register_edit_pwd.getText().toString();
            if (isEffectivePassword(edit_pwd_text)) {
                if (isprovingNumber(s.toString())) {
                    register_btn_register.setBackgroundResource(R.drawable.button_common_on_selector_green);
                    register_btn_register.setTextColor(getResources().getColor(R.color.common_button_middle_text_on_color));
                    register_btn_register.setClickable(true);

                } else {
                    register_btn_register.setBackgroundResource(R.drawable.shape_button_gray_normal);
                    register_btn_register.setTextColor(getResources().getColor(R.color.common_button_middle_text_off_color));
                    register_btn_register.setClickable(false);
                }
            } else {
                register_btn_register.setClickable(false);
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
                register_iv_deletepwd.setVisibility(View.INVISIBLE);
            } else {
                register_iv_deletepwd.setVisibility(View.VISIBLE);
            }
            String edit_name_text = register_edit_smscode.getText().toString();
            if (isprovingNumber(edit_name_text)) {
                if (isEffectivePassword(s.toString())) {
                    register_btn_register.setBackgroundResource(R.drawable.button_common_on_selector_green);
                    register_btn_register.setTextColor(getResources().getColor(R.color.common_button_middle_text_on_color));
                    register_btn_register.setClickable(true);
                } else {
                    register_btn_register.setBackgroundResource(R.drawable.shape_button_gray_normal);
                    register_btn_register.setTextColor(getResources().getColor(R.color.common_button_middle_text_off_color));
                    register_btn_register.setClickable(false);
                }
            } else {
                register_btn_register.setClickable(false);
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

        return matcher.matches();
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
}
