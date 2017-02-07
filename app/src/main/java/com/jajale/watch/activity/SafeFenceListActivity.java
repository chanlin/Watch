package com.jajale.watch.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseApplication;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.jajale.watch.IntentAction;
import com.jajale.watch.R;
import com.jajale.watch.adapter.SafeFenceListAdapter;
import com.jajale.watch.dao.AMapGeocodeHelper;
import com.jajale.watch.entity.LocationUserInfoEntity;
import com.jajale.watch.entity.SafeFenceData;
import com.jajale.watch.entityno.UMeventId;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.interfaces.GeocodeEventHandler;
import com.jajale.watch.listener.SimpleClickListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.DialogUtils;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 安全围栏列表
 * <p/>
 * Created by lilonghui on 2016/1/12.
 * Email:lilonghui@bjjajale.com
 */
public class SafeFenceListActivity extends NoNetworkActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

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
    @InjectView(R.id.safe_fence_lv_content)
    ListView safeFenceLvContent;
    @InjectView(R.id.safe_fence_ll_no_data)
    LinearLayout safeFenceLlNoData;
    private LocationUserInfoEntity locationUserInfoEntity;
    private LoadingDialog loadingDialog;
    private SafeFenceListAdapter safeFenceListAdapter;
    private String baby_address;

    @Override
    public void refreshView() {
        super.refreshView();
        loadingDialog.show();
        getListDataFromNetWork();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_fence_list);
        ButterKnife.inject(this);
        showNotworkView();
        MobclickAgent.onEvent(SafeFenceListActivity.this, UMeventId.UMENG_SAFE_FENCE_INTO);
        loadingDialog = new LoadingDialog(this);

        tvMiddle.setText(getResources().getString(R.string.save_fence));
        ivLeft.setImageResource(R.drawable.title_goback_selector);
        ivLeft.setOnClickListener(this);
        //设置右边按钮
        ivRight.setImageResource(R.mipmap.add_family_member);
        ivRight.setOnClickListener(this);
        safeFenceLvContent.setOnItemClickListener(this);
        safeFenceLvContent.setOnItemLongClickListener(this);

        locationUserInfoEntity = getIntent().getParcelableExtra(IntentAction.KEY_SAFE_FRENCE);
        baby_address = locationUserInfoEntity.getAddress();
        if (CMethod.isEmpty(baby_address)){
            getAddress(locationUserInfoEntity);
        }
//        loadingDialog.show();
        getListDataFromNetWork();//从网络获取列表并展示

    }

    public void getAddress(final LocationUserInfoEntity location) {
        AMapGeocodeHelper aMapGeocodeModel = new AMapGeocodeHelper(new GeocodeEventHandler() {
            @Override
            public void onRegeocodeAMap(int code, RegeocodeResult regeocodeResult) {
                if (code == 0) {
                    String formatAddress = regeocodeResult.getRegeocodeAddress().getFormatAddress();
                    baby_address=formatAddress;
                }
            }

            @Override
            public void onGeocodeAMap(int code, GeocodeResult geocodeResult) {
            }
        });
        aMapGeocodeModel.regeocodeSearch(location.getLatLng().latitude, location.getLatLng().longitude);
    }


    /**
     * 从网络获取列表并展示
     */
    private void getListDataFromNetWork() {

        List<SafeFenceData.SafeListEntity> safeListEntities = new ArrayList<SafeFenceData.SafeListEntity>();
        SafeFenceData.SafeListEntity safeListEntity = new SafeFenceData.SafeListEntity();
        safeListEntity.setSafe_id("1");
        safeListEntity.setSafe_title(getResources().getString(R.string.home));
        safeListEntity.setGps_lat(locationUserInfoEntity.getLatLng().latitude + "");
        safeListEntity.setGps_lon(locationUserInfoEntity.getLatLng().longitude + "");
        safeListEntity.setRadius(500);
        safeListEntity.setAddress(baby_address);
        safeListEntities.add(safeListEntity);
        if (safeListEntities.size() == 0) {
            safeFenceLvContent.setVisibility(View.GONE);
            safeFenceLlNoData.setVisibility(View.VISIBLE);
        } else {
            safeFenceLvContent.setVisibility(View.VISIBLE);
            safeFenceLlNoData.setVisibility(View.GONE);
            safeFenceListAdapter = new SafeFenceListAdapter(SafeFenceListActivity.this, safeListEntities);
            safeFenceLvContent.setAdapter(safeFenceListAdapter);
        }

//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("user_id", BaseApplication.getUserInfo().userID);//用户ID
//            jsonObject.put("friend_id",locationUserInfoEntity.getWatchID());//手表ID
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API, AppConstants.JAVA_WATCH_GET_WATCH_SAFE_AREA_LIST_URL, jsonObject, new HttpClientListener() {
//            @Override
//            public void onSuccess(String result) {
//                loadingDialog.dismiss();
//                Gson gson = new Gson();
//                SafeFenceData fromJson = gson.fromJson(result, SafeFenceData.class);
//                List<SafeFenceData.SafeListEntity> safeListEntities = fromJson.getSafeList();
//                if (safeListEntities.size() == 0) {
//                    safeFenceLvContent.setVisibility(View.GONE);
//                    safeFenceLlNoData.setVisibility(View.VISIBLE);
//                } else {
//                    safeFenceLvContent.setVisibility(View.VISIBLE);
//                    safeFenceLlNoData.setVisibility(View.GONE);
//                    safeFenceListAdapter = new SafeFenceListAdapter(SafeFenceListActivity.this, safeListEntities);
//                    safeFenceLvContent.setAdapter(safeFenceListAdapter);
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(String result) {
//                loadingDialog.dismiss();
//                T.s(result);
//            }
//
//            @Override
//            public void onError() {
//                loadingDialog.dismiss();
//            }
//        });

    }

    /**
     * 当编辑页面保存成功返回后，刷新列表
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            getListDataFromNetWork();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_left://返回
                finish();

                break;
            case R.id.iv_right://添加安全围栏
                if (!CMethod.isNet(this)) {
                    T.s(getResources().getString(R.string.no_network));
                    return;
                }

                if (CMethod.isEmpty(baby_address)){
                    T.s(getResources().getString(R.string.location_loading));
                }
                if (safeFenceListAdapter == null || safeFenceListAdapter.getCount() < 5) {
                    toSafeFenceEditActivity("", locationUserInfoEntity.getLatLng().latitude, locationUserInfoEntity.getLatLng().longitude,
                            locationUserInfoEntity.getSex(), baby_address, "", locationUserInfoEntity.getWatchID(), 500, safeFenceListAdapter == null ? null : safeFenceListAdapter.mSafeListEntities);
                } else if (safeFenceListAdapter.getCount() >= 5) {
                    T.s("您已达到添加上限，请删除后再做添加");
                }
                break;


        }


    }


    /**
     * 跳转添加或编辑页面
     *
     * @param safe_fence_safeId    为编辑安全区域必要参数，传""则为从添加入口进入，反之为从编辑入口
     * @param safe_fence_lat       经纬度为初始圆心
     * @param safe_fence_lon       经纬度为初始圆心
     * @param safe_fence_sex       为设置宝贝头像的必要参数
     * @param safe_fence_address   为初始地址（省却开始就逆地理获取，提高用户体验）
     * @param safe_fence_fenceName 为区域名称初始显示，传""则为从添加入口进入，反之为从编辑入口
     * @param safe_fence_watchId   为编辑或添加安全区域必要参数
     * @param safe_fence_radius    为编辑或添加安全区域必要参数
     */
    private void toSafeFenceEditActivity(String safe_fence_safeId, double safe_fence_lat, double safe_fence_lon, int safe_fence_sex, String safe_fence_address,
                                         String safe_fence_fenceName, String safe_fence_watchId, int safe_fence_radius, List<SafeFenceData.SafeListEntity> list) {
        Intent intent = new Intent(SafeFenceListActivity.this, SafeFenceEditActivity.class);
        intent.putExtra(IntentAction.SAFE_FENCE_LAT, safe_fence_lat);
        intent.putExtra(IntentAction.SAFE_FENCE_LON, safe_fence_lon);
        intent.putExtra(IntentAction.SAFE_FENCE_SEX, safe_fence_sex);
        intent.putExtra(IntentAction.SAFE_FENCE_ADDRESS, safe_fence_address);
        intent.putExtra(IntentAction.SAFE_FENCE_FENCENAME, safe_fence_fenceName);
        intent.putExtra(IntentAction.SAFE_FENCE_WATCHID, safe_fence_watchId);
        intent.putExtra(IntentAction.SAFE_FENCE_SAFEID, safe_fence_safeId);
        intent.putExtra(IntentAction.SAFE_FENCE_RADIU, safe_fence_radius);
        intent.putExtra(IntentAction.SAFE_FENCE_LIST, (Serializable) list);
        startActivityForResult(intent, 1);

    }

    /**
     * 点击进入该区域编辑页面
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        double lat = CMethod.parseDouble(safeFenceListAdapter.getItem(position).getGps_lat());
        double lon = CMethod.parseDouble(safeFenceListAdapter.getItem(position).getGps_lon());

        List<SafeFenceData.SafeListEntity> list = new ArrayList<SafeFenceData.SafeListEntity>();
        for (int i = 0; i < safeFenceListAdapter.mSafeListEntities.size(); i++) {
            if (i != position)
                list.add(safeFenceListAdapter.mSafeListEntities.get(i));
        }
        toSafeFenceEditActivity(safeFenceListAdapter.getItem(position).getSafe_id(), lat, lon, locationUserInfoEntity.getSex(), safeFenceListAdapter.getItem(position).getAddress(),
                safeFenceListAdapter.getItem(position).getSafe_title(), locationUserInfoEntity.getWatchID(), safeFenceListAdapter.getItem(position).getRadius(), list);
    }


    /**
     * 长按点击删除
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

        DialogUtils.deleteSaveFenceDialog(SafeFenceListActivity.this, new SimpleClickListener() {
            @Override
            public void ok() {
                deleteSaveFenceToNetWork(position);
            }

            @Override
            public void cancle() {
            }
        });
        return true;
    }


    /**
     * 删除安全区域
     */
    private void deleteSaveFenceToNetWork(final int position) {
        loadingDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id",locationUserInfoEntity.getWatchID());//用户ID
            jsonObject.put("safe_id", safeFenceListAdapter.getItem(position).getSafe_id());//
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API,AppConstants.JAVA_WATCH_DELETE_SAFE_AREA_URL, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                loadingDialog.dismiss();
                safeFenceListAdapter.deleteItem(position);
                if (safeFenceListAdapter.getCount() == 0) {
                    safeFenceLvContent.setVisibility(View.GONE);
                    safeFenceLlNoData.setVisibility(View.VISIBLE);
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
}
