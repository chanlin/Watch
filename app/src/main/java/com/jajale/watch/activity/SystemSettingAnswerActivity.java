package com.jajale.watch.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.BaseActivity;
import com.jajale.watch.R;
import com.jajale.watch.entity.QuestionData;
import com.jajale.watch.utils.CMethod;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 常见问题答案
 * <p/>
 * Created by lilonghui on 2015/12/5.
 * Email:lilonghui@bjjajale.com
 */
public class SystemSettingAnswerActivity extends BaseActivity implements View.OnClickListener {

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
    @InjectView(R.id.answer_tv)
    TextView answerTv;
    @InjectView(R.id.question_tv)
    TextView questionTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        ButterKnife.inject(this);
        //设置标题
        tvMiddle.setText(getResources().getString(R.string.system_setting_question));
        ivLeft.setImageResource(R.drawable.title_goback_selector);

        QuestionData.ProblemListEntity problem = getIntent().getExtras().getParcelable(QuestionData.Key);

        bindListener();
        questionTv.setText(Html.fromHtml(problem.getTitle()));//显示问题
        answerTv.setText(Html.fromHtml(problem.getContents()));//显示答案
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
}
