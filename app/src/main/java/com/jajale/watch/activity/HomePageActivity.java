package com.jajale.watch.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseActivity;
import com.jajale.watch.IntentAction;
import com.jajale.watch.R;
import com.jajale.watch.dialog.DownLoadDialog;
import com.jajale.watch.entity.AppInfo;
import com.jajale.watch.entity.IntentExtra;
import com.jajale.watch.entity.MoveData;
import com.jajale.watch.entity.RequestCode;
import com.jajale.watch.entity.ResultEntity;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.listener.VersionUpdateListener;
import com.jajale.watch.manager.JJLActivityManager;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.DialogUtils;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.ImageUtils;
import com.jajale.watch.utils.InputMethodUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LastLoginUtils;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.MoveDataUtils;
import com.jajale.watch.utils.PhoneSPUtils;
import com.jajale.watch.utils.QiNiuUtils;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;
import com.jajale.watch.utils.VersionUpdateUtils;

import org.json.JSONException;
import org.json.JSONObject;




/**
 * 客户端首页，输入手机号点击进入
 * <p/>
 * <p/>
 * Created by lilonghui on 2015/11/16.
 * Email:lilonghui@bjjajale.com
 */
public class HomePageActivity extends BaseActivity implements View.OnClickListener, TextWatcher {

    private Button homepage_btn_add_jiajiale;
    private ImageView homepage_iv_deletephone;
    private EditText homepage_edit_phone;
    private LoadingDialog loadingDialog;
    private String phone;
    private QiNiuUtils qiNiuUtils;
    private PhoneSPUtils phoneSPUtils;

    private LinearLayout scroll_layout;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    public static final String TAG = "DearDetailFragment";
    String URL = "http://lib.huayinghealth.com/lib-x/?";
    String service_bind = "watch.bind_watch";//绑定手表
    String imei, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_homepage_new);
        initScrollview();

        scroll_layout = (LinearLayout) findViewById(R.id.scroll_layout);
        scroll_layout.getParent().requestDisallowInterceptTouchEvent(false);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(IntentExtra.PHONE)) {
            phone = intent.getStringExtra(IntentExtra.PHONE);
        }
        phoneSPUtils=new PhoneSPUtils(this);
        loadingDialog = new LoadingDialog(this);
        //版本更新及自动登录
        //VersionUpdate();

        JJLActivityManager.getInstance().addActivity(this);
        initView();

        qiNiuUtils = new QiNiuUtils(HomePageActivity.this);
    }

    private void initScrollview() {
        findViewById(R.id.scrollview).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    /**
     * 点击屏幕关闭输入法
     *
     * @param event
     * @return
     */
    public boolean onTouchEvent(MotionEvent event) {
        L.d("guokm", "222");
        if(null != this.getCurrentFocus()){
            /**
             * 点击空白位置 隐藏软键盘
             */
        L.d("guokm","111");
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super .onTouchEvent(event);
    }


    private void VersionUpdate() {
        loadingDialog.show("请稍后...");
        if (CMethod.isNet(HomePageActivity.this)) {
            VersionUpdateUtils.update(HomePageActivity.this, new VersionUpdateListener() {
                @Override
                public void next() {
                    loadingDialog.dismiss();
                }

                @Override
                public void showToast(String message) {

                }

                @Override
                public void downLoad(String url) {
                    loadingDialog.dismiss();
                    loadingDialog.dismiss();
                    DownLoadDialog downLoadDialog = new DownLoadDialog(HomePageActivity.this, url);
                    downLoadDialog.show();
                }


            });
        } else {
            loadingDialog.dismiss();
        }
    }

    /**
     * 初始化view
     */
    private void initView() {
        homepage_btn_add_jiajiale = (Button) findViewById(R.id.homepage_btn_add_jiajiale);
        homepage_iv_deletephone = (ImageView) findViewById(R.id.homepage_iv_deletephone);
        homepage_edit_phone = (EditText) findViewById(R.id.homepage_edit_phone);
        homepage_edit_phone.addTextChangedListener(this);//输入框输入监听
        homepage_btn_add_jiajiale.setOnClickListener(this);//button点击监听
        homepage_iv_deletephone.setOnClickListener(this);//清空输入框
        homepage_btn_add_jiajiale.setClickable(false);

        LastLoginUtils utils = new LastLoginUtils(HomePageActivity.this);
        phone = utils.getPhone();
        if (!CMethod.isEmptyOrZero(phone)) {
            homepage_edit_phone.setText(phone);
            homepage_edit_phone.setSelection(phone.length());
        }

        sp = getApplicationContext().getSharedPreferences("NotDisturb", getApplicationContext().MODE_PRIVATE);// 创建对象
        editor = sp.edit();
        imei = sp.getString("IMEI", "");
        name = sp.getString("Telephone_Number", "");
    }


    @Override
    public void onClick(View v) {
        if (CMethod.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.homepage_btn_add_jiajiale://若手机号已经注册，则进入登录页面，若手机号未注册，则进入注册界面

                if (!CMethod.isFastDoubleClick()) {
                    InputMethodUtils.closeInputMethod(this, homepage_edit_phone);
                    String string_phone = homepage_edit_phone.getText().toString();
//                    getRegisterStateHasMoveData(string_phone);
//                    getRegisterState(string_phone);//检测是否注册，进行判断进入登录页面还是注册页面
                    //保存用户名
                    LastLoginUtils utils = new LastLoginUtils(HomePageActivity.this);
                    utils.setPhone(string_phone);
                    if (sp.getString("Telephone_Number2","").equals(string_phone)){
                        editor.putString("Telephone_Number", string_phone);
                        editor.commit();
//                        Intent intent_login = new Intent(HomePageActivity.this, HomeSecActivity.class);//跳转到首页
                        Intent intent_login = new Intent(HomePageActivity.this, LoginActivity.class);//进入密码验证界面
                        intent_login.putExtra(IntentExtra.PHONE, string_phone);
                        intent_login.putExtra(IntentExtra.IMEI, imei);
                        startActivity(intent_login);
                    } else {
                        Intent intent_bind_watch = new Intent(this, BindWatchActivity.class);//跳转到绑定手表imei页面
                        intent_bind_watch.putExtra(IntentAction.KEY_CLASS_NAME, TAG);
                        Bundle mBundle_bind = new Bundle();
                        mBundle_bind.putString("service_bind", service_bind);
                        mBundle_bind.putString("user_id", string_phone);
                        mBundle_bind.putString("URL", URL);
                        intent_bind_watch.putExtras(mBundle_bind);
                        startActivity(intent_bind_watch);
                    }
                }

                break;
            case R.id.homepage_iv_deletephone://清空输入框
                homepage_edit_phone.setText("");
                break;
        }

    }


    /**
     * java检测是否注册，进行判断进入登录页面还是注册页面
     */
    private void getRegisterStateHasMoveData(final String user_name) {
        loadingDialog.show();

        MoveDataUtils.checkuser(user_name, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                loadingDialog.dismiss();
//                Gson gson = new Gson();
//                final ResultEntity fromJson = gson.fromJson(result, ResultEntity.class);
//

                Gson gson = new Gson();
                MoveData moveData = gson.fromJson(result, MoveData.class);

                //保存用户名
                LastLoginUtils utils = new LastLoginUtils(HomePageActivity.this);
                utils.setPhone(user_name);
                if (moveData!=null){
                    String data=moveData.getData();
                    int code=moveData.getCode();

                    if (code==200) {
                        phoneSPUtils.save("userDataCanMove"+user_name, MoveDataUtils.CAN_MOVE.equals(data));
                        Intent intent_login = new Intent(HomePageActivity.this, LoginActivity.class);
                        intent_login.putExtra(IntentExtra.PHONE, user_name);
                        intent_login.putExtra(MoveDataUtils.NEED_CHECK_USER,MoveDataUtils.CAN_MOVE.equals(data));
                        startActivity(intent_login);
                    } else {
                        Intent intent_register = new Intent(HomePageActivity.this, RegisterActivity.class);
                        intent_register.putExtra(IntentExtra.PHONE, user_name);
                        startActivity(intent_register);
                    }
                }



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


    /**
     * java检测是否注册，进行判断进入登录页面还是注册页面
     */
    private void getRegisterState(final String user_name) {
//        loadingDialog.show();
        //保存用户名
        LastLoginUtils utils = new LastLoginUtils(HomePageActivity.this);
        utils.setPhone(user_name);

        if (sp.getString("Telephone_Number2","").equals(user_name)) { //json请求成功返回数据者进入登录界面否则进入注册界面
            editor.putString("Telephone_Number", user_name);
            editor.commit();
            Intent intent_login = new Intent(HomePageActivity.this, LoginActivity.class);
            intent_login.putExtra(IntentExtra.PHONE, user_name);
            intent_login.putExtra(IntentExtra.IMEI, imei);
            startActivity(intent_login);
        } else {
            Intent intent_register = new Intent(HomePageActivity.this, RegisterActivity.class);
            intent_register.putExtra(IntentExtra.PHONE, user_name);
            startActivity(intent_register);
        }

//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("user_name", user_name);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API, AppConstants.JAVA_IS_REGISTER_URL, jsonObject, new HttpClientListener() {
//            @Override
//            public void onSuccess(String result) {
//                loadingDialog.dismiss();
//                Gson gson = new Gson();
//                final ResultEntity fromJson = gson.fromJson(result, ResultEntity.class);
//                //保存用户名
//                LastLoginUtils utils = new LastLoginUtils(HomePageActivity.this);
//                utils.setPhone(user_name);
//
//                if (fromJson.isSuccess()) {
//                    Intent intent_login = new Intent(HomePageActivity.this, LoginActivity.class);
//                    intent_login.putExtra(IntentExtra.PHONE, user_name);
//                    startActivity(intent_login);
//                } else {
//                    Intent intent_register = new Intent(HomePageActivity.this, RegisterActivity.class);
//                    intent_register.putExtra(IntentExtra.PHONE, user_name);
//                    startActivity(intent_register);
//                }
//
//            }
//
//            @Override
//            public void onFailure(String result) {
//                loadingDialog.dismiss();
//                T.s(result);
//            }
//
//            @Override
//            public void onError() {
//                loadingDialog.dismiss();
//            }
//        });


    }


    @Override
    public void afterTextChanged(Editable s) {
        if (!CMethod.isPhoneNumber(s.toString())) {
//            homepage_btn_add_jiajiale.setBackgroundResource(R.drawable.shape_button_gray_normal);
            homepage_btn_add_jiajiale.setBackgroundResource(R.mipmap.common_btn_unclick);
            homepage_btn_add_jiajiale.setTextColor(getResources().getColor(R.color.common_button_middle_text_off_color));
            homepage_btn_add_jiajiale.setClickable(false);
        } else {
//            homepage_btn_add_jiajiale.setBackgroundResource(R.drawable.button_common_on_selector);
            homepage_btn_add_jiajiale.setBackgroundResource(R.drawable.button_common_on_new_selector);
            homepage_btn_add_jiajiale.setTextColor(getResources().getColor(R.color.common_button_middle_text_on_color));
            homepage_btn_add_jiajiale.setClickable(true);
        }
        if (s.toString().equals("")) {
            homepage_iv_deletephone.setVisibility(View.GONE);
        } else {
            homepage_iv_deletephone.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RequestCode.TAKE_FROM_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    uploadImage(AppInfo.getInstace().getCameraTempPath());
                } else {
                    DialogUtils.uploadPicDialog(HomePageActivity.this);
                }

            case RequestCode.TAKE_FROM_PHOTO:
                if (resultCode == Activity.RESULT_OK) {
                    if (data.getData() != null) {
                        String imagepath = ImageUtils.getPathFromUri(HomePageActivity.this, data.getData());
                        uploadImage(imagepath);
                    }
                } else {
                    DialogUtils.uploadPicDialog(HomePageActivity.this);
                }
                break;
        }
    }

    private void uploadImage(final String imagepath) {
//        loadingDialog.show();

        L.e(imagepath);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = ImageUtils.handleUploadImage(HomePageActivity.this, imagepath, AppConstants.MAX_IMAGE_SIZE);

                if (path != null) {
                    qiNiuUtils.uploadImage(path, new QiNiuUtils.ProgressHandler() {
                        @Override
                        public void progress(double percent) {
                            L.d("上传图片进度 ： " + percent * 100 + "%");
                        }
                    }, new QiNiuUtils.UploadHandler() {
                        @Override
                        public void ok(String filename) {
                            try {
                                //更新profile
//                                L.e("---881228---" + filename);
                                updateProfile(filename);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void error(String err) {
                            L.e(err);
                            loadingDialog.dismiss();
                        }
                    });
                }
            }
        }).start();

    }


    private void updateProfile(final String filename) throws JSONException {
        L.e("更新用户资料");

    }

}
