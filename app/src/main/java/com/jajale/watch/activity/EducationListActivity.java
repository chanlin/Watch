package com.jajale.watch.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jajale.watch.BaseActivity;
import com.jajale.watch.R;
import com.jajale.watch.adapter.ReaderDetailListAdapter;
import com.jajale.watch.entity.BookListData;
import com.jajale.watch.entity.SPKeyConstants;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 书籍目录页面
 * <p/>
 * Created by athena on 2015/12/31.
 * Email: lizhiqiang@bjjajale.com
 */
public class EducationListActivity extends BaseActivity implements View.OnClickListener {

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
    @InjectView(R.id.layout_title)
    RelativeLayout layoutTitle;
    @InjectView(R.id.not_disturb_lv_content)
    ListView notDisturbLvContent;
    private List<BookListData> bookList;
    private SharedPreferences sp;
    private ReaderDetailListAdapter adapter;
    private String mPath;
    private int list_length;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_education);
        ButterKnife.inject(this);

        sp = this.getSharedPreferences(SPKeyConstants.BOOK_SP_NAME, Context.MODE_PRIVATE);

        getIntentData();
        initView();
        setAdapter();
    }

    private void getIntentData() {
        mPath = getIntent().getStringExtra("filePath");//从上一个页面获取的路径作为保存position的key值
        list_length = getIntent().getIntExtra("list_length", 0);//获取目录json长度
        bookList = (List<BookListData>) getIntent().getSerializableExtra("bookListData");
        title = getIntent().getStringExtra("book_title");
    }

    private void initView() {
//        layoutTitle.setBackgroundColor(getResources().getColor(R.color.edu_title_bg));
        layoutTitle.setBackgroundResource(R.mipmap.nav_bar);
        tvMiddle.setText(title);

        ivLeft.setImageResource(R.drawable.title_goback_selector);
        ivLeft.setOnClickListener(this);

        notDisturbLvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //将选中状态保存，然后返回上一页面

                SharedPreferences.Editor editor = sp.edit();
                editor.putInt(mPath + "begin", adapter.mBookList.get(position).getOffset_b() + list_length);
                editor.putInt(mPath + "end", adapter.mBookList.get(position).getOffset_b() + list_length + 300);
                editor.apply();
                Intent intent = new Intent();
                setResult(1, intent);
                finish();
            }
        });
    }


    private void setAdapter() {
        adapter = new ReaderDetailListAdapter(this, bookList);
        notDisturbLvContent.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left://返回键
                finish();
                break;
        }
    }
}
