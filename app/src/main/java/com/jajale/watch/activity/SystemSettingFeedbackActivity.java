package com.jajale.watch.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseActivity;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.R;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.InputMethodUtils;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 意见反馈
 * <p/>
 * Created by lilonghui on 2015/12/5.
 * Email:lilonghui@bjjajale.com
 */
public class SystemSettingFeedbackActivity extends BaseActivity implements View.OnClickListener {

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
    @InjectView(R.id.feedback_edit_text)
    EditText feedbackEditText;
    @InjectView(R.id.feedback_button)
    Button feedbackButton;

    private LoadingDialog loadingDialog;
    private boolean isDestroy = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.inject(this);
        //设置标题
        tvMiddle.setText(getResources().getString(R.string.feedback_title_text));
        ivLeft.setImageResource(R.drawable.title_goback_selector);
        loadingDialog = new LoadingDialog(this);

        bindListener();
        InputMethodUtils.openInputMethod(this, feedbackEditText);

    }

    private void bindListener() {
        ivLeft.setOnClickListener(this);
        feedbackButton.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        if (CMethod.isFastDoubleClick()){
            return;
        }
        switch (v.getId()) {

            case R.id.iv_left:
                InputMethodUtils.closeInputMethod(this, feedbackEditText);
                finish();
                break;
            case R.id.feedback_button://发送意见
                InputMethodUtils.closeInputMethod(this, feedbackEditText);
                String feedback = feedbackEditText.getText().toString().trim();

                if (feedback.replace(" ", "").equals("")){
                    //                    Toast.makeText(SystemSettingFeedbackActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                    T.s("输入不能为空");
                }else {
                    if (CMethod.isNet(SystemSettingFeedbackActivity.this)){
                        //sendFeebback(feedback);
                    }else {
                        T.s("网络不给力");
                    }
                }
                break;


        }

    }


    /**
     * java发送意见反馈
     */
    private void sendFeebback(final String feedback) {
        loadingDialog.show("发送中，请稍后...");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", BaseApplication.getUserInfo().userID);
            jsonObject.put("content", feedback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API, AppConstants.JAVA_GET_API_FEED_BACK_BY_PARAMS_URL, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                loadingDialog.dismiss();
                feedbackEditText.setText("");
                T.s("发送意见反馈成功");
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroy = true;
    }
}
