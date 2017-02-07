package com.jajale.watch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseActivity;
import com.jajale.watch.R;
import com.jajale.watch.entitydb.GrowRecord;
import com.jajale.watch.entitydb.SmartWatch;
import com.jajale.watch.entitydb.StandardGrow;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.listener.SingleStringListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.DialogUtils;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.StandardGrowTool;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 增加成长记录
 * Created by chunlongyuan on 11/23/15.
 */
public class AddGrowRecordActivity extends BaseActivity implements View.OnClickListener {

    public static final String EXTRA_RECORD = "extra_record";
    public static final String EXTRA_WATCH = "extra_watch";
    public static final String EXTRA_IS_ADD = "extra_is_add";
    public static final int TO_ADD_GROW_RECORD = 1000;
    private GrowRecord record;

    private TextView tv_current_day;
    private EditText et_weight;
    private EditText et_height;
    private double oldWeight;
    private double oldHeight;
    private SmartWatch watch;
    private LoadingDialog loadingDialog;
    private StandardGrow grow;
    private StandardGrowTool standardGrowTool;
    private String currentDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grow_record);

        loadingDialog = new LoadingDialog(this);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_RECORD)) {//修改记录
            record = (GrowRecord) intent.getSerializableExtra(EXTRA_RECORD);
        }

        //必须有
        watch = intent.getParcelableExtra(EXTRA_WATCH);
        standardGrowTool = new StandardGrowTool();
        int month = getMonthByBirthday(watch.getBirthday());
        List<StandardGrow> standardGrows = standardGrowTool.getAllData(watch.getSex()==1);
        for (StandardGrow standardGrow : standardGrows) {
            if (month >= standardGrow.month) {
                grow = standardGrow;
                break;
            }
        }

        ImageView iv_left = (ImageView) findViewById(R.id.iv_left);
        iv_left.setImageResource(R.drawable.title_goback_selector);
        iv_left.setOnClickListener(this);


        TextView tv_middle = (TextView) findViewById(R.id.tv_middle);
        tv_middle.setText((record != null) ? R.string.title_modify_grow_record : R.string.title_add_grow_record);
        TextView tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setText(R.string.save);
        tv_right.setOnClickListener(this);

        findViewById(R.id.ll_current_daye).setOnClickListener(this);

        et_height = (EditText) findViewById(R.id.et_heitht);
        et_weight = (EditText) findViewById(R.id.et_weitht);
        if (record != null) {
            oldHeight = record.height;
            oldWeight = record.weight;
            et_weight.setText(record.weight + "");
            et_height.setText(record.height + "");
            currentDay = record.create_time;
        }

        SimpleDateFormat hmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDay = (!CMethod.isEmpty(currentDay)) ? currentDay : CMethod.getCurrentDay(hmt);

        tv_current_day = (TextView) findViewById(R.id.tv_current_day);
        tv_current_day.setText(CMethod.formatDay(currentDay));
    }

    @Override
    public void onClick(View v) {
        if (CMethod.isFastDoubleClick()) {
            return;
        }

        switch (v.getId()) {
            case R.id.ll_current_daye:
                DialogUtils.birthdayDialog(this, "记录日期", currentDay.substring(0, 10), 20, new SingleStringListener() {
                    @Override
                    public void choiced(String result) {
                        L.d("时间--->" + result);
                        currentDay = result;

                        SimpleDateFormat hmt = new SimpleDateFormat("yyyy-MM-dd");
                        String day = CMethod.getCurrentDay(hmt);
                        int target = CMethod.changeTimeStr2Int(currentDay);
                        int now = CMethod.changeTimeStr2Int(day);

                        if (target != -1 && now - target >= 0) {
                            tv_current_day.setText(CMethod.formatDay(currentDay));
                        } else {
                            tv_current_day.setText(CMethod.formatDay(day));
                        }
                    }
                });
                break;
            case R.id.iv_left:
                finish();
                break;
            case R.id.tv_right:
                String weight = et_weight.getText().toString();
                String height = et_height.getText().toString();
                String time = currentDay;

                if (CMethod.isEmptyOrZero(weight)) {
                    T.s("体重不能为空！");
                } else if (CMethod.isEmpty(height)) {
                    T.s("身高不能为空！");
                } else if (!isEdited(weight, height)) {
                    finish();
                } else if (isOverWeightOrHeight(weight, height)) {
                    T.s("请输入合理的身高或体重！");
                } else {
                    commitGrowRecord(Double.parseDouble(weight), Double.parseDouble(height), time);
                }

                break;
        }
    }

    /**
     * 提交成长记录
     *
     * @param weight
     * @param height
     * @param time
     */
    private void commitGrowRecord(double weight, double height, String time) {
        loadingDialog.show();
        //根据身高体重选择文案
        String content = getContent(weight, height);
        final boolean isAdd = (record == null);

        final GrowRecord tmpRecord = new GrowRecord();

        tmpRecord.create_time = time;
        tmpRecord.weight = weight;
        tmpRecord.height = height;
        tmpRecord.content = content;

        //提交网络
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("user_id", watch.getUser_id());
            if (record!=null)
            {
                jsonObject.put("growth_id", record.growth_id);
            }
            jsonObject.put("height", height);
            jsonObject.put("weight", weight);
            jsonObject.put("content", content);
            jsonObject.put("create_time", time);
        } catch (JSONException e) {
            loadingDialog.dismissAndStopTimer();
            e.printStackTrace();
        }

        String path;
        if (record!=null)
        {
            path=AppConstants.JAVA_EDIT_BABY_GROWTH_URL;
        }else{
            path=AppConstants.JAVA_ADD_BABY_GROWTH_URL;
        }
        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API,path, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                String parse = parse(result);
                if (!CMethod.isEmpty(parse)) {
                    tmpRecord.growth_id = parse;
                }

                loadingDialog.dismiss();
                //添加或刷新成功,刷新界面
                Intent intent = new Intent();
                intent.putExtra(EXTRA_IS_ADD, isAdd);
                intent.putExtra(EXTRA_RECORD, tmpRecord);
                setResult(RESULT_OK, intent);
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

    private String parse(String result) {
        if (!CMethod.isEmpty(result)) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has(GrowRecord.FIELD_GROWTHID)) {
                    return jsonObject.getString(GrowRecord.FIELD_GROWTHID);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private String getContent(double weight, double height) {

        if (grow == null) {
            return "";
        } else {


            double uPerValue = getUperValue();
            double lowerValue = getLowerValue();
            double myValue = getMyValue(weight, height);

            L.e("uPerValue==" + uPerValue + ",lowerValue==" + lowerValue + ",myValue==" + myValue);

            if (myValue > uPerValue) {
                return "注意控制饮食哦";
            }
            if (myValue < lowerValue) {
                return "注意补充营养哦";
            }
            if (myValue >= lowerValue && myValue <= uPerValue) {
                return "继续保持哦";
            }


        }
        return "";
    }

    private double getUperValue() {
        L.e("grow.weightStander==" + grow.weightStander);
        return grow.weightStander / (grow.heightStander * grow.heightStander * 0.0001) + 1.5;
    }

    private double getLowerValue() {

        return grow.weightStander / (grow.heightStander * grow.heightStander * 0.0001) - 1.5;
    }

    private double getMyValue(double weight, double height) {
        L.e("weight==" + weight);
        return weight / (height * height * 0.0001);
    }

    private int getMonthByBirthday(String birthday) {
        //根据生日算年龄
        try {
            if (!CMethod.isEmptyOrZero(birthday)) {
                SimpleDateFormat df = new SimpleDateFormat(birthday.length() > 10 ? "yyyy-MM-dd HH:mm:ss" : "yyyy-MM-dd");
                Date date = df.parse(birthday);
                return getMonthByBirthday(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 根据用户生日计算年龄
     */
    public static int getMonthByBirthday(Date birthday) {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthday)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;

        cal.setTime(birthday);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH) + 1;

        int subYear = yearNow - yearBirth;
        int subMonth = monthNow - monthBirth;
        return subYear * 12 + subMonth;
    }

    private boolean isEdited(String weight, String height) {
        return !weight.equals(oldWeight + "") || !height.equals(oldHeight + "") || !tv_current_day.getText().toString().equals(CMethod.formatDay(record.create_time));
    }

    private boolean isOverWeightOrHeight(String weight, String height) {
        try {
            double weight_d = Double.parseDouble(weight);
            double height_d = Double.parseDouble(height);
            return weight_d >= 80 || weight_d <= 2 || height_d >= 180 || height_d <= 30;
        } catch (Exception e) {
            return true;
        }

    }


}
