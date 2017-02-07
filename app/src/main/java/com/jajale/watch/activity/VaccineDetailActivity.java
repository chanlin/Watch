package com.jajale.watch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseActivity;
import com.jajale.watch.R;
import com.jajale.watch.entity.VaccineContentData;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.listener.SimpleClickListener;
import com.jajale.watch.listener.SingleStringListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.DialogUtils;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 疫苗详情及编辑状态
 * <p/>
 * Created by lilonghui on 2015/11/30.
 * Email:lilonghui@bjjajale.com
 */
public class VaccineDetailActivity extends BaseActivity implements View.OnClickListener {

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
    @InjectView(R.id.vaccine_detail_tv_state)
    TextView vaccineDetailTvState;
    @InjectView(R.id.vaccine_detail_rl_state)
    RelativeLayout vaccineDetailRlState;
    @InjectView(R.id.vaccine_detail_tv_date)
    TextView vaccineDetailTvDate;
    @InjectView(R.id.vaccine_detail_rl_date)
    RelativeLayout vaccineDetailRlDate;
    @InjectView(R.id.vaccine_detail_tv_describe)
    TextView vaccineDetailTvDescribe;


    private VaccineContentData.VaccineListEntity vaccineListEntity;
    private LoadingDialog loadingDialog;


    private String time_null = "请选择接种日期";
    private String y_vaccine = "已接种";
    private String n_vaccine = "未接种";


    private String watchID;//手表ID
    private String vaccineID;//疫苗信息ID
    private String state;//是否接种    0-false 未接种     1-true 已接种
    private String time;//接种日期

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine_detail);
        ButterKnife.inject(this);
        tvMiddle.setText(getResources().getString(R.string.vaccine_detail_describe));
        ivLeft.setImageResource(R.drawable.title_goback_selector);
        ivLeft.setOnClickListener(this);
        tvRight.setText(R.string.save);
        tvRight.setOnClickListener(this);

        loadingDialog = new LoadingDialog(this);

        vaccineListEntity = getIntent().getParcelableExtra(VaccineContentData.KEY);
        vaccineID = vaccineListEntity.getVaccineID();

        vaccineDetailTvState.setText(vaccineListEntity.getState() == 0 ? n_vaccine : y_vaccine);
        vaccineDetailTvDate.setText(CMethod.isEmpty(vaccineListEntity.getTime())||"0000-00-00".equals(vaccineListEntity.getTime())? time_null : vaccineListEntity.getTime());
        if (!CMethod.isEmpty(vaccineListEntity.getDescribe()))
            vaccineDetailTvDescribe.setText(Html.fromHtml(vaccineListEntity.getDescribe()));

        vaccineDetailRlDate.setOnClickListener(this);
        vaccineDetailRlState.setOnClickListener(this);

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
            case R.id.tv_right:// 保存
                watchID = VaccineActivity.watchID;
                time = vaccineDetailTvDate.getText().toString();
                state = vaccineDetailTvState.getText().toString().equals(y_vaccine) ? "1" : "0";
                if (isReasonDate()) {
                    if (!time.equals(time_null)) {
                        if (CMethod.isNet(VaccineDetailActivity.this)) {
                            editVaccineInfo();
                        } else {
                            T.s("网络不给力");
                        }
                    } else {
                        T.s("请选择接种日期");
                    }
                } else {
                    T.s("所选时间不能超过当前日期");
                }

                break;
            case R.id.vaccine_detail_rl_state://点击选择接种状态
                DialogUtils.vaccineSelectDialog(this, "是否接种过" + vaccineListEntity.getVaccineName() + "?", new SimpleClickListener() {
                    @Override
                    public void ok() {//未接种
                        vaccineDetailTvState.setText(n_vaccine);
                    }

                    @Override
                    public void cancle() {//已接种
                        vaccineDetailTvState.setText(y_vaccine);
                    }
                });

                break;
            case R.id.vaccine_detail_rl_date://点击选择日期
                String select_date = vaccineDetailTvDate.getText().toString();
                String date = select_date.equals(time_null) ? CMethod.getFullDay() : select_date;
                date = date.equals("") ? CMethod.getFullDay() : date;
                DialogUtils.birthdayDialog(this, vaccineDetailTvState.getText().toString() + "日期", date, 20, new SingleStringListener() {
                    @Override
                    public void choiced(String result) {
                        vaccineDetailTvDate.setText(result);
                    }
                });

                break;


        }

    }

    private boolean isReasonDate() {
        try {
            if (vaccineDetailTvState.getText().toString().equals(y_vaccine)) {
                String[] select = vaccineDetailTvDate.getText().toString().split("-");
                int select_year = Integer.parseInt(select[0]);
                int select_month = Integer.parseInt(select[1]);
                int select_day = Integer.parseInt(select[2]);
                String[] now = CMethod.getFullDay().split("-");
                if (select_year > Integer.parseInt(now[0])) {
                    return false;
                } else if (select_year == Integer.parseInt(now[0])) {
                    if (select_month > Integer.parseInt(now[1])) {
                        return false;
                    } else if (select_month == Integer.parseInt(now[1])) {
                        if (select_day > Integer.parseInt(now[2])) {
                            return false;
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
        return true;

//        return true;
    }

    /**
     * 保存疫苗信息
     */
    private void editVaccineInfo() {
        loadingDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("vaccine_id", vaccineID);
            jsonObject.put("user_id", watchID);
            jsonObject.put("create_time", time);
            jsonObject.put("state", state);
            //缺少state字段
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API,AppConstants.JAVA_VACCINE_INSERT_UPDATE_URL, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                loadingDialog.dismiss();
                Toast.makeText(VaccineDetailActivity.this, " 保存成功", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("save_success", true);
                setResult(1, intent);
                finish();
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
}
