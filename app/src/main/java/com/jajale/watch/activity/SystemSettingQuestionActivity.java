package com.jajale.watch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseActivity;
import com.jajale.watch.R;
import com.jajale.watch.adapter.QuestionListAdapter;
import com.jajale.watch.entity.QuestionData;
import com.jajale.watch.entity.SPKeyConstants;
import com.jajale.watch.entityno.UMeventId;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.PhoneSPUtils;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 常见问题
 * <p/>
 * Created by lilonghui on 2015/12/5.
 * Email:lilonghui@bjjajale.com
 */
public class SystemSettingQuestionActivity extends BaseActivity implements View.OnClickListener {

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
    @InjectView(R.id.question_lv_content)
    ListView questionLvContent;
    private boolean isDestroy = false;
    private QuestionListAdapter adapter;
    private PhoneSPUtils phoneSPUtils;
    private QuestionData fromJson_content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ButterKnife.inject(this);
        phoneSPUtils = new PhoneSPUtils(SystemSettingQuestionActivity.this);

        //设置标题
        tvMiddle.setText(getResources().getString(R.string.system_setting_question));
        ivLeft.setImageResource(R.drawable.title_goback_selector);


        bindListener();


        getQuestionListFromSp();

        getQuestionListFormNetwork();

        MobclickAgent.onEvent(this, UMeventId.UMENG_NUMBER_OF_COMMON_PROBLEMS);

    }


    private void setQuestionListView(String result) {
        Gson gson = new Gson();
        phoneSPUtils.save(SPKeyConstants.SYSTEM_SETTING_QUESTION, result);
        fromJson_content = gson.fromJson(result, QuestionData.class);

        if (fromJson_content.getProblemList() != null && fromJson_content.getProblemList().size() != 0) {
            if (adapter != null && adapter.list.size() != 0) {
                adapter.list = fromJson_content.getProblemList();
                adapter.notifyDataSetChanged();
            } else {
                adapter = new QuestionListAdapter(this, fromJson_content.getProblemList());
                questionLvContent.setAdapter(adapter);
            }
        }


        questionLvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (CMethod.isFastDoubleClick()) {
                    return;
                }
                Intent intent = new Intent(SystemSettingQuestionActivity.this, SystemSettingAnswerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(QuestionData.Key, fromJson_content.getProblemList().get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void bindListener() {
        ivLeft.setOnClickListener(this);

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
        }

    }


    private void getQuestionListFromSp() {
        if (!CMethod.isEmpty(phoneSPUtils.getString(SPKeyConstants.SYSTEM_SETTING_QUESTION))) {
            setQuestionListView(phoneSPUtils.getString(SPKeyConstants.SYSTEM_SETTING_QUESTION));
        }
    }


    /**
     * java获取问题列表
     */
    private void getQuestionListFormNetwork() {

        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API, AppConstants.JAVA_GET_ALL_API_QA_URL, null, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                setQuestionListView(result);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }
}
