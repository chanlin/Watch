package com.jajale.watch.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseActivity;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.IntentAction;
import com.jajale.watch.R;
import com.jajale.watch.entity.SmartWatchListData;
import com.jajale.watch.entitydb.SmartWatch;
import com.jajale.watch.entityno.UMeventId;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.listener.SimpleClickListener;
import com.jajale.watch.listener.SingleStringListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.DialogUtils;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.Md5Util;
import com.jajale.watch.utils.NotificationUtils;
import com.jajale.watch.utils.SmartWatchUtils;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 宝贝信息
 * <p/>
 * Created by lilonghui on 2015/11/17.
 * Email:lilonghui@bjjajale.com
 */
public class BabyInfoActivity extends BaseActivity implements View.OnClickListener {

    private String notFill = "请填写";
    private boolean isDestroy = false;
    private boolean isCanSave = false;

    @InjectView(R.id.iv_left)
    ImageView ivLeft;
    @InjectView(R.id.tv_left_2)
    TextView tvLeft2;
    @InjectView(R.id.tv_middle)
    TextView tvMiddle;
    @InjectView(R.id.tv_right)
    TextView tvRight;
    @InjectView(R.id.body_info_head_image)
    ImageView bodyInfoHeadImage;
    @InjectView(R.id.bady_info_rl_head)
    RelativeLayout badyInfoRlHead;
    @InjectView(R.id.bady_info_tv_name)
    TextView badyInfoTvName;
    @InjectView(R.id.tonext_iv)
    ImageView tonextIv;
    @InjectView(R.id.bady_info_rl_name)
    RelativeLayout badyInfoRlName;
    @InjectView(R.id.radio_boy)
    RadioButton radioBoy;
    @InjectView(R.id.radio_girl)
    RadioButton radioGirl;
    @InjectView(R.id.radioGroup_meitianjiancha)
    RadioGroup radioGroupMeitianjiancha;
    @InjectView(R.id.bady_info_rl_sex)
    RelativeLayout badyInfoRlSex;
    @InjectView(R.id.bady_info_tv_phone)
    TextView badyInfoTvPhone;
    @InjectView(R.id.bady_info_rl_phone)
    RelativeLayout badyInfoRlPhone;
    @InjectView(R.id.bady_info_tv_birthday)
    TextView badyInfoTvBirthday;
    @InjectView(R.id.bady_info_rl_birthday)
    RelativeLayout badyInfoRlBirthday;
    @InjectView(R.id.bady_info_btn_next)
    Button badyInfoBtnNext;
    @InjectView(R.id.watch_tools_btn_remove_bind)
    Button watch_tools_btn_remove_bind;
    private LoadingDialog loadingDialog;
    private SmartWatch watch;
    private List<SmartWatch> watches;
    private int present_position = 0;
    private BaseApplication baseApplication;
    private String user_id, URL, service_unbind, imei, header_img_url, nick_name, sex, phone, birthday1;
    RequestQueue queue;
    SharedPreferences sp;
    SharedPreferences.Editor editor,editor1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_babyinfo);
        ButterKnife.inject(this);

        tvMiddle.setText(getResources().getString(R.string.baby_info_title_text));
        loadingDialog = new LoadingDialog(this);
        baseApplication = (BaseApplication) getApplication();
        present_position = baseApplication.getPresent_position();
        List<SmartWatch> watches = SmartWatchUtils.getWatchList();
        setBabyData(watches);

        queue = Volley.newRequestQueue(getApplicationContext());
        sp = getApplicationContext().getSharedPreferences("NotDisturb", getApplicationContext().MODE_PRIVATE);// 创建对象
        editor = sp.edit();
        Bundle bundle=getIntent().getExtras();
        user_id = bundle.getString("user_id");//读出数据
        URL = bundle.getString("URL");
        imei = bundle.getString("imei");
        service_unbind = bundle.getString("service_unbind");

        header_img_url = sp.getString("header_img_url", "");
        nick_name = sp.getString("nick_name", "");
        sex = sp.getString("sex", "");
        phone = sp.getString("phone", "");
        birthday1 = sp.getString("birthday", "");
        if (!"".equals(nick_name)) {
            badyInfoTvName.setText(nick_name);
            badyInfoTvPhone.setText(phone);
            badyInfoTvBirthday.setText(birthday1);
        }
        if (sex.equals("0")) {
            bodyInfoHeadImage.setImageResource(R.mipmap.head_image_girl);
            radioBoy.setChecked(false);
            radioGirl.setChecked(true);
        }

        watch = getIntent().getParcelableExtra(SmartWatchListData.KEY);

        if (watch != null)//entity不为null，则为编辑状态，反之为创建状态
        {
            badyInfoBtnNext.setVisibility(View.GONE);//完成button隐藏
            badyInfoTvName.setText(watch.getNick_name());
            badyInfoTvPhone.setText(watch.getPhone_num_binded());
            badyInfoTvBirthday.setText(watch.getBirthday());
            if (watch.getSex()==0) {
                radioBoy.setChecked(false);
                radioGirl.setChecked(true);
                bodyInfoHeadImage.setImageResource(R.mipmap.head_image_girl);
            } else {
                radioBoy.setChecked(true);
                radioGirl.setChecked(false);
                bodyInfoHeadImage.setImageResource(R.mipmap.head_image_boy);
            }

            tvRight.setText(getResources().getString(R.string.save));
            tvRight.setOnClickListener(this);
            ivLeft.setImageResource(R.drawable.title_goback_selector);
            ivLeft.setOnClickListener(this);
        } else {
//            watch_tools_btn_remove_bind.setVisibility(View.GONE);
            watch_tools_btn_remove_bind.setVisibility(View.VISIBLE);
            badyInfoBtnNext.setVisibility(View.VISIBLE);
            badyInfoBtnNext.setOnClickListener(this);
        }

        radioBoy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bodyInfoHeadImage.setImageResource(R.mipmap.head_image_girl);
            }
        });
        radioGirl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bodyInfoHeadImage.setImageResource(R.mipmap.head_image_boy);

            }
        });

        isSetSuccess();
        bindListener();
    }

    private void setBabyData(List<SmartWatch> mWatches) {
        if (mWatches != null) {
            watches = mWatches;
        } else {
            watches = SmartWatchUtils.getWatchList();
        }
        if (watches == null)
            return;
        if (watches.size() != 0) {
            if (present_position < watches.size()) {
                watch = watches.get(present_position);
            } else {
                watch = watches.get(0);
            }

        } else {
            L.e("list.size = 0");
            Intent intent = new Intent(BabyInfoActivity.this, BindWatchActivity.class);
            startActivity(intent);
            BabyInfoActivity.this.finish();
        }

    }

    private void bindListener() {
        badyInfoRlHead.setOnClickListener(this);
        badyInfoRlName.setOnClickListener(this);
        badyInfoRlPhone.setOnClickListener(this);
        badyInfoRlBirthday.setOnClickListener(this);
        watch_tools_btn_remove_bind.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        if (CMethod.isFastDoubleClick()) {
            return;
        }

        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.bady_info_rl_name://昵称
                DialogUtils.changeNickname(this, new SingleStringListener() {
                    @Override
                    public void choiced(String result) {

                        badyInfoTvName.setText(result);
                        isSetSuccess();

                    }
                });
                break;
            case R.id.bady_info_rl_phone://手表号码
                DialogUtils.changePhone(this, new SingleStringListener() {
                    @Override
                    public void choiced(String result) {
                        badyInfoTvPhone.setText(result);
                        isSetSuccess();
                    }
                });
                break;
            case R.id.bady_info_rl_birthday://生日

                String birthday = badyInfoTvBirthday.getText().toString();
                if (birthday.equals(getResources().getString(R.string.fill_in))) {
                    birthday = CMethod.getFullDay();
                }
                DialogUtils.birthdayDialog(this, getResources().getString(R.string.birthday), birthday, 20,new SingleStringListener() {
                    @Override
                    public void choiced(String result) {
                        badyInfoTvBirthday.setText(result);
                        isSetSuccess();
                    }
                });

                break;
            case R.id.bady_info_btn_next://完成

                userAddBabyInfo(getIntent().getStringExtra(IntentAction.KEY_WATCH_ID)
                        , ""
                        , badyInfoTvName.getText().toString()
                        , radioBoy.isChecked() ? "1" : "0"
                        , badyInfoTvPhone.getText().toString(), badyInfoTvBirthday.getText().toString());
                break;
            case R.id.tv_right://保存宝贝信息
                if (watch != null) {

                    if (watch.getIs_manage()==0) {
                        Toast.makeText(BabyInfoActivity.this, "您没有编辑宝贝的权限", Toast.LENGTH_SHORT).show();
                    } else if (!isCanSave) {
                        Toast.makeText(BabyInfoActivity.this, getResources().getString(R.string.not_fill_data), Toast.LENGTH_SHORT).show();
                    } else {

                        if (CMethod.isNet(BabyInfoActivity.this)) {
                            userAddBabyInfo(watch.getUser_id(), ""
                                    , badyInfoTvName.getText().toString()
                                    , radioBoy.isChecked() ? "1" : "0"
                                    , badyInfoTvPhone.getText().toString()
                                    , badyInfoTvBirthday.getText().toString());
                        } else {
                            T.s(getResources().getString(R.string.no_network));
                        }
                    }
                }
                break;
            case R.id.watch_tools_btn_remove_bind:
                if (!CMethod.isNet(getApplicationContext())) {
                    T.s(getResources().getString(R.string.no_network));
                    return;
                }
                //解绑手表
                DialogUtils.removeBindDialog(BabyInfoActivity.this, new SimpleClickListener() {
                    @Override
                    public void ok() {
                        unbindThisWatch();
                        editor.putString("header_img_url", "");
                        editor.putString("nick_name", "");
                        editor.putString("sex", "");
                        editor.putString("phone", "");
                        editor.putString("birthday", "");
                        editor.putString("relation", "");
                        editor.commit();
                        Intent intent_login = new Intent(BabyInfoActivity.this, HomeSecActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//跳转到首页
                        startActivity(intent_login);
                    }

                    @Override
                    public void cancle() {

                    }
                });
                MobclickAgent.onEvent(BabyInfoActivity.this, UMeventId.UMENG_NUMBER_OF_SOLUTIONS);
                break;
        }

    }

    /**
     * java删除管理员(此页面为解除绑定)
     */
    private void unbindThisWatch() {
        loadingDialog.show();
        editor.putString("IMEI", "");
//        editor.putString("Telephone_Number", "");
        editor.commit();
        String sign = Md5Util.stringmd5(imei, service_unbind, user_id);
        String path = URL + "service=" + service_unbind + "&imei=" + imei + "&user_id=" + user_id + "&sign=" +sign;
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
                    } else if (code == 1) {
                        T.s(getResources().getString(R.string.unbundling));
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
        //清除listview数据
        SharedPreferences sp1 = getSharedPreferences("file1", MODE_PRIVATE);
        editor1 = sp1.edit();
        editor1.putString("num","");
        editor1.commit();
    }


    /**
     * java编辑宝贝信息
     */
    private void userAddBabyInfo(String user_id, String  header_img_url,String nick_name, String sex, String phone,String birthday) {
        loadingDialog.show();
        editor.putString("header_img_url", header_img_url);
        editor.putString("nick_name", nick_name);
        editor.putString("sex", sex);
        editor.putString("phone", phone);
        editor.putString("birthday", birthday);
        editor.commit();
        new Handler().postDelayed((new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismiss();
            }
        }), 1000);
        Intent intent = new Intent(BabyInfoActivity.this, HomeSecActivity.class);//跳转到首页
        intent.putExtra(IntentAction.OPEN_HONE_FROM,"babyInfo");
        startActivity(intent);
        finish();
    }





    /**
     * 检查是否为有效的手机号码
     *
     * @param mobile
     * @return
     */
    public static boolean isPhoneNumber(String mobile) {

        if (!CMethod.isEmptyOrZero(mobile)){
            return true ;
        }
        return false;
    }

    /**
     * 判断button是否可点击
     */
    private void isSetSuccess() {

        String birthDay = badyInfoTvBirthday.getText().toString();
        String phone = badyInfoTvPhone.getText().toString();
        String nickName = badyInfoTvName.getText().toString();
        if (!birthDay.equals("") && !birthDay.equals(notFill) && isPhoneNumber(phone) && !nickName.equals(notFill)) {
            isCanSave = true;
            badyInfoBtnNext.setBackgroundResource(R.drawable.button_common_on_selector_green);
            badyInfoBtnNext.setTextColor(getResources().getColor(R.color.common_button_middle_text_on_color));
            badyInfoBtnNext.setClickable(true);
        } else {
            badyInfoBtnNext.setBackgroundResource(R.drawable.shape_button_gray_normal);
            badyInfoBtnNext.setTextColor(getResources().getColor(R.color.common_button_middle_text_off_color));
            badyInfoBtnNext.setClickable(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }
}
