package com.jajale.watch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jajale.watch.AppConstants;
import com.jajale.watch.R;
import com.jajale.watch.adapter.VaccineAgeListAdapter;
import com.jajale.watch.adapter.VaccineContentListAdapter;
import com.jajale.watch.entity.GrowPageEntity;
import com.jajale.watch.entity.SmartWatchListData;
import com.jajale.watch.entity.VaccineAgeData;
import com.jajale.watch.entity.VaccineContentData;
import com.jajale.watch.entitydb.SmartWatch;
import com.jajale.watch.entityno.UMeventId;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 疫苗
 * <p/>
 * Created by lilonghui on 2015/11/22.
 * Email:lilonghui@bjjajale.com
 */
public class VaccineActivity extends NoNetworkActivity implements View.OnClickListener {
    Boolean[] boolean_press;
    private boolean isDestroy = false;
    private VaccineAgeData fromJson_age;
    private TextView tv_middle;
    public static String watchID;
//    public static String vaccineID;
    public static String age_name;
    private GrowPageEntity pageEntity = new GrowPageEntity();
    public SmartWatch currentWatch;
    @InjectView(R.id.vaccine_age_list)
    ListView vaccineAgeList;
    @InjectView(R.id.vaccine_content_list)
    ListView vaccineContentList;
    private VaccineContentListAdapter content_adapter;


    @Override
    public void refreshView() {
        super.refreshView();
        getVaccineAgeList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MobclickAgent.onEvent(this, UMeventId.UMENG_VACCINE_USE);
        setContentView(R.layout.activity_vaccine);
        ButterKnife.inject(this);
        showNotworkView();
        initTitleView();
        currentWatch= getIntent().getParcelableExtra(SmartWatchListData.KEY);
        tv_middle.setText(currentWatch.getNick_name());
        watchID = currentWatch.getUser_id();
        getVaccineAgeList();




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            boolean isRefresh = data.getBooleanExtra("save_success", false);
            if (isRefresh)
                getVaccineContentList();
        }
    }


    /**
     * 获取疫苗年龄时间段
     */
    private void getVaccineAgeList() {

        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API, AppConstants.JAVA_VACCINE_GET_AGES_LIST_URL, null, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                fromJson_age = gson.fromJson(result, VaccineAgeData.class);
                setAgeListViewAdapter(fromJson_age.getVaccineAgeList());
                age_name = fromJson_age.getVaccineAgeList().get(0).getAge();
                getVaccineContentList();
            }
            @Override
            public void onFailure(String result) {

                T.s(result);
            }

            @Override
            public void onError() {

            }
        });
    }

    /**
     * 获取疫苗内容
     */
    private void getVaccineContentList() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id",watchID);
            jsonObject.put("age", age_name);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API,AppConstants.JAVA_VACCINE_DATA_LIST_URL, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                VaccineContentData fromJson_content = gson.fromJson(result, VaccineContentData.class);
                setContentListViewAdapter(fromJson_content.getVaccineList());
            }
            @Override
            public void onFailure(String result) {

                T.s(result);
            }

            @Override
            public void onError() {

            }
        });
    }


    /**
     * 疫苗内容显示
     *
     * @param vaccineList
     */
    private void setContentListViewAdapter(List<VaccineContentData.VaccineListEntity> vaccineList) {


        if (content_adapter != null && content_adapter.mVaccineList != null) {
            content_adapter.mVaccineList = vaccineList;
            content_adapter.notifyDataSetChanged();
        } else {
            content_adapter = new VaccineContentListAdapter(this, vaccineList);
            vaccineContentList.setAdapter(content_adapter);
        }


        vaccineContentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //进入编辑
                if (CMethod.isFastDoubleClick()){
                    return;
                }
                Intent intent = new Intent(VaccineActivity.this, VaccineDetailActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable(VaccineContentData.KEY, content_adapter.mVaccineList.get(position));//传递宝贝信息
                intent.putExtras(mBundle);

                startActivityForResult(intent, 1);
            }
        });
    }

    /**
     * 儿童年龄段内容显示
     *
     * @param vaccineAgeList
     */
    private void setAgeListViewAdapter(List<VaccineAgeData.VaccineAgeListEntity> vaccineAgeList) {
        boolean_press = new Boolean[vaccineAgeList.size()];
        for (int i = 0; i < boolean_press.length; i++) {
            boolean_press[i] = (i == 0);
        }

        final VaccineAgeListAdapter adapter = new VaccineAgeListAdapter(this, vaccineAgeList, boolean_press);
        this.vaccineAgeList.setAdapter(adapter);
        this.vaccineAgeList.setSelection(0);
        this.vaccineAgeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < boolean_press.length; i++) {
                    if (i == position)
                        boolean_press[i] = true;
                    else
                        boolean_press[i] = false;
                }
                adapter.booleans = boolean_press;
                adapter.notifyDataSetChanged();
                age_name = fromJson_age.getVaccineAgeList().get(position).getAge();
                getVaccineContentList();
            }
        });
    }


    /**
     * 初始化title view
     */
    private void initTitleView() {
        findViewById(R.id.iv_left).setOnClickListener(this);

        tv_middle = (TextView) findViewById(R.id.tv_middle);



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
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }
}
