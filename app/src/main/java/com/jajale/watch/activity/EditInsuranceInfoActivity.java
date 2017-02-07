package com.jajale.watch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseActivity;
import com.jajale.watch.R;
import com.jajale.watch.entityno.SimpleResult;
import com.jajale.watch.entityno.UMeventId;
import com.jajale.watch.listener.AreaListener;
import com.jajale.watch.listener.SimpleResponseListener;
import com.jajale.watch.listener.SingleStringListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.DialogUtils;
import com.jajale.watch.utils.InsuranceUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.T;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 编辑
 * Created by lilonghui on 2015/11/28.
 * Email:lilonghui@bjjajale.com
 */
public class EditInsuranceInfoActivity extends BaseActivity implements OnClickListener {

    private String [] title_tc1 = {"您与儿童的关系","性别","性别"};
    private String [] title_et_item = {"姓名","证件号码","移动电话","备用移动电话","邮箱地址","","","姓名","证件号码"};
    private String [] content_et_item_hint = {"请填写您的真实姓名","请填写您的证件号码","请填写您的联系方式","选填","请填写您的邮箱地址","请填写您所在的街道","请填写您的小区和门牌号","请填写儿童的真实姓名","请填写您的证件号"};

    private String [] rb_title = {"爸爸","妈妈","男","女","男","女"};


    private String [] title_dialog_item = {"出生日期","证件类型","投保人地址","出生日期","证件类型","现居城市"};
    private String [] content_dialog_hint = {"请选择您的出生日期","请选择您的证件类型","请选择省、市、区","请选择您的出生日期","请选择您的证件类型","请选择儿童的所在城市"};

    private int [] layouts_tc1 = {R.id.layout_two_choosen_parent,R.id.layout_two_choosen_gender,R.id.edit_insurance_layout_baby_gender};

    private int [] layouts_et = {R.id.edit_insurance_layout_name,R.id.edit_insurance_layout_idcard,R.id.edit_insurance_layout_phone, R.id.edit_insurance_layout_phone_sec,R.id.edit_insurance_layout_email,R.id.edit_insurance_layout_street,R.id.edit_insurance_layout_door,R.id.edit_insurance_layout_baby_name,R.id.edit_insurance_layout_baby_id_no};

    private int [] layouts_dialog={R.id.edit_insurance_layout_birthday,R.id.edit_insurance_layout_id_type,R.id.edit_insurance_layout_city,R.id.edit_insurance_layout_baby_birthday,R.id.edit_insurance_layout_baby_id_type,R.id.edit_insurance_layout_baby_current_city };

    private List<EditText> filds_et = new ArrayList<EditText>();
    private List<RadioButton> filds_radio = new ArrayList<RadioButton>();
    private List<TextView> filds_text = new ArrayList<TextView>();

    private String watchID = "";
    private String IMEI = "";



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
    @InjectView(R.id.insurance_btn_next)
    Button insuranceBtnNext;
    private LoadingDialog loadingdialog;
    //    @InjectView(R.id.tv_insurance_et_city)
//    TextView tv_insurance_et_city;
//    @InjectView(R.id.tv_insurance_baby_city_tv)
//    TextView tv_insurance_baby_city_tv;

//    @InjectView(R.id.radio_father)
//    RadioButton radio_father;

//    @InjectView(R.id.radio_mother)
//    RadioButton radio_mother;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_insurance);
        ButterKnife.inject(this);
        loadingdialog = new LoadingDialog(this);

        tvMiddle.setText(getResources().getString(R.string.insurance_title_text));
        ivLeft.setImageResource(R.drawable.title_goback_selector);
        ivLeft.setOnClickListener(this);
//        tv_insurance_et_city.setOnClickListener(this);
//        tv_insurance_baby_city_tv.setOnClickListener(this);
        insuranceBtnNext.setOnClickListener(this);

        watchID = getIntent().getStringExtra("watchID");
        IMEI  = getIntent().getStringExtra("IMEI");


        for (int i = 0; i < layouts_tc1.length; i++) {

            View layout = findViewById(layouts_tc1[i]);
            TextView tv_title = (TextView) layout.findViewById(R.id.tv_two_choosen_title);
            tv_title.setText(title_tc1[i]);
            RadioButton left = (RadioButton) layout.findViewById(R.id.rb_two_choosen_left);
            filds_radio.add(left);
            RadioButton right = (RadioButton) layout.findViewById(R.id.rb_two_choosen_right);
            filds_radio.add(right);
        }

        for (int i = 0 ; i <filds_radio.size();i++){
            RadioButton temp = filds_radio.get(i);
            temp.setText(rb_title[i]);
        }



        for (int i = 0 ; i < layouts_dialog.length;i++){
            View layout = findViewById(layouts_dialog[i]);
            TextView tv_title = (TextView) layout.findViewById(R.id.tv_insurance_dialog_title);
            tv_title.setText(title_dialog_item[i]);

            TextView tv_content = (TextView) layout.findViewById(R.id.tv_insurance_dialog_content);
            tv_content.setText(content_dialog_hint[i]);
            filds_text.add(tv_content);
        }

        for (int i = 0 ; i < layouts_et.length;i++){
            View layout = findViewById(layouts_et[i]);
            TextView tv_title = (TextView) layout.findViewById(R.id.edit_insurance_tv);
            tv_title.setText(title_et_item[i]);

            EditText content = (EditText) layout.findViewById(R.id.edit_insurance_et);
            content.setHint(content_et_item_hint[i]);
            if (i== 1 || i == 8 ){
                content.setMaxWidth(18);
            }else if( i == 2 || i == 3){
                content.setMaxWidth(11);
            }


            filds_et.add(content);
        }



        bindListener();

//        initValue();

    }


    private void initValue(){
        for (int i = 0 ; i < filds_et.size();i++){
            EditText et_target = filds_et.get(i);

            switch (i){
                case 0:
                    et_target.setText("王大雷");
                    break;

                case 1:
                    et_target.setText("130703199002010201");
                    break;

                case 2:
                    et_target.setText("15033565566");
                    break;

                case 3:
                    et_target.setText("15033565566");
                    break;

                case 4:
                    et_target.setText("15033565566@qq.com");
                    break;
                case 5:
                    et_target.setText("步道巷");
                    break;
                case 6:
                    et_target.setText("16号");
                    break;
                case 7:
                    et_target.setText("汪旺罔");
                    break;
                case 8:
                    et_target.setText("130401201010106585");
                    break;

            }

        }

        for (int i = 0; i <filds_text.size();i++){
            TextView tv_target = filds_text.get(i);

            switch (i){
                case 0:

                    break;
                case 1:

                    break;
                case 2:

                    break;
                case 3:

                    break;
            }

        }



    }

    private void bindListener(){
        for (int i = 0 ;i< filds_radio.size();i++){
            RadioButton btn = filds_radio.get(i);

            switch (i){
                case 0:
                    btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            filds_radio.get(2).setChecked(isChecked);
                        }
                    });
                    break;
                case 1:
                    btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            filds_radio.get(3).setChecked(isChecked);
                        }
                    });
                    break;

                case 2:
                    btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            filds_radio.get(0).setChecked(isChecked);
                        }
                    });
                    break;

                case 3:
                    btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            filds_radio.get(1).setChecked(isChecked);
                        }
                    });
                    break;



            }
        }
        for (int i = 0 ;i<filds_text.size();i++){
            final TextView target = filds_text.get(i);
            switch (i){
                case 0:
                    target.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            if(!CMethod.isFastDoubleClick()){
                                DialogUtils.birthdayDialog(EditInsuranceInfoActivity.this, "生日","", 60,new SingleStringListener() {
                                    @Override
                                    public void choiced(String result) {
//                                    TextView tv = (TextView)v;
                                        target.setText(result);
                                        target.setTextColor(getResources().getColor(R.color.app_color));
                                    }
                                });
                            }
                        }
                    });
                    break;

                case 1:
                    target.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (CMethod.isFastDoubleClick()){
                                return;
                            }
                            DialogUtils.idTypeDialog(EditInsuranceInfoActivity.this, 1,new SingleStringListener() {
                                @Override
                                public void choiced(String result) {
                                    target.setText(result);
                                    target.setTextColor(getResources().getColor(R.color.app_color));
                                }
                            });
                        }
                    });
                    break;

                case 2:
                    target.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (CMethod.isFastDoubleClick()){
                                return;
                            }
                            openAreaDialog(target);
//                            DialogUtils.areaDialog(EditInsuranceInfoActivity.this, new AreaListener() {
//                                @Override
//                                public void ok(String provinceId, String provinceName, String cityId, String cityName, String areaId, String areaName) {
//                                    target.setText(provinceName+""+cityName+""+areaName);
//                                }
//                            });
                        }
                    });
                    break;

                case 3:
                    target.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (CMethod.isFastDoubleClick()){
                                return;
                            }
                            DialogUtils.birthdayDialog(EditInsuranceInfoActivity.this, "生日","", 20,new SingleStringListener() {
                                @Override
                                public void choiced(String result) {

                                    target.setText(result);
                                    target.setTextColor(getResources().getColor(R.color.app_color));
                                }
                            });
                        }
                    });
                    break;
                case 4:
                    target.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (CMethod.isFastDoubleClick()){
                                return;
                            }
                            DialogUtils.idTypeDialog(EditInsuranceInfoActivity.this, 0,new SingleStringListener() {
                                @Override
                                public void choiced(String result) {
                                    target.setText(result);
                                    target.setTextColor(getResources().getColor(R.color.app_color));
                                }
                            });
                        }
                    });
                    break;
                case 5:
                    target.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (CMethod.isFastDoubleClick()){
                                return;
                            }
                            openAreaDialog(target);

//                            DialogUtils.areaDialog(EditInsuranceInfoActivity.this, new AreaListener() {
//                                @Override
//                                public void ok(String provinceId, String provinceName, String cityId, String cityName, String areaId, String areaName) {
////                                    target.setText(provinceName+""+cityName+""+areaName);
//                                }
//                            });
                        }
                    });
                    break;

                default:

                    break;

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.insurance_btn_next://点击投保


                MobclickAgent.onEvent(this, UMeventId.UMENG_INSURANCE_NUMBER);

                String check_result = InsuranceUtils.checkInsuranceDeatails(filds_et, filds_text);
                if (check_result.equals("success")){
                    loadingdialog.show("正在提交...", AppConstants.MaxIndex);
                    try {
                        //提交保险
                        JSONObject dataObj = new JSONObject();
                        String relation_me = "爸爸";
                        if (filds_radio.get(1).isChecked()){
                            relation_me = "妈妈";
                        }
                        dataObj.put("relation",relation_me);
                        dataObj.put("parent_name",filds_et.get(0).getText().toString().trim());
                        String gender_me = "1";
                        if (filds_radio.get(3).isChecked()){
                            gender_me = "0";
                        }
                        dataObj.put("parent_sex",gender_me);
                        dataObj.put("parent_birthday",filds_text.get(0).getText().toString().trim());
                        dataObj.put("parent_cardtype",des2id(filds_text.get(1).getText().toString().trim()));
                        dataObj.put("parent_cardid",filds_et.get(1).getText().toString().trim());
                        dataObj.put("parent_phone_num",filds_et.get(2).getText().toString().trim());
                        dataObj.put("parent_phone_num2",filds_et.get(3).getText().toString().trim());
                        dataObj.put("parent_mailbox",filds_et.get(4).getText().toString().trim());
                        dataObj.put("parent_address",filds_text.get(2).getText().toString().trim() + filds_et.get(5).getText().toString().trim()+filds_et.get(6).getText().toString().trim());
                        dataObj.put("baby_name",filds_et.get(7).getText().toString().trim());
                        String gender_c = "1";
                        if (filds_radio.get(5).isChecked()){
                            gender_c = "0";
                        }
                        dataObj.put("baby_sex",gender_c);
                        dataObj.put("baby_birthday",filds_text.get(3).getText().toString().trim());

                        dataObj.put("baby_cardtype",des2id(filds_text.get(4).getText().toString().trim()));
                        dataObj.put("baby_cardid",filds_et.get(8).getText().toString().trim());
                        dataObj.put("baby_address",filds_text.get(5).getText().toString().trim());
                        dataObj.put("watch_id",watchID);
                        dataObj.put("imei",IMEI);
                        InsuranceUtils.getInsurance(dataObj, new SimpleResponseListener() {
                            @Override
                            public void onSuccess(String url, String result) {
                                L.e("提交成功");
                                loadingdialog.dismissAndStopTimer();
                                MobclickAgent.onEvent(EditInsuranceInfoActivity.this, UMeventId.UMENG_SUCCESSFUL_SUBMISSION_OF_POLICY);
                                openComplete();
                            }

                            @Override
                            public void onFailure(String url, SimpleResult response) {
                                L.e("提交失败");
                                loadingdialog.dismissAndStopTimer();
                                T.s(response.getMessage());
                            }

                            @Override
                            public void onError(String url, SimpleResult result) {
                                L.e("提交失败");
                                loadingdialog.dismissAndStopTimer();
                                T.s(result.getMessage());
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                L.e("提交失败");
                                loadingdialog.dismissAndStopTimer();
                            }
                        },EditInsuranceInfoActivity.this);


                    } catch (JSONException e) {
                        e.printStackTrace();
                        L.e("提交失败");
                        loadingdialog.dismissAndStopTimer();
                    }


                }else {
                    T.s(check_result);

                }




//                if (radio_father.isChecked()){
//
//                }


//                String value = "athenamax@126.com";
//                String result = CMethod.desCrypto(value, AppConstants.S_KEY);
//
//                L.e("加密后的内容是："+result);
//                String decryResult = CMethod.decrypt(result,AppConstants.S_KEY);
//                L.e("解密后的内容是："+decryResult);

//                String encriptStr =CMethod. ;


//                if (radio_father.isChecked()){
//                    L.e("father");
//                }
//                if (radio_mother.isChecked()){
//                    L.e("mother");
//                }
//
//
//                for(int i = 0 ; i < filds.size();i++){
//                    EditText temp = filds.get(i);
//
//                    L.e("target file text"+temp.getText());
//                }
//
//                L.e(">>>====" + tv_insurance_baby_city_tv.getText());
//                L.e("==========" );
//                L.e(">>>====" + tv_insurance_et_city.getText());
                break;
//            case R.id.tv_insurance_et_city:
//                L.e("打开地区选矿");
//                openAreaDialog(tv_insurance_et_city);
//                break;
//            case R.id.tv_insurance_baby_city_tv:
//                L.e("打开儿童地区选矿");
//                openAreaDialog(tv_insurance_baby_city_tv);
//                break;

        }
    }

    private void openComplete(){
        Intent i = new Intent(EditInsuranceInfoActivity.this,InsuranceCompleteActivity.class);
        startActivity(i);
        Finish();


    }


    private void Finish(){
        finish();
    }


    private void openAreaDialog(final TextView target){
        DialogUtils.areaDialog(EditInsuranceInfoActivity.this, new AreaListener() {
            @Override
            public void ok(String provinceId, String provinceName, String cityId, String cityName, String areaId, String areaName) {


                StringBuilder sb = new StringBuilder();
                sb.append(provinceName);
                if (!CMethod.isEmpty(cityName) && !"市区".equals(cityName) && !"郊县".equals(cityName)) {
                    sb.append(cityName);
                }
                sb.append(areaName);
                String result = sb.toString();
                L.e("result is ---" + result);
                target.setText(result);
                target.setTextColor(getResources().getColor(R.color.app_color));
            }
        });


    }


    private String des2id(String type_des){

        String result = "1";
//        if (type_des.equals("身份证")){
//
//        }else
        if (type_des.equals("护照")){
            result = "3" ;
        }else if (type_des.equals("军官证")){
            result = "2";
        }else if (type_des.equals("港澳通行证")){
            result = "4";
        }else if (type_des.equals("台湾通行证")){
            result = "5";
        }

        return result;
    }



}
