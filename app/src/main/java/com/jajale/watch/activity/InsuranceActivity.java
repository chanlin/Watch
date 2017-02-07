package com.jajale.watch.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.BaseActivity;
import com.jajale.watch.R;
import com.jajale.watch.entityno.UMeventId;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.T;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 *
 * 投保首页
 *
 * Created by lilonghui on 2015/11/28.
 * Email:lilonghui@bjjajale.com
 */
public class InsuranceActivity extends BaseActivity implements View.OnClickListener {

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
    @InjectView(R.id.insurance_tv_accident)
    TextView insuranceTvAccident;
    @InjectView(R.id.insurance_tv_sick)
    TextView insuranceTvSick;
    @InjectView(R.id.tv_get_insurance)
    TextView tv_get_insurance;
//    @InjectView(R.id.cb_get_insurance)
//    CheckBox cb_get_insurance;

    private String watchID = "";
    private String IMEI = "";
    private boolean changebtn = false ;

    private String num = "010-84922888" ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance);
        ButterKnife.inject(this);

        watchID = getIntent().getStringExtra("watchID");
        IMEI = getIntent().getStringExtra("IMEI");
        changebtn = getIntent().getBooleanExtra("changebtn",false);


        MobclickAgent.onEvent(InsuranceActivity.this, UMeventId.UMENG_ENTER_THE_NUMBER_OF_INSURANCE_PAGES);
        tvMiddle.setText(getResources().getString(R.string.insurance_title_text));
        ivLeft.setImageResource(R.drawable.title_goback_selector);
        ivLeft.setOnClickListener(this);
//        cb_get_insurance.setChecked(true);
//        cb_get_insurance.setOnClickListener(this);
        insuranceBtnNext.setOnClickListener(this);

        if(changebtn){
            insuranceBtnNext.setText("投保热线："+num);
        }

        String  text_top = "华英智联儿童星";


//        意外导致身故保险金：保险金额10万元，保险责任：因意外导致身故，包含但不限于失踪（法院宣告身故）、溺水、食物中毒、烧伤、交通、运动意外事故导致的身故。
        String  text_accident="华英智联儿童星";

//        重大疾病保险金：保险金额10万元，保险责任:首次确诊患：1、恶性肿瘤；2、重大器官移植术或造血干细胞移植术；3、终末期肾病（或称慢性肾功能衰竭尿毒症期）；4、急性或亚急性重症肝炎；5、良性脑肿瘤；6、脑炎后遗症或脑膜炎后遗症；7、双耳失聪；8、双目失明；9、瘫痪；10、严重脑损伤；11、严重Ⅲ度烧伤；12、系统性红斑狼疮并发重度的肾功能损害

        String  text_sick="华英智联儿童星";

        TextView textView_top = (TextView) findViewById(R.id.insurance_tv_top);
        textView_top.setText(Html.fromHtml(text_top));
        insuranceTvAccident.setText(Html.fromHtml(text_accident));
        insuranceTvSick.setText(Html.fromHtml(text_sick));
        insuranceBtnNext.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        if (CMethod.isFastDoubleClick()){
            return;
        }

        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.insurance_btn_next://点击立即投保

                if(changebtn){
                    try{
                        //用intent启动拨打电话
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + num));
                        startActivity(intent);
                    }catch (Exception e){
                        T.s("您的手机无法拨号");
                    }


                }else {
                    Intent intent=new Intent(this,EditInsuranceInfoActivity.class);
                    intent.putExtra("watchID",watchID);
                    intent.putExtra("IMEI",IMEI);
//                Intent intent = new Intent(this,InsuranceCompleteActivity.class);
                    startActivity(intent);

                    finish();
                    break;
                }


//            case R.id.cb_get_insurance:
//                insuranceBtnNext.setEnabled(true);
//                break;

        }


    }
}
