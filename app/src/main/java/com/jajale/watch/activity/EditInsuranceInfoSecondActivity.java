package com.jajale.watch.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jajale.watch.BaseActivity;
import com.jajale.watch.R;
import com.jajale.watch.adapter.InsuranceInfoAdapter;
import com.jajale.watch.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by athena on 2015/12/5.
 * Email: lizhiqiang@bjjajale.com
 */
public class EditInsuranceInfoSecondActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {


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
    @InjectView(R.id.lv_insurance_info_content)
    ListView lvInsuranceInfoContent;


    private String [] titles = {"投保人信息","您与孩子的关系","姓名","投保人身份证","移动电话","备用移动电话","邮箱地址","投保人地址","","","孩子信息","姓名","身份证号","现居城市","确认并投保"};
    private String [] hints = {"","","请填写您的真实姓名","请填写您的身份证号码","请填写您的联系方式","选填","请填写您的邮箱地址","请选择省、市、区","请填写您所在的街道","请填写您的小区和门牌号","投保年龄为3-14周岁","请填写孩子的真实姓名","可在户口本查看孩子的身份证号","请选择孩子的所在城市",""};

    private List<BaseEntity> source = new ArrayList<BaseEntity>();
    private InsuranceInfoAdapter mAdapter  ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_insurance_second);
        ButterKnife.inject(this);

        initTitle();
        initRes();
        initView();
        bindListener();

    }

    private void initView(){
        mAdapter = new InsuranceInfoAdapter(EditInsuranceInfoSecondActivity.this,source);
        lvInsuranceInfoContent.setAdapter(mAdapter);


    }

    private void initRes(){
        for (int i = 0 ; i <titles.length;i++){
            BaseEntity entity = new BaseEntity();
            entity.setKey(titles[i]);
            entity.setValue(hints[i]);
            source.add(entity);
        }
    }


    private void bindListener(){
        ivLeft.setOnClickListener(this);
    }

    private void initTitle(){
        tvMiddle.setText(getResources().getString(R.string.insurance_title_text));
        ivLeft.setImageResource(R.drawable.title_goback_selector);

        lvInsuranceInfoContent.setOnItemSelectedListener(this);
        lvInsuranceInfoContent.setOnItemClickListener(this);



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left:

                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        lvInsuranceInfoContent.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        EditText editText = (EditText) view.findViewById(R.id.et_insurance_right_value);
        editText.requestFocus();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        lvInsuranceInfoContent.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
    }
}
