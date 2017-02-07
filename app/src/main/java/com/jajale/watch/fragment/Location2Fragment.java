package com.jajale.watch.fragment;

import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.BroadcastConstants;
import com.jajale.watch.IntentAction;
import com.jajale.watch.R;
import com.jajale.watch.activity.HistoryTrackActivity;
import com.jajale.watch.activity.HomeSecActivity;
import com.jajale.watch.activity.SafeFenceListActivity;
import com.jajale.watch.activity.WeatherSearchActivity;
import com.jajale.watch.cviews.InfoWindow;
import com.jajale.watch.dao.AMapGeocodeHelper;
import com.jajale.watch.dialog.MapUiSettingDialog;
import com.jajale.watch.entity.AppInfo;
import com.jajale.watch.entity.BDPosition;
import com.jajale.watch.entity.CityListEntity;
import com.jajale.watch.entity.LocationUserInfoEntity;
import com.jajale.watch.entity.SPKeyConstants;
import com.jajale.watch.entitydb.SmartWatch;
import com.jajale.watch.entityno.UMeventId;
import com.jajale.watch.factory.LocationFactory;
import com.jajale.watch.interfaces.GeocodeEventHandler;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.DialogUtils;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LastLoginUtils;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.MapUtil;
import com.jajale.watch.utils.Md5Util;
import com.jajale.watch.utils.ParentHeadViewLocationUtils;
import com.jajale.watch.utils.PhoneSPUtils;
import com.jajale.watch.utils.SmartWatchUtils;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;
import com.qiniu.android.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 新写的定位页面
 * <p/>
 * Created by lilonghui on 2016/1/13.
 * Email:lilonghui@bjjajale.com
 */
public class Location2Fragment extends BaseFragment implements View.OnClickListener, AMap.OnCameraChangeListener, AMap.OnMapClickListener {
    public static final String TAG = "Location2Fragment";
    private HashMap<String, HashMap<LatLng, String>> addressHashMaps = new HashMap<String, HashMap<LatLng, String>>();
    private String parentID = "0001";
    //百度地图最大缩放级别
    private static final int MAX_LEVEL = 19;
    //各级比例尺分母值数组
    private static final int[] SCALES = {10, 25, 50, 100, 200, 500, 1000, 2000, 5000, 10000
            , 20000, 30000, 50000, 100000, 200000, 500000, 1000000};
    private AMap mMap;
    private PhoneSPUtils appSP;
    private LoadingDialog loadingDialog;
    private View rootView;
    //    private GeocodeSearch geoCoderSearch;
    private Button imageView_to_save_fence;
    private Button imageView_to_history_track;
    private TextView midTitle;
    private String user_address;
    private TextView user_name;
    private ImageView map_bottom_head;
    private Button map_call_phone_btn;
    private Button map_menu_btn, map_photo_btn;
    private Button map_monitor_btn;


    private TextureMapView mMapView;
    private boolean flag;//菜单是否打开

    private List<LocationUserInfoEntity> locationUserInfoEntityLists = new ArrayList<LocationUserInfoEntity>();
    private int present_position = 0;//当前选择的position
    private int present_order = 16;
    private LatLng present_center;

    private String TYPE_ONCLICK = "TYPE_ONCLICK";
    private String TYPE_ALL = "TYPE_ALL";
    private String CUT_STR = "JJL";
    private TextView location_no_data_tv;

    private View location_progress_view;
    private ObjectAnimator animator;
    private Button map_location_btn;
    private Button iv_to_ui_setting;


    InfoWindow infoWindow;
    private ImageView iv_left;
    private ImageView iv_right;
    private ImageView location_phone_btn;
    private Marker phoneMarker;

    private View layout_map_marker;
    private ArrayList<CityListEntity> listEntities = new ArrayList<CityListEntity>();
    String service_last_location = "watch.get_lastest_location";//定位
    String service_req_location = "watch.req_location";//请求定位
    String service_day_location = "watch.get_day_location";//获取某一天时间内的所有定位坐标
    String service_monitor = "watch.monitor";//电话监听
    String service_remote_photo = "watch.remote_photo";//远程拍照监听
    String imei, nick_name, sex;
    String sign = "";
    String URL = "http://lib.huayinghealth.com/lib-x/?";
    private boolean removemap = true;
    private String present_phone;//给手表拨打电话
    private String phonenumber;//聆听、手表回拨的号码


    boolean isLoactaion = false;
    private int[] res = {R.id.map_menu_btn, R.id.map_photo_btn, R.id.map_monitor_btn, R.id.iv_to_history_track, R.id.iv_to_save_fence, R.id.iv_to_ui_setting};
    private List<Button> buttonList = new ArrayList<Button>();
    int checkedId = R.id.uisettings_2d_btn;
    private LastLoginUtils lastLoginUtils;
    RequestQueue queue;

    private SmartWatch smartWatch;

    private String refresh_type = TYPE_ALL;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public static Location2Fragment getInstance() {
        Location2Fragment fragment = new Location2Fragment();
        return fragment;
    }

    public FrameLayout getInfowindowContener() {
        return (FrameLayout) rootView.findViewById(R.id.infowindow_contener);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(getActivity());
        registerReceiver();
        appSP = new PhoneSPUtils(getActivity());
        lastLoginUtils = new LastLoginUtils(getActivity());
        loadingDialog = new LoadingDialog(getActivity());
        sp = getActivity().getSharedPreferences("NotDisturb", getActivity().MODE_PRIVATE);// 创建对象
        editor = sp.edit();
        imei = sp.getString("IMEI", "");
        phonenumber = sp.getString("Telephone_Number", "");
        present_phone = sp.getString("phone", "");
        nick_name = sp.getString("nick_name", "");//宝贝昵称
        if ("".equals(nick_name)) {
            nick_name = getResources().getString(R.string.function_name_dear);
        }
        sex = sp.getString("sex", "");

        //初始化定位
        mLocationClient = new AMapLocationClient(getActivity());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
//        //启动定位
//        mLocationClient.startLocation();
    }

    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    private void last_location_data() {
        if ("".equals(imei)) {
            T.s(getResources().getString(R.string.unbounded_watch));
            Map<String, String> map = new HashMap<String, String>();
            map.put("longitude", "113.9749712");
            map.put("latitude", "22.5617714");
            map.put("location_content", "广东省 深圳市 南山区 龙珠四路 靠近金谷创业园");
            map.put("watch_time", getStringDate());
            map.put("battery", "0");
            map.put("location_type", "0");
            last_location_data.add(map);
            return;
        }
        sign = Md5Util.stringmd5(imei, service_last_location);
        String url = URL + "service=" + service_last_location + "&imei=" + imei + "&sign=" + sign;
        L.e("sign===" + url);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsondata = jsonObject.getJSONObject("data");
                    JSONArray array = jsondata.getJSONArray("message");
                    for (int i = 0; i < array.length(); i++) {
                        Map<String, String> map = new HashMap<String, String>();
                        JSONObject object = array.getJSONObject(i);
                        String lon = object.getString("location_lon");
                        String lat = object.getString("location_lat");
                        String location_content = object.getString("location_content");
                        String watch_time = object.getString("watch_time");
                        String battery = object.getString("battery");
                        String location_type = object.getString("location_type");

                        map.put("longitude", lon);
                        map.put("latitude", lat);
                        map.put("location_content", location_content);
                        map.put("watch_time", watch_time);
                        map.put("battery", battery);
                        map.put("location_type", location_type);
                        last_location_data.add(map);
                    }
                    addMapMarker();//移动map
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                loadingDialog.dismiss();
                setLocationEnable(true);
                addMapMarker();//移动map
            }
        });
        queue.add(request);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView != null) {

            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_losition, container, false);
        } catch (InflateException e) {

        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view, savedInstanceState);
        if (CMethod.isNet(getActivity())) {
            last_location_data();
        } else {
            T.s(getResources().getString(R.string.no_network));
        }
        setListener();
        infoWindow.hide();
        //setLocationEnable(false);
//        if (HomeSecActivity.refreshLocation) {
        setLocationEnable(true);
//        List<SmartWatch> watche_db = SmartWatchUtils.getWatchList();
//        setLocationView(watche_db, true, true);
//        }
    }

    @Override
    public void refreshWatchList(List<SmartWatch> watches) {
        super.refreshWatchList(watches);
        //setLocationEnable(true);
        if (watches != null) {
            if (refresh_type.equals(TYPE_ALL)) {
                //整体刷新
                setLocationView(watches, true, true);
            } else if (refresh_type.equals(TYPE_ONCLICK)) {
                //点击条目刷新
                setLocationView(watches, false, true);
            }
        } else {
            List<SmartWatch> watche_db = SmartWatchUtils.getWatchList();
            setLocationView(watche_db, true, true);
        }
        getAddressList(watches);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }


    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {

        super.onPause();
        L.e(TAG + "===life==onPause");
        if (mMapView != null)
            mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        L.e(TAG + "===life==onDestroy");
        unregisterReceiver();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void initView(View view, Bundle savedInstanceState) {
        for (int i = 0; i < res.length; i++) {
            Button button = (Button) view.findViewById(res[i]);
            button.setOnClickListener(this);
            buttonList.add(button);
        }
        map_menu_btn = (Button) view.findViewById(R.id.map_menu_btn);//菜单

        location_progress_view = view.findViewById(R.id.location_progress_view);
        imageView_to_save_fence = (Button) view.findViewById(R.id.iv_to_save_fence);//安全围栏
        imageView_to_history_track = (Button) view.findViewById(R.id.iv_to_history_track);//历史轨迹
        map_photo_btn = (Button) view.findViewById(R.id.map_photo_btn);//照片监听
        midTitle = (TextView) view.findViewById(R.id.tv_middle);//title文字
        midTitle.setText(getResources().getString(R.string.function_name_position));
        user_name = (TextView) view.findViewById(R.id.user_name);//底部name显示
        map_bottom_head = (ImageView) view.findViewById(R.id.map_bottom_head);//底部头像显示
        map_call_phone_btn = (Button) view.findViewById(R.id.map_call_phone_btn);//拨打电话监听事件
        map_location_btn = (Button) view.findViewById(R.id.map_location_btn);
        iv_to_ui_setting = (Button) view.findViewById(R.id.iv_to_ui_setting);//地图类型
        map_monitor_btn = (Button) view.findViewById(R.id.map_monitor_btn);//底部监听button
//        iv_left = (ImageView) view.findViewById(R.id.iv_left);
//        iv_left.setImageResource(R.mipmap.location_phone_icon_on);

        //
        location_phone_btn = (ImageView) view.findViewById(R.id.location_phone);
        location_phone_btn.setImageResource(R.mipmap.location_phone_icon_on);
        location_phone_btn.setVisibility(View.GONE);
        //
        iv_left = (ImageView) view.findViewById(R.id.iv_left);
        iv_left.setImageResource(R.mipmap.weather_location_icon);
        location_no_data_tv = (TextView) view.findViewById(R.id.location_no_data_tv);

        //高德地图初始化
        mMapView = (TextureMapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        initMapView();
    }


    public void setLocationEnable(boolean b) {
        isLoactaion = !b;
        if (b) {
            location_progress_view.setVisibility(View.INVISIBLE);
            if (animator != null) {
                animator.cancel();
            }
        } else {
            location_progress_view.setVisibility(View.VISIBLE);
            if (location_progress_view.getWidth() == 0) {
                ViewTreeObserver vto = location_progress_view.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onGlobalLayout() {
                        location_progress_view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        int width = location_progress_view.getWidth();
                        int height = location_progress_view.getHeight();
                        location_progress_view.setPivotX(width / 2);
                        location_progress_view.setPivotY(height / 2);
                        animator = ObjectAnimator.ofFloat(location_progress_view, "rotation", 0, 360).setDuration(2000);
                        animator.setRepeatMode(ObjectAnimator.RESTART);
                        animator.setRepeatCount(ObjectAnimator.INFINITE);
                        animator.start();
                    }
                });
            } else {

                int width = location_progress_view.getWidth();
                int height = location_progress_view.getHeight();

                location_progress_view.setPivotX(width / 2);
                location_progress_view.setPivotY(height / 2);
                animator = ObjectAnimator.ofFloat(location_progress_view, "rotation", 0, 360).setDuration(2000);
                animator.setRepeatMode(ObjectAnimator.RESTART);
                animator.setRepeatCount(ObjectAnimator.INFINITE);
                animator.start();
            }

        }
        map_location_btn.setEnabled(b);
    }


    /**
     * 初始化AMap对象
     */
    private void initMapView() {
        if (mMap == null) {
            mMap = mMapView.getMap();
            try {
                UiSettings uiSettings = mMap.getUiSettings();
                uiSettings.setZoomControlsEnabled(false);
                uiSettings.setScaleControlsEnabled(true);
                infoWindow = new InfoWindow(getContext(), this);
                infoWindow.init();


            } catch (Exception e) {
            }
        }
    }

    private void setListener() {
        //菜单按钮
        map_menu_btn.setOnClickListener(this);
        //历史轨迹监听事件
        imageView_to_history_track.setOnClickListener(this);
        //安全围栏监听事件
        imageView_to_save_fence.setOnClickListener(this);
        //拨打电话监听事件
        map_call_phone_btn.setOnClickListener(this);
        //监听功能监听事件
        map_monitor_btn.setOnClickListener(this);
        //照片监听
        map_photo_btn.setOnClickListener(this);
//        //导航功能监听事件
//        map_navigation_btn.setOnClickListener(this);//
//          定位监听事件
        map_location_btn.setOnClickListener(this);
        //手机定位
        location_phone_btn.setOnClickListener(this);
/*        iv_left.setOnClickListener(this);//是否显示手机定位

        iv_right.setOnClickListener(this);//天气预报*/


        iv_left.setOnClickListener(this);//天气预报


        iv_to_ui_setting.setOnClickListener(this);//地图类型
        //逆地理监听的注册（获取地理位置）
//        geoCoderSearch = new GeocodeSearch(getActivity());
//        geoCoderSearch.setOnGeocodeSearchListener(this);
        //高德地图marker点击监听事件
        mMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                infoWindow.show(locationUserInfoEntityLists.get(present_position), mMap, mMapView);//标记信息弹出框
//                try {
//                    if (marker.getPosition().latitude != 0 && !getMarkerWatchID(marker.getSnippet()).equals(CUT_STR)) {
//                        //点击刷新书籍并从新撒点
//                        present_center = marker.getPosition();
//                        present_order = 16;
//                        present_position = getMarkerPosition(marker.getSnippet());
//                        L.e("Integer.parseInt(marker.getSnippet())==" + getMarkerPosition(marker.getSnippet()));
////                        marker.setIcon(getMarkerView(locationUserInfoEntityLists.get(present_position).getHeadView(),true));
//                        refreshWatchList(TYPE_ONCLICK);
//                    }
//                } catch (Exception e) {
//                    L.e(e.toString());
//                }
                return true;
            }
        });

        mMap.setOnCameraChangeListener(this);
        mMap.setOnMapClickListener(this);
    }

    private class locationTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {
            try {
                Thread.sleep(60*1000);
                last_location_data();
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);

            refreshWatchList(TYPE_ALL);
        }
    }


    private void refreshWatchList(final String type) {
        refresh_type = type;
        Intent intent = new Intent();
        intent.setAction(BroadcastConstants.WATCH_LIST_REFRESH_RECEIVER);
        getActivity().sendBroadcast(intent);
        MobclickAgent.onEvent(getActivity(), UMeventId.UMENG_NUMBER_OF_POSITIONING_MODULE);
    }


    private void setLocationView(List<SmartWatch> watches, boolean isCountOrder, boolean isMove) {
        editLocationUserInfoList(watches);
        if (isCountOrder)
            countMapOrder();
        if (isMove)
            moveMap();
//        addMapMarker();

    }


    private void getAddressList(List<SmartWatch> watches) {
        if (watches == null)
            return;
        listEntities = LocationFactory.getInstance().getCityEntityList(watches);
        for (CityListEntity citylistentity : listEntities) {
            getAddress(citylistentity);
        }

    }


    boolean isNoWatchLatLonData = true;

    /**
     * 通过所有值得经纬度计算缩放比例
     */

    private void countMapOrder() {
        if (locationUserInfoEntityLists == null)
            return;


        for (LocationUserInfoEntity locationUserInfoEntity : locationUserInfoEntityLists) {
            if (locationUserInfoEntity.getLatLng().latitude != 0 && locationUserInfoEntity.getLatLng().longitude != 0) {
                isNoWatchLatLonData = false;
            }
        }
        if (isNoWatchLatLonData) {
            location_no_data_tv.setVisibility(View.VISIBLE);
        } else {
            location_no_data_tv.setVisibility(View.GONE);
        }

        List<LatLng> latlngs = LocationFactory.getInstance().getLatLngList(locationUserInfoEntityLists);

        //计算最大距离
        double all_lng = 0, all_lat = 0;
        double max_destance = 0;
        int num = 0;
        for (int i = 0; i < latlngs.size(); i++) {
            LatLng i1 = latlngs.get(i);
            int j = i;
            if (j + 1 == latlngs.size()) {
                j = -1;
            }
            LatLng i2 = latlngs.get(j + 1);
            if (i1.latitude != 0 && i2.latitude != 0) {
                double distance = AMapUtils.calculateLineDistance(i1, i2);
                if (distance >= max_destance) {
                    max_destance = distance;
                }
                all_lng = all_lng + latlngs.get(i).longitude;
                all_lat = all_lat + latlngs.get(i).latitude;
                num = num + 1;
            }
        }
        //计算缩放等级
        for (int i = 0; i < SCALES.length; i++) {
            if (max_destance != 0 && max_destance / 2.5 > SCALES[i]) {
                present_order = MAX_LEVEL - i;
            }
        }

        LatLng latLng_child = locationUserInfoEntityLists.get(present_position).getLatLng();
        if (latLng_child.latitude == 0 || latLng_child.longitude == 0) {
            if (num == 0 && latlngs != null && latlngs.size() != 0) {
                present_center = CMethod.getLatAndLng();
            } else if (all_lat != 0 && all_lng != 0) {
                present_center = new LatLng(all_lat / num, all_lng / num);
            }
        } else {
            present_center = latLng_child;
        }


    }


    private void moveMap() {

        if (present_center != null && present_center.latitude != 0 && present_center.longitude != 0) {
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                    present_center, present_order, 0, 0));
            mMap.animateCamera(update);
        } else {
            LatLng latLng = new LatLng(34.259383, 108.947038);

            CameraUpdate update = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                    latLng, 4, 0, 0));
            mMap.animateCamera(update);

        }
    }

    LatLng latLng;
    double dLong2, dLat2;
    private String last_location, last_time, battery, location_type;

    /**
     * 在地图上撒点
     */
    private void addMapMarker() {
        //new locationTask().execute();
        infoWindow.hide();
        mMap.clear();
        for (int i = 0; i < last_location_data.size(); i++) {
            String longitude = last_location_data.get(i).get("longitude").toString();
            String latitude = last_location_data.get(i).get("latitude").toString();
            if (null == longitude || "".equals(longitude)){
                longitude = "0";
            }
            if (null == latitude || "".equals(latitude)){
                latitude = "0";
            }
            dLong2 = Double.parseDouble(longitude);
            dLat2 = Double.parseDouble(latitude);
            last_location = last_location_data.get(i).get("location_content").toString();
            last_time = last_location_data.get(i).get("watch_time").toString();
            battery = last_location_data.get(i).get("battery").toString();
            location_type = last_location_data.get(i).get("location_type").toString();
            // Log.e("dlong", dLong2 + " " + dLat2);
        }
        if (dLong2 != 0 && dLat2 != 0) {
            latLng = new LatLng(dLat2, dLong2);
        } else {
            latLng = new LatLng(22.5617714, 113.9749712);
        }
        MarkerOptions options2 = new MarkerOptions();
        if (sex.equals("0")) {
            options2.icon(BitmapDescriptorFactory.fromResource(R.mipmap.head_image_girl_location));
        } else {
            options2.icon(BitmapDescriptorFactory.fromResource(R.mipmap.head_image_boy_location));
        }
        options2.position(latLng)
                .anchor(0.5f, 0.5f)
                .draggable(true)
                .setFlat(true);
        mMap.addMarker(options2);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));// 移动地图
//        new Handler().postDelayed((new Runnable() {
//            @Override
//            public void run() {
//                setLocationEnable(true);
//            }
//        }), 2000);

//        locationUserInfoEntityLists = new ArrayList<LocationUserInfoEntity>();
        LocationUserInfoEntity locat = new LocationUserInfoEntity();
        locat.setAddress("".equals(last_location)?"广东省 深圳市 南山区 龙珠四路 靠近金谷创业园":last_location);
        Long time = System.currentTimeMillis();
        locat.setBaseTime("".equals(last_time)?time.toString():last_time);
        locat.setNickName(nick_name);
        locat.setElectricity(("".equals(battery)||battery == null)?0:Integer.parseInt(battery));
        locat.setBaseType(("".equals(location_type)||location_type == null)?0:Integer.parseInt(location_type));
        locat.setLatLng(latLng);
        locat.setSex("0".equals(sex)?0:1);
        locationUserInfoEntityLists.add(locat);

        infoWindow.show(locationUserInfoEntityLists.get(present_position), mMap, mMapView);
    }

    private double latitude, longitude;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            L.e("amplocation1" + "amplocation1");
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                //可在其中解析amapLocation获取相应内容。
                    latitude = amapLocation.getLatitude();//获取纬度
                    longitude = amapLocation.getLongitude();//获取经度
                    L.e("amplocation==" + latitude + ":" + longitude);
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

    private void addPhoneMarker() {//手机图标定位
        String relation = sp.getString("relation", "");
        if ("".equals(relation)) {
            relation = "其他";
        }
        //启动定位
//        mLocationClient.startLocation();
//        if (smartWatch == null)
//            return;
        MarkerOptions options = new MarkerOptions();
//        options.icon(getMarkerView(ParentHeadViewLocationUtils.getImage(smartWatch.getRelation())));
//        options.position(CMethod.getLatAndLng());
        options.icon(getMarkerView(ParentHeadViewLocationUtils.getImage(relation)));
        if (latitude == 0 || longitude == 0) {
            options.position(new LatLng(22.561776 ,113.9749672));
        } else {
            options.position(new LatLng(latitude, longitude));
        }
        phoneMarker = mMap.addMarker(options);
        isShowTitleLeft();
    }

    private BitmapDescriptor getMarkerView(int headView) {
        layout_map_marker = LayoutInflater.from(getActivity()).inflate(R.layout.layout_map_marker, null);
        ImageView imageView = (ImageView) layout_map_marker.findViewById(R.id.image_head);
        imageView.setImageResource(headView);
        return BitmapDescriptorFactory.fromBitmap(CMethod.getViewBitmap(layout_map_marker));

    }


    private String getMarkerId(String string) {
        try {
            String strings[] = string.split(CUT_STR);
            return strings[1];

        } catch (Exception e) {
            return "";
        }

    }

    private int getMarkerPosition(String string) {
        try {
            String strings[] = string.split(CUT_STR);
            int position = Integer.parseInt(strings[0]);
            return position;
        } catch (Exception e) {
            return 0;
        }
    }

    private String getMarkerWatchID(String string) {
        try {
            String strings[] = string.split(CUT_STR);
            String watchID = strings[1];
            return watchID;
        } catch (Exception e) {
            return CUT_STR;
        }
    }

    /**
     * 将watchlist编辑为定位的list数据，添加了家长的经纬度数据和头像书籍
     *
     * @param watchs
     * @return
     */
    private void editLocationUserInfoList(final List<SmartWatch> watchs) {
        if (watchs == null || watchs.size() <= 0)
            return;
        if (present_position >= watchs.size()) {
            present_position = 0;
        }
        smartWatch = watchs.get(present_position);
        locationUserInfoEntityLists = LocationFactory.getInstance().getLocationList(watchs);
    }

    private void callPhoneState() {//聆听
        //String present_phone = locationUserInfoEntityLists.get(present_position).getPhone();
        if (!"".equals(present_phone)) {
            Intent phoneIntent = new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:" + present_phone));
            startActivity(phoneIntent);
        } else {
            T.s(getResources().getString(R.string.complete_information));
        }
    }


    public void getAddress(final LocationUserInfoEntity location, final TextView address_tv) {
        AMapGeocodeHelper aMapGeocodeModel = new AMapGeocodeHelper(new GeocodeEventHandler() {
            @Override
            public void onRegeocodeAMap(int code, RegeocodeResult regeocodeResult) {
                if (code == 0) {
                    String formatAddress = regeocodeResult.getRegeocodeAddress().getFormatAddress();
                    address_tv.setText(formatAddress);

                }
            }

            @Override
            public void onGeocodeAMap(int code, GeocodeResult geocodeResult) {
            }
        });
        aMapGeocodeModel.regeocodeSearch(location.getLatLng().latitude, location.getLatLng().longitude);
    }

    public void getAddress(final CityListEntity location) {
        AMapGeocodeHelper aMapGeocodeModel = new AMapGeocodeHelper(new GeocodeEventHandler() {
            @Override
            public void onRegeocodeAMap(int code, RegeocodeResult regeocodeResult) {
                if (code == 0) {
                    String city = regeocodeResult.getRegeocodeAddress().getCity();
                    String province = regeocodeResult.getRegeocodeAddress().getProvince();
                    city = city.equals("") ? province : city;
                    location.setCityName(city);
                }
            }

            @Override
            public void onGeocodeAMap(int code, GeocodeResult geocodeResult) {
            }
        });
        aMapGeocodeModel.regeocodeSearch(location.getLatLng().latitude, location.getLatLng().longitude);
    }


    /**
     * 高德地图获取经纬度广播
     */
    private BroadcastReceiver gaodeRefreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (BaseApplication.getHomeActivity() != null && BaseApplication.getHomeActivity().tabAdapter != null) {
                int item = BaseApplication.getHomeActivity().tabAdapter.getCurrentTab();
                L.e("item==" + item);
                if (item != 0) {
                    return;
                }
                String action = intent.getAction();
                //接收指令广播
                if (action.equals(BroadcastConstants.POSITION_REFRESH_RECEIVER)) {

                    if (!isLoactaion) {
                        if (phoneMarker != null) {
                            phoneMarker.remove();
                        }
                        addPhoneMarker();
                    }
                }
            }

        }
    };


    /**
     * 注册广播(高德地图获取经纬度)
     */
    private void registerReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastConstants.POSITION_REFRESH_RECEIVER);
        getActivity().registerReceiver(gaodeRefreshReceiver, intentFilter);
    }

    /**
     * 注销广播
     */
    private void unregisterReceiver() {
        getActivity().unregisterReceiver(gaodeRefreshReceiver);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        infoWindow.update(mMap, mMapView);
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        infoWindow.hide();
    }


    private void isShowTitleLeft() {
        boolean isShowLocation = appSP.getBoolean(SPKeyConstants.IS_SHOW_PHONE_LOCATION, false);
        location_phone_btn.setVisibility(View.VISIBLE);
        if (phoneMarker == null || phoneMarker.getPosition().latitude == 0) {
            location_phone_btn.setVisibility(View.GONE);
        } else {
            location_phone_btn.setVisibility(View.VISIBLE);
        }
        if (isShowLocation) {
            location_phone_btn.setImageResource(R.mipmap.location_phone_icon_on);
            phoneMarker.setVisible(true);

        } else {
            location_phone_btn.setImageResource(R.mipmap.location_phone_icon_off);
            phoneMarker.setVisible(false);

        }


    }

    private List<Map<String, String>> last_location_data = new ArrayList<Map<String, String>>();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            //Toast.makeText(getActivity(), (String)msg.obj, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onClick(View v) {
        {
            if (CMethod.isFastDoubleClick()) {
                return;
            }

            switch (v.getId()) {

                case R.id.map_menu_btn:
                    //打开菜单动画
                    if (!flag) {
                        startAnimation();
                        map_menu_btn.setBackgroundResource(R.mipmap.location_menu_on);
                    } else {
                        endAnimation();
                        map_menu_btn.setBackgroundResource(R.mipmap.location_menu_off);
                    }
                    break;
                case R.id.location_phone://是否显示手机位置

                    if (!isLoactaion) {
                        if (phoneMarker != null) {
                            if (appSP.getBoolean(SPKeyConstants.IS_SHOW_PHONE_LOCATION, false)) {
                                appSP.save(SPKeyConstants.IS_SHOW_PHONE_LOCATION, false);
                                location_phone_btn.setImageResource(R.mipmap.location_phone_icon_off);
                                phoneMarker.setVisible(false);
                                T.s(getResources().getString(R.string.location_closed));
                                mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
                            } else {
                                appSP.save(SPKeyConstants.IS_SHOW_PHONE_LOCATION, true);
                                location_phone_btn.setImageResource(R.mipmap.location_phone_icon_on);
                                phoneMarker.setVisible(true);
                                T.s(getResources().getString(R.string.location_open));
                                //启动定位
                                mLocationClient.startLocation();
                            }
                        }
                    } else {
                        T.s(getResources().getString(R.string.location_loading));
                    }

                    break;
                case R.id.iv_left://天气
                    listEntities.clear();
                    String[] strings = locationUserInfoEntityLists.get(0).getAddress().split(" ");
                    CityListEntity cityListEntity = new CityListEntity();
                    cityListEntity.setCityName(strings[1]);
                    cityListEntity.setChildName(nick_name);
                    cityListEntity.setLatLng(locationUserInfoEntityLists.get(0).getLatLng());
                    listEntities.add(cityListEntity);
                    if (listEntities == null || listEntities.size() == 0) {
                        return;
                    }
                    if (!CMethod.isNetWorkEnable(getActivity())) {
                        T.s(getActivity().getString(R.string.net_poor));
                        return;
                    }
                    L.e("isNoWatchLatLonData==" + isNoWatchLatLonData);
                    if (isNoWatchLatLonData) {
                        Intent intent_weather = new Intent(getActivity(), WeatherSearchActivity.class);
                        Bundle mBundle_weather = new Bundle();
                        mBundle_weather.putParcelableArrayList(IntentAction.KEY_WEATHER_DATA, listEntities);//传递宝贝信息
                        intent_weather.putExtras(mBundle_weather);
                        startActivity(intent_weather);
                    } else {
                        T.s(getResources().getString(R.string.location_unable));
                    }


                    break;

                case R.id.map_call_phone_btn://拨打电话
                    //if (locationUserInfoEntityLists != null && present_position < locationUserInfoEntityLists.size()) {
                        callPhoneState();
                    //}
                    break;

                case R.id.map_monitor_btn://电话监听
                    if (!CMethod.isNet(getActivity())) {
                        T.s(getResources().getString(R.string.no_network));
                        return;
                    }
                    if (!CMethod.isFastDoubleClick()) {
                        return;
                    }

                    monitorWatch(smartWatch);
                    break;
                case R.id.map_photo_btn://照片监听
                    if (!CMethod.isNet(getActivity())) {
                        T.s(getResources().getString(R.string.no_network));
                        return;
                    }
                    if (!CMethod.isFastDoubleClick()) {
                        return;
                    }

                    photoWatch(smartWatch);
                    break;
                case R.id.map_location_btn://定位
                    if (!CMethod.isNet(getActivity())) {
                        T.s(getResources().getString(R.string.no_network));
                        return;
                    }
//                    setLocationEnable(false);
                    positionToWeb(smartWatch);//请求定位

                    infoWindow.hide();
//                    last_location_data();//定位
                    new locationTask().execute();
                    addMapMarker();
                    break;

                case R.id.iv_to_ui_setting://选择地图类型
                    if (!CMethod.isNet(getActivity())) {
                        T.s(getResources().getString(R.string.no_network));
                        return;
                    }

                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_to_ui_setting.getLayoutParams();

                    new MapUiSettingDialog(getActivity(), checkedId, new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                            //3.根据选中的id，保存修改之后的设置
                            checkedId = R.id.uisettings_2d_btn;
                            String maptype = MapUtil.MAP_UISETTINGS_2D;
                            switch (i) {
                                case R.id.uisettings_3d_btn:
                                    checkedId = R.id.uisettings_3d_btn;
                                    maptype = MapUtil.MAP_UISETTINGS_3D;
                                    break;
                                case R.id.uisettings_satellite_btn:
                                    checkedId = R.id.uisettings_satellite_btn;
                                    maptype = MapUtil.MAP_UISETTINGS_SATELLITE;
                                    break;
                            }
                            MapUtil.changeMapType(mMap, maptype);
                        }
                    }).show();


                    break;


                case R.id.iv_to_save_fence://安全围栏

                    if (!CMethod.isNet(getActivity())) {
                        T.s(getResources().getString(R.string.no_network));
                        return;
                    }

                    if (locationUserInfoEntityLists != null && present_position < locationUserInfoEntityLists.size()) {
                        LocationUserInfoEntity locationUserInfoEntity = locationUserInfoEntityLists.get(present_position);
                        if (locationUserInfoEntity.getLatLng().latitude != 0 || locationUserInfoEntity.getLatLng().longitude != 0) {
                            Intent intent = new Intent(getActivity(), SafeFenceListActivity.class);
                            Bundle mBundle = new Bundle();
                            mBundle.putParcelable(IntentAction.KEY_SAFE_FRENCE, locationUserInfoEntity);//传递宝贝信息
                            intent.putExtras(mBundle);
                            startActivity(intent);
                        } else {
                            T.s(getResources().getString(R.string.repeat_location));
                        }

                    }
                    break;
                case R.id.iv_to_history_track://历史轨迹
                    if (!CMethod.isNet(getActivity())) {
                        T.s(getResources().getString(R.string.no_network));
                        return;
                    }

                    if (locationUserInfoEntityLists != null && present_position < locationUserInfoEntityLists.size()) {
                        Intent intent = new Intent(getActivity(), HistoryTrackActivity.class);
//                        intent.putExtra("history_track_watchId", locationUserInfoEntityLists.get(present_position).getWatchID());
                        intent.putExtra("history_track_baby_sex", locationUserInfoEntityLists.get(present_position).getSex());
                        intent.putExtra("service_day_location", service_day_location);
                        intent.putExtra("imei", imei);
//                        intent.putExtra("history_track_location_lat", CMethod.getLatAndLng().latitude);
//                        intent.putExtra("history_track_location_lon", CMethod.getLatAndLng().longitude);
                        intent.putExtra("history_track_location_lat", latitude);
                        intent.putExtra("history_track_location_lon", longitude);
                        startActivity(intent);
                    }
                    break;


            }
        }

    }

    private void startAnimation() {
        flag = true;

        float step = getActivity().getResources().getDimension(R.dimen.pop_menu_step);
        for (int i = 0; i < res.length; i++) {
            L.e(-i * 115f * 2 + "");
            ObjectAnimator animator = ObjectAnimator.ofFloat(buttonList.get(i), "translationY", 0f, -i * step * 2);
            animator.setDuration(i * 100);
            animator.setInterpolator(new LinearInterpolator());
            animator.start();
        }
    }

    private void endAnimation() {
        flag = false;
        float step = getActivity().getResources().getDimension(R.dimen.pop_menu_step);
        for (int i = 0; i < res.length; i++) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(buttonList.get(i), "translationY", -i * step * 2, 0f);
            animator.setDuration(i * 100);
            animator.start();
        }
    }


    public void startNavigation() {
        if (!CMethod.isNet(getActivity())) {
            T.s(getResources().getString(R.string.no_network));
            return;
        }
        if (locationUserInfoEntityLists != null && present_position < locationUserInfoEntityLists.size()) {
            openMap();
        }
    }

    private void openMap() {
        LocationUserInfoEntity entity = locationUserInfoEntityLists.get(present_position);

        AppInfo info = AppInfo.getInstace();


        try {
            if (CMethod.isInstallByread("com.autonavi.minimap")) {
                if (CMethod.isEmptyPoi(info.getLatitude()) || CMethod.isEmptyPoi(info.getLongitude()) || entity.getLatLng() == null) {
                    T.s(getResources().getString(R.string.relay_location));
                    return;
                }

                Intent i = new Intent();
                i.addCategory("android.intent.category.DEFAULT");
//                                                                                                                  116.454475, 39.934441
//                i.setData(Uri.parse("androidamap://navi?sourceApplication=com.jajale.watch&poiname=三里屯太古里&lat=39.934441&lon=116.454475&dev=0&style=0"));
                String request = "androidamap://route?sourceApplication=com.jajale.watch&slat=#SA#&slon=#SO#&sname=我&dlat=#DA#&dlon=#DO#&dname=#DN#&dev=0&m=0&t=1";
                request = request.replace("#SA#", info.getLatitude() + "").replace("#SO#", info.getLongitude() + "").replace("#DA#", entity.getLatLng().latitude + "").replace("#DO#", entity.getLatLng().longitude + "").replace("#DN#", entity.getNickName());
                i.setData(Uri.parse(request));
                i.setPackage("com.autonavi.minimap");
                startActivity(i);
            } else if (CMethod.isInstallByread("com.baidu.BaiduMap")) {

                if (CMethod.isEmptyPoi(info.getLatitude()) || CMethod.isEmptyPoi(info.getLongitude()) || entity.getLatLng() == null) {
                    T.s(getResources().getString(R.string.relay_location));
                    return;
                }

                BDPosition posi = CMethod.bd_encrypt(info.getLatitude(), info.getLongitude());
                BDPosition pos_c = CMethod.bd_encrypt(entity.getLatLng().latitude, entity.getLatLng().longitude);
//                String req = "intent://map/direction?origin=latlng:#OA#,#OL#|name:我&destination=#DES#&src=com.bjjajale&mode=driving®ion=#CITY#&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
                String req = "intent://map/direction?origin=latlng:#OA#,#OL#|name:我&destination=latlng:#DA#,#DL#|name:#DN#&src=com.bjjajale&mode=driving®ion=#CITY#&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end";
                req = req.replace("#OA#", posi.getLat() + "").replace("#OL#", posi.getLon() + "").replace("#CITY#", info.getCity()).replace("#DA#", pos_c.getLat() + "").replace("#DL#", pos_c.getLon() + "").replace("#DN#", entity.getNickName());
                Intent intent = Intent.getIntent(req);
                startActivity(intent);
            } else {

                T.s(getResources().getString(R.string.install_gold_or_baidu));
            }
        } catch (Exception e) {
            e.printStackTrace();
            T.s(getResources().getString(R.string.install_gold_or_baidu));
        }

    }


    /**
     * 电话监听
     */
    private void monitorWatch(SmartWatch smartWatch) {
        sign = Md5Util.stringmd5(imei, phonenumber, service_monitor);
        loadingDialog.show(getResources().getString(R.string.send_instruct), AppConstants.MaxIndex);
        String path = URL + "service=" + service_monitor + "&imei=" + imei + "&phonenumber=" + phonenumber + "&sign=" + sign;
        L.e("sign===" + path);
        StringRequest request = new StringRequest(path, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loadingDialog.dismissAndStopTimer();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsondata = jsonObject.getJSONObject("data");
                    int code = jsondata.getInt("code");
                    if (code == 0) {
                        T.s(getResources().getString(R.string.send_code_success));
                    } else if (code == 1) {
                        T.s(getResources().getString(R.string.not_online));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismissAndStopTimer();
            }
        });
        queue.add(request);

    }

    /**
     * 照片监听
     */
    private void photoWatch(SmartWatch smartWatch) {
        sign = Md5Util.stringmd5(imei, service_remote_photo);
        loadingDialog.show(getResources().getString(R.string.send_instruct), AppConstants.MaxIndex);
        String path = URL + "service=" + service_remote_photo + "&imei=" + imei + "&sign=" + sign;
        L.e("sign===" + path);
        StringRequest request = new StringRequest(path, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loadingDialog.dismissAndStopTimer();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsondata = jsonObject.getJSONObject("data");
                    int code = jsondata.getInt("code");
                    if (code == 0) {
                        T.s(getResources().getString(R.string.send_code_success));
                    } else if (code == 1) {
                        T.s(getResources().getString(R.string.not_online));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismissAndStopTimer();
            }
        });
        queue.add(request);

    }


    /**
     * 定位
     */
    private void positionToWeb(SmartWatch smartWatch) {
        loadingDialog.show(getResources().getString(R.string.send_instruct), AppConstants.MaxIndex);
        if ("".equals(imei)) {
            loadingDialog.dismiss();
            setLocationEnable(true);
        }
        sign = Md5Util.stringmd5(imei, service_req_location);
        String path = URL + "service=" + service_req_location + "&imei=" + imei + "&sign=" + sign;
        L.e("sign===" + path);
        StringRequest request = new StringRequest(path, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                new Handler().postDelayed((new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismissAndStopTimer();
                    }
                }), 1000);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsondata = jsonObject.getJSONObject("data");
                    int code = jsondata.getInt("code");
                    if (code == 0) {

                    } else if (code == 1) {
                        T.s(getResources().getString(R.string.not_online));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismiss();
                setLocationEnable(true);
            }
        });
        queue.add(request);

    }


}
