package com.jajale.watch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseActivity;
import com.jajale.watch.R;
import com.jajale.watch.adapter.HealthyPageAdapter;
import com.jajale.watch.entity.GetInfomationListData;
import com.jajale.watch.entity.InfomationItemEntity;
import com.jajale.watch.entity.SmartWatchListData;
import com.jajale.watch.entitydb.SmartWatch;
import com.jajale.watch.helper.ListViewFooterHelper;
import com.jajale.watch.listener.HealthyItemSelectedListener;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.utils.CMethod;

import com.jajale.watch.utils.HttpUtils;

import com.jajale.watch.utils.DisplayUtil;

import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * Created by athena on 2016/2/17.
 * Email: lizhiqiang@bjjajale.com
 */
public class HealthyActivity extends BaseActivity {

    private LoadingDialog loadingDialog;
    private ListView mListView;
    private PtrClassicFrameLayout mPtrFrame;

    private HealthyPageAdapter adapter;
    private ListViewFooterHelper<HealthyPageAdapter> listViewFooterHelper;

    private ImageView ivLeft;

    private boolean isRefreshFoot;
    private int visiableCount = 0;

    private int pagesize = 10;
    private int pageindex = 1;
    private SmartWatch mWatch;

    boolean isloadFoot = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_healthy);
        mWatch = getIntent().getParcelableExtra(SmartWatchListData.KEY);
        initView();
        doSearch(true);
    }

    private void initView() {
        loadingDialog = new LoadingDialog(HealthyActivity.this);
        View title = findViewById(R.id.title);
        TextView midTitle = (TextView) title.findViewById(R.id.tv_middle);
        midTitle.setText(getResources().getString(R.string.function_name_helthy));
        ivLeft = (ImageView) findViewById(R.id.iv_left);
        ivLeft.setImageResource(R.drawable.title_goback_selector);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });

        mListView = (ListView) findViewById(R.id.lv_healthy_content);
        listViewFooterHelper = new ListViewFooterHelper<HealthyPageAdapter>(mListView);

        mPtrFrame = (PtrClassicFrameLayout) this.findViewById(R.id.rotate_header_list_view_frame);
//        mPtrFrame.setLastUpdateTimeRelateObject(this);
        final MaterialHeader header=new MaterialHeader(this);
        header.setPtrFrameLayout(mPtrFrame);
        int[] colors = getResources().getIntArray(R.array.material_colors);
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, DisplayUtil.designedDP2px(15),0,DisplayUtil.designedDP2px(10));
        mPtrFrame.setDurationToClose(100);
        mPtrFrame.setPinContent(true);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
//                pageindex = 1;
                doSearch(true);
                listViewFooterHelper.showLoading();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        // the following are default settings
        mPtrFrame.setResistance(2.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(100);
        // default is false
        mPtrFrame.setPullToRefresh(false);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);

        adapter = new HealthyPageAdapter(HealthyActivity.this, new HealthyItemSelectedListener() {
            @Override
            public void onGrowsSelected() {
                L.e("onGrow");
                if (CMethod.isNet(HealthyActivity.this)) {
                    Intent intent_grow = new Intent();
                    intent_grow.setClass(HealthyActivity.this, GrowActivity.class);
                    Bundle mBundle_tel = new Bundle();
                    mBundle_tel.putParcelable(SmartWatchListData.KEY, mWatch);//传递宝贝信息
                    intent_grow.putExtras(mBundle_tel);
                    startActivity(intent_grow);
                } else {
                    T.s("网络不给力");
                }
            }

            @Override
            public void onVaccineSelecterd() {
                L.e("onVaccine");
                if (CMethod.isNet(HealthyActivity.this)) {
                    Intent intent_vaccine = new Intent();
                    intent_vaccine.setClass(HealthyActivity.this, VaccineActivity.class);
                    Bundle mBundle_tel = new Bundle();
                    mBundle_tel.putParcelable(SmartWatchListData.KEY, mWatch);//传递宝贝信息
                    intent_vaccine.putExtras(mBundle_tel);
                    startActivity(intent_vaccine);
                } else {
                    T.s("网络不给力");
                }

            }

            @Override
            public void onExpectSelected() {
                L.e("onExpect");
            }

        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (CMethod.isNet(HealthyActivity.this)) {
                    if (position > 1 && position - 1 <=adapter.lists.size()) {
                        Intent i = new Intent(HealthyActivity.this, InfomationActivity.class);
                        i.putExtra("info_msg_id", adapter.getItem(position - 2).getMsgID());
                        startActivity(i);
                    }
                } else {
                    T.s("请检查网络连接");
                }
            }
        });

        mListView.setAdapter(adapter);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && isRefreshFoot) {
                    if (listViewFooterHelper.isLoading()) {//滑动到底部了，并且底部显示正在加载
                        //没有数据了显示没有数据了，或者隐藏
                        listViewFooterHelper.showLoading();
                        if (isloadFoot) {
                            isloadFoot = false;
                            doSearch(false);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                visiableCount = visibleItemCount;

                // 判断是否滑动到底部
                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    isRefreshFoot = true;
                } else {
                    isRefreshFoot = false;
                }
            }
        });

    }


    //    资讯类型 1.健康、2.教育、3.旅游、4.安全、5.保险、6.通讯、7.公益
    private void doSearch(final boolean reload) {
        if (reload) {
            pageindex = 1;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", 1);
            jsonObject.put("pageIndex", pageindex + "");
            jsonObject.put("pageSize", pagesize + "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API, AppConstants.JAVA_INFOR_MATION_LIST_URL, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                isloadFoot = true;
                Gson gson = new Gson();
                GetInfomationListData fromJson = gson.fromJson(result, GetInfomationListData.class);
                List<InfomationItemEntity> lists = fromJson.getMsgList();
                adapter.refresh(lists, reload);
                pageindex++;
                L.e(lists.size() + "");
                if (lists.size() < pagesize) {//本次返回不够一页，说明没有数据了
                    listViewFooterHelper.showNoMoreData();
                }
                mPtrFrame.refreshComplete();

            }


            @Override
            public void onFailure(String result) {
                T.s(result);
                loadingDialog.dismiss();
                mPtrFrame.refreshComplete();
                listViewFooterHelper.showClickLoadMore(new ListViewFooterHelper.ClickListener() {
                    @Override
                    public void click(View view) {

                    }
                });
            }

            @Override
            public void onError() {
                loadingDialog.dismiss();
                mPtrFrame.refreshComplete();
                listViewFooterHelper.showClickLoadMore(new ListViewFooterHelper.ClickListener() {
                    @Override
                    public void click(View view) {

                    }
                });
            }
        });


    }


}
