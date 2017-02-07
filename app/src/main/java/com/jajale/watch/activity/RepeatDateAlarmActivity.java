package com.jajale.watch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jajale.watch.BaseActivity;
import com.jajale.watch.IntentAction;
import com.jajale.watch.R;
import com.jajale.watch.adapter.RepeatDateListAdapter;
import com.jajale.watch.utils.CMethod;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 *
 * 设置重复日期
 *
 * Created by lilonghui on 2015/11/25.
 * Email:lilonghui@bjjajale.com
 */
public class RepeatDateAlarmActivity extends BaseActivity implements View.OnClickListener {



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
    @InjectView(R.id.alarm_lv_content)
    ListView alarmLvContent;
    private RepeatDateListAdapter adapter;
    private String[] weeks = new String[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_clock);
        ButterKnife.inject(this);

        tvMiddle.setText(getResources().getString(R.string.alarm_repeat));
        ivLeft.setImageResource(R.drawable.title_goback_selector);
        ivLeft.setOnClickListener(this);
        tvRight.setText(getResources().getString(R.string.sure));
        tvRight.setOnClickListener(this);
        weeks = new String[]{getResources().getString(R.string.sunday), getResources().getString(R.string.monday),
                getResources().getString(R.string.tuesday), getResources().getString(R.string.wednesday),
                getResources().getString(R.string.thursday), getResources().getString(R.string.friday),
                getResources().getString(R.string.saturday)};
        //设置listView
        setRepeatListView();
    }

    private void setRepeatListView() {
        //从上一个页面传过来的选择状态（起到记住选择效果）
        boolean[] booleans = getIntent().getBooleanArrayExtra(IntentAction.KEY_REPEAT_DATE_BOOLEAN_ARRAY);
        adapter = new RepeatDateListAdapter(this,weeks,booleans);
        alarmLvContent.setAdapter(adapter);
        alarmLvContent.setOnItemClickListener(OnItemClickListener);
    }

    /**
     * listview 点击checkbox选中
     */
    AdapterView.OnItemClickListener OnItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (CMethod.isFastDoubleClick()){
                return;
            }
            adapter.booleans[position]=!adapter.booleans[position];
            adapter.notifyDataSetChanged();
        }
    };


    @Override
    public void onClick(View v) {
        if (CMethod.isFastDoubleClick()){
            return;
        }
        switch (v.getId()) {
            case R.id.iv_left://返回键
                finish();
                break;
            case R.id.tv_right://确定键

                //将选中状态传回给上一页面
                Intent intent = new Intent();
                intent.putExtra(IntentAction.KEY_REPEAT_DATE_BOOLEAN_ARRAY, adapter.booleans);
                setResult(1, intent);
                finish();

                break;

        }
    }
}
