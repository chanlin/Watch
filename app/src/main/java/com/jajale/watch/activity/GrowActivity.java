package com.jajale.watch.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jajale.watch.AppConstants;
import com.jajale.watch.R;
import com.jajale.watch.adapter.GrowFragmentAdapter;
import com.jajale.watch.entity.GrowPageEntity;
import com.jajale.watch.entity.GrowRecordData;
import com.jajale.watch.entity.SmartWatchListData;
import com.jajale.watch.entitydb.SmartWatch;
import com.jajale.watch.entityno.RecordDictionary;
import com.jajale.watch.entityno.UMeventId;
import com.jajale.watch.fragment.HealthyRecordFragment;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.manager.BaseRecordDictionary;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.GrowRecordUtils;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.TabPageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 成长
 * Created by chunlongyuan on 11/23/15.
 */
public class GrowActivity extends NoNetworkActivity implements View.OnClickListener,HealthyRecordFragment.OnArticleSelectedListener {

    private TextView tv_middle;
    public static SmartWatch currentWatch;
    private GrowPageEntity pageEntity = new GrowPageEntity();
    public static final String EXTRA_WATCH = "extra_watch";
    public static RecordDictionary dictionary;
    private GrowFragmentAdapter fragmentAdapter;
    private ViewPager viewPager;

    private LoadingDialog loadingDialog;



    @Override
    public void refreshView() {
        super.refreshView();
        loadingDialog.show();
        getGrowList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_grow);
        showNotworkView();
        loadingDialog=new LoadingDialog(this);
        MobclickAgent.onEvent(this, UMeventId.UMENG_GROWTH_USING_NUMBER);
        viewPager = (ViewPager) findViewById(R.id.pager);


        findViewById(R.id.iv_left).setOnClickListener(this);
        findViewById(R.id.tv_right).setOnClickListener(this);


        tv_middle = (TextView) findViewById(R.id.tv_middle);
        currentWatch= getIntent().getParcelableExtra(SmartWatchListData.KEY);

        dictionary = BaseRecordDictionary.getBaseDictionary(GrowActivity.this);

        L.e(dictionary.dictionary_record.size() + "<<<---");

        tv_middle.setText(currentWatch.getNick_name() + "的成长");

        loadingDialog.show();
        getGrowList();

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
            case R.id.tv_right:
                if (currentWatch.getIs_manage()==1) {
                    Intent intent_r = new Intent();
                    intent_r.setClass(GrowActivity.this, AddGrowRecordActivity.class);
                    intent_r.putExtra(AddGrowRecordActivity.EXTRA_WATCH, currentWatch);
                    startActivityForResult(intent_r, AddGrowRecordActivity.TO_ADD_GROW_RECORD);
                } else {
                    T.s("没有管理员权限");
                }

                break;
        }
    }

//    /**
//     * 切换选择baby的popupwindow
//     *
//     * @param v
//     */
//    private void switchBabyChoice(View v) {
//
//        if (popupWindow != null && popupWindow.isShowing()) {
//            //关闭
//            popupWindow.dismiss();
//        } else {
//            //show
//            if (popupWindow == null) {
//                initPopupWindow();
//            }
//            popupWindow.showAsDropDown(rl_title, 0, 0);
//            btn_arrow.setEnabled(false);
//        }
//    }

//    private void initPopupWindow() {
//
//        final GrowChoiceBabyAdapter adapter;
//
//        View view = LayoutInflater.from(this).inflate(R.layout.view_choice_baby, null);
//        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        ListView lv_content = (ListView) view.findViewById(R.id.lv_content);
//        adapter = new GrowChoiceBabyAdapter(this, pageEntity);
//        lv_content.setAdapter(adapter);
//        lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                //这个position是popup的position
//                if (CMethod.isFastDoubleClick()) {
//                    return;
//                }
//
//                currentWatch = pageEntity.getWatches().get(position);
//                SmartWatch entity = currentWatch;
//                if (!adapter.isPositionSelected(position)) {
//                    tv_middle.setText(entity.getNick_name() + "的成长");
//                    adapter.cancelOtherSelected();
//                    adapter.selectItem(position);
//                    adapter.notifyDataSetChanged();
//                    //通知fragment刷新
//                    loadingDialog.show();
//                    getGrowList();
//
//                }
//                pageEntity.setCurrent_index(position);
//                adapter.notifyDataSetChanged();
//                popupWindow.dismiss();
//            }
//        });
//        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                btn_arrow.setEnabled(true);
//            }
//        });
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//        popupWindow.setTouchable(true);
//        popupWindow.setFocusable(true);
//        popupWindow.setOutsideTouchable(true);
//        popupWindow.setAnimationStyle(0);
//    }

    private void getGrowList(){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", currentWatch.getUser_id());
            jsonObject.put("pageIndex", "1");
            jsonObject.put("pageSize", "100");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API, AppConstants.JAVA_BABY_GROWTH_LIST_URL, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                loadingDialog.dismiss();
                Gson gson = new Gson();
                GrowRecordData growRecordData = gson.fromJson(result, GrowRecordData.class);
                if (growRecordData!=null&&growRecordData.growthList.size()!=0){
                    for (int i = 0; i <growRecordData.growthList.size() ; i++) {
                        int month = GrowRecordUtils.getMonthByBirthday(growRecordData.growthList.get(i).create_time, currentWatch.getBirthday());
                        growRecordData.growthList.get(i).setMonth(month);
                    }
                }

                if (fragmentAdapter==null){
                    fragmentAdapter = new GrowFragmentAdapter(GrowActivity.this, growRecordData, getSupportFragmentManager());
                    viewPager.setAdapter(fragmentAdapter);
                    viewPager.setOffscreenPageLimit(3);

                    TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
                    indicator.setViewPager(viewPager);
                    viewPager.setScrollContainer(false);
                }else{

                    for (int i = 0; i < fragmentAdapter.getCount(); i++) {
                        fragmentAdapter.refresh(i, growRecordData);
                    }

                }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddGrowRecordActivity.TO_ADD_GROW_RECORD && resultCode == Activity.RESULT_OK) {
            getGrowList();

        }

    }

    public SmartWatch getCurrentWatch() {
        return currentWatch;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRefresh() {
        getGrowList();
    }
}