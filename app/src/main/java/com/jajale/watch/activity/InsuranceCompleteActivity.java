package com.jajale.watch.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseActivity;
import com.jajale.watch.R;
import com.jajale.watch.cviews.ClauseURLSpan;
import com.jajale.watch.listener.ClauseSelectListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.T;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by athena on 2015/12/5.
 * Email: lizhiqiang@bjjajale.com
 */
public class InsuranceCompleteActivity extends BaseActivity implements View.OnClickListener {


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
    @InjectView(R.id.tv_report_by_phone)
    TextView tvReportByPhone;
    @InjectView(R.id.tv_web_dadi)
    TextView tvWebDadi;

    @InjectView(R.id.tv_insu_complete_sick)
    TextView tv_insu_complete_sick;
    @InjectView(R.id.tv_ques_detail)
    TextView tv_ques_detail;

//    @InjectView(R.id.tv_insu_complete_accident)
//    TextView tv_insu_complete_accident;
//    @InjectView(R.id.tv_insu_complete_add)
//    TextView tv_insu_complete_add;
//    @InjectView(R.id.tv_insu_complete_death)
//    TextView tv_insu_complete_death;

    private String content_sick = "大地个人重大疾病保险条款";
    private String content_accident = "大地个人意外伤害保险条款";
    private String content_add = "附加重大疾病特约保险条款";
    private String content_death = "附加意外身故特约保险条款";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_complete);
        ButterKnife.inject(this);

        initTitle();

        initView();

        bindListener();

    }

    private void initTitle(){
        tvMiddle.setText(getResources().getString(R.string.insurance_title_text));
        ivLeft.setImageResource(R.drawable.title_goback_selector);


    }


    private void initView(){

        String clause = "＊《SICK》、《ACCIDENT》、《ADD》、《DEATH》";

        String sick_cla = "<a href=\"sick\">"+content_sick+"</a>";
        String sick_accident = "<a href=\"accident\">"+content_accident+"</a>";
        String sick_add = "<a href=\"add\">"+content_add+"</a>";
        String sick_death = "<a href=\"death\">"+content_death+"</a>";

        String content = clause.replace("SICK",sick_cla).replace("ACCIDENT",sick_accident).replace("ADD",sick_add).replace("DEATH",sick_death);
        Spanned spanned = Html.fromHtml(content);
        URLSpan[] spans = spanned.getSpans(0, spanned.length(), URLSpan.class);
        for (URLSpan span : spans) {
            int spanStart = spanned.getSpanStart(span);
            int spanEnd = spanned.getSpanEnd(span);
            Spannable spannable = (Spannable) spanned;
            spannable.removeSpan(span);
            spannable.setSpan(new ClauseURLSpan(span.getURL(), new ClauseSelectListener() {
                @Override
                public void choiced(String result, int index) {
                }
                @Override
                public void onCheckedLink(String link) {
                    L.e("reuslt is"+link);
                    if (link.equals("sick")){
                        L.e("大病保险条款");
                        openWebPage(4,content_sick);

                    }else if(link.equals("accident")){
                        L.e("大地个人意外伤害保险条款");
                        openWebPage(2,content_accident);

                    }else if(link.equals("add")){
                        L.e("附加重大疾病特约保险条款");
                        openWebPage(3,content_add);
                    }else if(link.equals("death")){
                        L.e("附加意外身故特约保险条款");
                        openWebPage(1,content_death);

                    }

                }
            }), spanStart, spanEnd, 0);
        }


        tv_insu_complete_sick.setText(spanned);
        tv_insu_complete_sick.setMovementMethod(LinkMovementMethod.getInstance());


        tv_ques_detail.setText("<<常见问题>>");
        tv_ques_detail.setOnClickListener(this);

//        《大地个人意外伤害保险条款》
//        《附加重大疾病特约保险条款》
//        《附加意外身故特约保险条款》
//        tv_insu_complete_sick.setText(Html.fromHtml("《<font color=\"#2fbbef\">大地个人重大疾病保险条款</font>》"));
//        tv_insu_complete_sick.setOnClickListener(this);

//        tv_insu_complete_accident.setText(Html.fromHtml("《<font color=\"#2fbbef\">大地个人意外伤害保险条款</font>》"));
//        tv_insu_complete_accident.setOnClickListener(this);
//        tv_insu_complete_add.setText(Html.fromHtml("《<font color=\"#2fbbef\"> 附加重大疾病特约保险条款</font>》"));
//        tv_insu_complete_add.setOnClickListener(this);
//        tv_insu_complete_death.setText(Html.fromHtml("《<font color=\"#2fbbef\">附加意外身故特约保险条款</font>》"));
//        tv_insu_complete_death.setOnClickListener(this);


    }


    private void bindListener(){
        ivLeft.setOnClickListener(this);
        tvReportByPhone.setOnClickListener(this);
        tvWebDadi.setOnClickListener(this);



    }

//    @InjectView(R.id.tv_insu_complete_accident)
//    TextView tv_insu_complete_accident;
//    @InjectView(R.id.tv_insu_complete_add)
//    TextView tv_insu_complete_add;
//    @InjectView(R.id.tv_insu_complete_death)
//    TextView tv_insu_complete_death;



    @Override
    public void onClick(View v) {
        if (CMethod.isFastDoubleClick()){
            return;
        }

        switch (v.getId()){
            case R.id.tv_insu_complete_sick:

            break;

            case R.id.tv_web_dadi:
                L.e("打开官网");
                if(!CMethod.isFastDoubleClick()){
				    try{
					    Intent intent_net = new Intent();
					    intent_net.setAction("android.intent.action.VIEW");
					    Uri uri = Uri.parse(AppConstants.DADI_NET);
					    intent_net.setData(uri);
					    startActivity(intent_net);
				    }catch (Exception e) {
                        T.s("您的手机未安装浏览器");
				    }
                }
                break;
            case R.id.tv_report_by_phone:
                L.e("打电话");

                if(!CMethod.isFastDoubleClick()){
                    Intent intent_phone = new Intent();
                    intent_phone.setAction(Intent.ACTION_CALL);
                    intent_phone.setData(Uri.parse("tel:" + AppConstants.DADI_PHONE));
                    startActivity(intent_phone);
                }

                break;
            case R.id.iv_left:
                L.e("关闭");
                Finish();
                break;

            case R.id.tv_ques_detail:
                L.e("常见问题");
                openWebPage(0,"常见问题");

                break;

        }
    }

    private void openWebPage(int openID ,String openTitle){
        if(!CMethod.isFastDoubleClick()){
            Intent i = new Intent(InsuranceCompleteActivity.this,InsuranceArticleActivity.class);
            i.putExtra("openID",openID);
            i.putExtra("openTitle",openTitle);
            startActivity(i);
        }
    }



    private void Finish(){


        finish();
    }

}
