package com.jajale.watch.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseActivity;
import com.jajale.watch.IntentAction;
import com.jajale.watch.R;
import com.jajale.watch.entity.SafeFenceData;
import com.jajale.watch.entityno.UMeventId;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 安全围栏编辑
 * <p/>
 * Created by lilonghui on 2016/1/12.
 * Email:lilonghui@bjjajale.com
 */
public class SafeFenceEditActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener
        , AMap.OnMapClickListener, GeocodeSearch.OnGeocodeSearchListener {


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
    @InjectView(R.id.safe_fence_sb_radius)
    SeekBar safeFenceSbRadius;
    @InjectView(R.id.safe_fence_tv_radius)
    TextView safeFenceTvRadius;
    @InjectView(R.id.safe_fence_rt_name)
    EditText safeFenceRtName;
    @InjectView(R.id.safe_fence_tv_address)
    TextView safeFenceTvAddress;
    @InjectView(R.id.safe_fence_btn_add)
    Button safeFenceBtnAdd;

    private int present_radius = 500;
    private float present_scale = 14;
    private Circle circle;
    private double first_lat;
    private double first_lon;
    private int sex;
    private String address;
    private GeocodeSearch geocoderSearch;
    private String watchID;
    private LatLng present_latLng;

    private LoadingDialog loadingDialog;
    private String fenceName;
    private String safe_fence_safeId;
    private MapView mapView;
    private AMap aMap;
    private List<SafeFenceData.SafeListEntity> safeFenceList;
    private String loading_get_address ="正在获取地址...";
    private String fail_get_address ="地理位置获取失败";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safe_fence_edit);
        ButterKnife.inject(this);
        loadingDialog = new LoadingDialog(this);
        tvMiddle.setText(getResources().getString(R.string.save_edit_fence));
        ivLeft.setImageResource(R.drawable.title_goback_selector);
        ivLeft.setOnClickListener(this);
        //设置右边按钮
        ivRight.setImageResource(R.mipmap.title_search);
        ivRight.setOnClickListener(this);
        safeFenceBtnAdd.setOnClickListener(this);
        safeFenceSbRadius.setOnSeekBarChangeListener(this);
        mapView = (MapView) findViewById(R.id.mapView);
        getIntentData();
        present_latLng = new LatLng(first_lat, first_lon);

        mapView.onCreate(savedInstanceState); // 此方法必须重写
        initMapView();
        setSafeFence(present_latLng);
        aMap.setOnMapClickListener(this);

        //逆地理监听的注册（获取地理位置）
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);

        initData();
    }

    /**
     * 初始化数据，包括安全区域名称，地址，半径，seekBar百分比
     */
    private void initData() {
        if (!CMethod.isEmpty(address)) {
            safeFenceTvAddress.setText(address);
        }
        if (!CMethod.isEmpty(fenceName)) {
            safeFenceRtName.setText(fenceName);
            safeFenceRtName.setSelection(fenceName.length());
        }
        safeFenceTvRadius.setText(present_radius + "m");
        safeFenceSbRadius.setProgress(getSeekBarProgress(present_radius));
    }

    /**
     * 获取从SafeFenceListActivity传递过来的数据，经纬度为初始圆心，sex为设置宝贝头像的必要参数，address为初始地址（省却开始就逆地理获取，提高用户体验）
     * watchID为编辑或添加安全区域必要参数，fenceName为区域名称初始显示，传""则为从添加入口进入，反之为从编辑入口，safe_fence_safeId为编辑安全区域必要
     * 参数，传""则为从添加入口进入，反之为从编辑入口,SAFE_FENCE_RADIU为编辑页面传递进来的半径
     */
    private void getIntentData() {
        first_lat = getIntent().getDoubleExtra(IntentAction.SAFE_FENCE_LAT, 0);
        first_lon = getIntent().getDoubleExtra(IntentAction.SAFE_FENCE_LON, 0);
        sex = getIntent().getIntExtra(IntentAction.SAFE_FENCE_SEX, 1);
        address = getIntent().getStringExtra(IntentAction.SAFE_FENCE_ADDRESS);
        watchID = getIntent().getStringExtra(IntentAction.SAFE_FENCE_WATCHID);
        fenceName = getIntent().getStringExtra(IntentAction.SAFE_FENCE_FENCENAME);
        safe_fence_safeId = getIntent().getStringExtra(IntentAction.SAFE_FENCE_SAFEID);
        present_radius = getIntent().getIntExtra(IntentAction.SAFE_FENCE_RADIU, 500);
        safeFenceList = (List<SafeFenceData.SafeListEntity>) getIntent().getSerializableExtra(IntentAction.SAFE_FENCE_LIST);

    }


    /**
     * 判断是否与其他围栏重合
     * @param latLng
     * @param radius
     * @return
     */
    private boolean isCircleCoincide(LatLng latLng, int radius) {
        if (safeFenceList != null && safeFenceList.size() > 0) {
            for (int i = 0; i < safeFenceList.size(); i++) {
                double lat = parseDouble(safeFenceList.get(i).getGps_lat());
                double lon = parseDouble(safeFenceList.get(i).getGps_lon());
                if (lat!=0&&lon!=0){
                    LatLng latLng2 = new LatLng(lat, lon);
                    double distance = AMapUtils.calculateLineDistance(latLng, latLng2);
                    int radius2 = safeFenceList.get(i).getRadius();

                    if (distance < radius + radius2) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * 设置安全区域
     *
     * @param latLng
     */
    private void setSafeFence(LatLng latLng) {
        present_latLng = latLng;
        if (latLng.latitude != 0 && latLng.longitude != 0) {
            initCircle(latLng);
            addMarker(latLng);
            moveCircle(latLng);

        }
    }

    /**
     * 初始化圆圈
     *
     * @param latLng
     */
    private void initCircle(LatLng latLng) {
        aMap.clear();
        circle = aMap.addCircle(new CircleOptions().center(latLng)
                .radius(present_radius).strokeColor(Color.argb(100, 18, 160, 250))
                .fillColor(Color.argb(40, 18, 160, 250)).strokeWidth(2));

    }


    /**
     * 添加圆心中间的宝贝头像
     *
     * @param latLng
     */
    private void addMarker(LatLng latLng) {
        View view_ = LayoutInflater.from(this).inflate(R.layout.layout_map_marker, null);
        ImageView imageView = (ImageView) view_.findViewById(R.id.image_head);
        if (sex == 0) {
            imageView.setImageResource(R.mipmap.head_image_girl_location);
        } else {
            imageView.setImageResource(R.mipmap.head_image_boy_location);
        }
        MarkerOptions options = new MarkerOptions();
        options.position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(CMethod.getViewBitmap(view_)));
        aMap.addMarker(options);

    }

    /**
     * 移动圆圈
     *
     * @param latLng
     */
    private void moveCircle(LatLng latLng) {

        CameraUpdate update = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                latLng, present_scale, 0, 0));
        aMap.animateCamera(update);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_left://返回
                finish();
                break;
            case R.id.iv_right://关键字搜索
                Intent intent = new Intent(SafeFenceEditActivity.this, SafeFenceSearchActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.safe_fence_btn_add://添加或编辑
                if(!CMethod.isNet(this)){
                    T.s(getResources().getString(R.string.no_network));
                    return;
                }
                if (loading_get_address.equals(safeFenceTvAddress.getText().toString())){
                    T.s(getResources().getString(R.string.location_loading));
                    return;
                }
                if (fail_get_address.equals(safeFenceTvAddress.getText().toString())){
                    T.s(getResources().getString(R.string.relay_location));
                    return;
                }

                String name = safeFenceRtName.getText().toString();
                if (CMethod.isEmpty(name)) {
                    T.s(getResources().getString(R.string.not_null_empty));
                } else if (isCircleCoincide(present_latLng,present_radius)) {
                    T.s(getResources().getString(R.string.coincide_fence));
                } else if ("".equals(safe_fence_safeId)) {//当safeid为“”时，为增加数据，反之为编辑数据
//                    addSafeFenceDataToNetWork();
                    finish();
                } else {
//                    editSafeFenceDataToNetWork();
                }


                break;


        }


    }



    /**
     * 添加安全区域
     */
    private void addSafeFenceDataToNetWork() {
        loadingDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", watchID);//用户ID
            jsonObject.put("gps_lat", present_latLng.latitude);//纬度(圆心)
            jsonObject.put("gps_lon", present_latLng.longitude);//经度(圆心)
            jsonObject.put("radius", present_radius);//半径
            jsonObject.put("safe_title", safeFenceRtName.getText().toString());//围栏名称
            jsonObject.put("address", safeFenceTvAddress.getText().toString());// 圆心地址
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API, AppConstants.JAVA_WATCH_ADD_SAFE_AREA_URL, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                loadingDialog.dismiss();
                MobclickAgent.onEvent(SafeFenceEditActivity.this, UMeventId.UMENG_SAFE_FENCE_SETTING);
                setResult(1, null);
                finish();
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


    /**
     * 编辑安全区域
     */
    private void editSafeFenceDataToNetWork() {
        loadingDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("user_id", watchID);//用户ID
            jsonObject.put("gps_lat", present_latLng.latitude);//纬度(圆心)
            jsonObject.put("gps_lon", present_latLng.longitude);//经度(圆心)
            jsonObject.put("radius", present_radius);//半径
            jsonObject.put("safe_title", safeFenceRtName.getText().toString());//围栏名称
            jsonObject.put("address", safeFenceTvAddress.getText().toString());//圆心地址
            jsonObject.put("safe_id", safe_fence_safeId);//safe_id
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API,AppConstants.JAVA_WATCH_EDIT_SAFE_AREA_URL, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                loadingDialog.dismiss();
                MobclickAgent.onEvent(SafeFenceEditActivity.this, UMeventId.UMENG_SAFE_FENCE_SETTING);
                setResult(1, null);
                finish();
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


    /**
     * 从search页面返回回来的数据，移动区域，显示地理位置
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            LatLonPoint point = data.getParcelableExtra("safe_fence_point");
            String address = data.getStringExtra("safe_fence_address");
            if (!CMethod.isEmpty(address))
                safeFenceTvAddress.setText(address);
            if (point != null) {
                LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
                setSafeFence(latLng);
            }
        }
    }


    /**
     * 初始化AMap对象
     */
    private void initMapView() {

        if (aMap == null) {
            aMap = mapView.getMap();
        }
    }


    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * 通过seekBar得到半径长度，每50米为判断
     *
     * @param progress
     * @return
     */
    private int getRadiusLength(int progress) {
        int position = progress / 7;
        return position * 50 + 300;
    }

    /**
     * 通过半径长度，得到SeekBarprogress
     *
     * @param radius
     * @return
     */
    private int getSeekBarProgress(int radius) {
        int progress = (radius - 300) / 50 * 7;
        return progress;
    }

    /**
     * seekBar移动监听
     *
     * @param seekBar
     * @param progress
     * @param fromUser
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        present_radius = getRadiusLength(progress);
        safeFenceTvRadius.setText(present_radius + "m");
        circle.setRadius(present_radius);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    /**
     * 地图点击移动安全区域
     *
     * @param latLng
     */
    @Override
    public void onMapClick(LatLng latLng) {
        present_scale = aMap.getCameraPosition().zoom;
        aMap.clear();
        getAddress(new LatLonPoint(latLng.latitude, latLng.longitude));
        setSafeFence(latLng);
    }


    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        safeFenceTvAddress.setText(loading_get_address);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == 0) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                String addressName = result.getRegeocodeAddress().getFormatAddress();
                addressName = addressName.equals("") ? fail_get_address : addressName + getResources().getString(R.string.nearby);
                safeFenceTvAddress.setText(addressName);
            } else {
                safeFenceTvAddress.setText(fail_get_address);
                L.e("无结果");
            }
        } else if (rCode == 27) {
            safeFenceTvAddress.setText(fail_get_address);
            L.e("网络错误");
        } else if (rCode == 32) {
            safeFenceTvAddress.setText(fail_get_address);
            L.e("错误的kEy");
        } else {
            safeFenceTvAddress.setText(fail_get_address);
            L.e("其他错误");
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }
}
