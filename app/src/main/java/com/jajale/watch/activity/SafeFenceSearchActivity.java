package com.jajale.watch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.district.DistrictItem;
import com.amap.api.services.district.DistrictResult;
import com.amap.api.services.district.DistrictSearch;
import com.amap.api.services.district.DistrictSearchQuery;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Tip;
import com.jajale.watch.BaseActivity;
import com.jajale.watch.R;
import com.jajale.watch.adapter.SafeFenceSearchListAdapter;
import com.jajale.watch.utils.InputMethodUtils;
import com.jajale.watch.utils.LoadingDialog;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 安全围栏编辑
 * <p>
 * Created by lilonghui on 2016/1/12.
 * Email:lilonghui@bjjajale.com
 */
public class SafeFenceSearchActivity extends BaseActivity implements View.OnClickListener, TextWatcher, AdapterView.OnItemClickListener {


    @InjectView(R.id.iv_left)
    ImageView ivLeft;
    @InjectView(R.id.safe_fence_et_search)
    EditText safeFenceEtSearch;
    @InjectView(R.id.safe_fence_iv_search_delete)
    ImageView safeFenceIvSearchDelete;
    @InjectView(R.id.safe_fence_lv_search)
    ListView safeFenceLvSearch;
    @InjectView(R.id.safe_fence_tv_hint)
    TextView safeFenceTvHint;

    private String hint_no_back = "未搜索到相关地址";
    private String hint_no_start = "正在搜索...";
    private SafeFenceSearchListAdapter adapter;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_fence_search);
        loadingDialog = new LoadingDialog(SafeFenceSearchActivity.this);
        ButterKnife.inject(this);
        ivLeft.setOnClickListener(this);
        safeFenceIvSearchDelete.setOnClickListener(this);
        safeFenceEtSearch.addTextChangedListener(this);
        safeFenceLvSearch.setOnItemClickListener(this);
        InputMethodUtils.openInputMethod(this, safeFenceEtSearch);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left://返回
                InputMethodUtils.closeInputMethod(this, safeFenceEtSearch);
                finish();
                break;
            case R.id.safe_fence_iv_search_delete:
                safeFenceEtSearch.setText("");
                break;
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        setLvOrTv(false, hint_no_start);
        String newText = s.toString().trim();
        if ("".equals(newText)) {
            safeFenceIvSearchDelete.setVisibility(View.GONE);
        } else {
            safeFenceIvSearchDelete.setVisibility(View.VISIBLE);
        }
        Inputtips inputTips = new Inputtips(SafeFenceSearchActivity.this,
                new Inputtips.InputtipsListener() {
                    @Override
                    public void onGetInputtips(List<Tip> tipList, int rCode) {
                        if (rCode == 0) {// 正确返回
                            if (tipList.size() == 0) {
                                setLvOrTv(false, hint_no_back);
                            } else {
                                setLvOrTv(true, "");
                                adapter = new SafeFenceSearchListAdapter(SafeFenceSearchActivity.this, tipList);
                                safeFenceLvSearch.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            setLvOrTv(false, hint_no_back);
                        }
                    }
                });
        try {
            inputTips.requestInputtips(newText, "");// 第一个参数表示提示关键字，第二个参数默认代表全国，也可以为城市区号
        } catch (AMapException e) {
            e.printStackTrace();
            setLvOrTv(false, hint_no_back);
        }

    }


    /**
     * 显示listview还是提示语
     *
     * @param isShowLv
     * @param hint
     */
    private void setLvOrTv(boolean isShowLv, String hint) {
        if (isShowLv) {
            safeFenceLvSearch.setVisibility(View.VISIBLE);
            safeFenceTvHint.setVisibility(View.GONE);
        } else {
            safeFenceLvSearch.setVisibility(View.GONE);
            safeFenceTvHint.setVisibility(View.VISIBLE);
            safeFenceTvHint.setText(hint);
        }
    }
//    new PoiSearch.Query(addres, selectType, city);

    /**
     * 点击条目返回
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        InputMethodUtils.closeInputMethod(this, safeFenceEtSearch);
        Intent intent = new Intent();
        LatLonPoint latLonPoint = adapter.getItem(position).getPoint();
        String address = adapter.getItem(position).getDistrict() + adapter.getItem(position).getName();


        if (latLonPoint == null || latLonPoint.getLatitude() == 0 || latLonPoint.getLongitude() == 0) {
            querySubDistrict(adapter.getItem(position).getName(), address);
        } else {
            intent.putExtra("safe_fence_point", latLonPoint);
            intent.putExtra("safe_fence_address", address);
            setResult(1, intent);
            finish();
        }


    }


    /**
     * 查询下级区划
     * <p>
     * 要查询的区划对象
     */
    private void querySubDistrict(String name, final String address) {
        loadingDialog.show();
        DistrictSearch districtSearch = new DistrictSearch(
                SafeFenceSearchActivity.this);
        districtSearch.setOnDistrictSearchListener(new DistrictSearch.OnDistrictSearchListener() {
            @Override
            public void onDistrictSearched(DistrictResult result) {
                loadingDialog.dismiss();
                if (result != null) {


                    List<DistrictItem> district = result.getDistrict();
                    if (district != null && district.size() >= 0 && district.get(0) != null && district.get(0).getCenter() != null) {
                        Intent intent = new Intent();
                        LatLonPoint latLonPoint=district.get(0).getCenter();
                        intent.putExtra("safe_fence_point", latLonPoint);
                        intent.putExtra("safe_fence_address", address);
                        setResult(1, intent);
                        finish();
                    }


                }
            }
        });
        // 异步查询行政区
        districtSearch.searchDistrictAnsy();
        DistrictSearchQuery query = new DistrictSearchQuery(
                name, DistrictSearchQuery.KEYWORDS_CITY, 0);
        districtSearch.setQuery(query);
    }


}
