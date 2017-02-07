package com.jajale.watch.activity;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.VisibleRegion;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseActivity;
import com.jajale.watch.R;
import com.jajale.watch.cviews.HistoryTrackCalendarPopupWindow;
import com.jajale.watch.entity.HistoryTrackData;
import com.jajale.watch.entityno.UMeventId;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.CalendarUtil;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.Md5Util;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 历史轨迹
 * <p/>
 * Created by lilonghui on 2016/1/18.
 * Email:lilonghui@bjjajale.com
 */
public class HistoryTrackActivity extends BaseActivity implements OnClickListener, AdapterView.OnItemClickListener, GeocodeSearch.OnGeocodeSearchListener {
    @InjectView(R.id.iv_left)
    ImageView ivLeft;
    @InjectView(R.id.tv_middle)
    TextView tvMiddle;
    @InjectView(R.id.history_track_btn_front)
    Button historyTrackBtnFront;
    @InjectView(R.id.history_track_tv_day)
    TextView historyTrackTvDay;
    @InjectView(R.id.history_track_iv_arrow)
    ImageView historyTrackIvArrow;
    @InjectView(R.id.history_track_ll_middle)
    LinearLayout historyTrackLlMiddle;
    @InjectView(R.id.history_track_btn_behind)
    Button historyTrackBtnBehind;
    @InjectView(R.id.history_track_tv_time)
    TextView historyTrackTvTime;
    @InjectView(R.id.history_track_tv_address)
    TextView historyTrackTvAddress;
    @InjectView(R.id.history_track_btn_play)
    Button historyTrackBtnPlay;
    @InjectView(R.id.tv_no_history_data)
    TextView tv_no_history_data;
    private int present_position;
    private int max_position;
    private int min_position = 0;
    //地图最大缩放级别
    private static final int MAX_LEVEL = 19;
    //各级比例尺分母值数组
    private static final int[] SCALES = {10, 25, 50, 100, 200, 500, 1000, 2000, 5000, 10000
            , 20000, 30000, 50000, 100000, 200000, 500000, 1000000};
    private int present_order = 16;
    private LatLng present_center;
    private MapView mapView;
    private AMap aMap;
    private float lineWidth = 8.0F;
    private HistoryTrackCalendarPopupWindow calendarPopupWindow;
    private List<CalendarUtil.CalendarData> calendarLists;

    private Polyline mPolyline;
    //    private ArrayList<LatLng> latlngList;
    private Marker mMoveMarker;
    // 通过设置间隔时间和距离可以控制速度和图标移动的距离
    private static int TIME_INTERVAL = 280;
    private static final double DISTANCE = 0.0001;
    private Marker dotmarker;
    private MoveBabyMarkerTask moveBabyMarker;

    private GeocodeSearch geocoderSearch;
    private String watchID;
    private LoadingDialog loading;

    private List<HistoryTrackData.TrajectoryListEntity> mTrajectoryListEntities;
    private int baby_sex;

    private LatLng init_center;

    RequestQueue queue;
    private String URL = "http://lib.huayinghealth.com/lib-x/?";
    private String service_day_location, imei;//获取某一天时间内的所有定位坐标,imei


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_track);
        queue = Volley.newRequestQueue(getApplicationContext());
        ButterKnife.inject(this);
        MobclickAgent.onEvent(HistoryTrackActivity.this, UMeventId.UMENG_HISTORY_TRACK_INTO);
        loading = new LoadingDialog(this);
//        watchID = getIntent().getStringExtra("history_track_watchId");
        service_day_location = getIntent().getStringExtra("service_day_location");
        imei = getIntent().getStringExtra("imei");
        baby_sex = getIntent().getIntExtra("history_track_baby_sex", 1);

        double location_lat = getIntent().getDoubleExtra("history_track_location_lat", 0);
        double location_lon = getIntent().getDoubleExtra("history_track_location_lon", 0);
        init_center = new LatLng(location_lat, location_lon);

        initCalendarPopupWindow();

//        getHistoryTrackListToDrowLine();
        tvMiddle.setText(getResources().getString(R.string.history_track_title));
        ivLeft.setImageResource(R.drawable.title_goback_selector);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState); // 此方法必须重写
        initMapView();//初始化地图

        bindOnClickListener();

//        drawline();
        loading.show();
        getHistoryTrackListToDrowLine();
    }


    /**
     * 初始化AMap对象
     */
    private void initMapView() {

        if (aMap == null) {
            aMap = mapView.getMap();
        }
    }

    private void bindOnClickListener() {
        //逆地理监听的注册（获取地理位置）
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);

        ivLeft.setOnClickListener(this);
        historyTrackBtnFront.setOnClickListener(this);//前一天
        historyTrackBtnBehind.setOnClickListener(this);//后一天
        historyTrackBtnPlay.setOnClickListener(this);//播放轨迹
        historyTrackLlMiddle.setOnClickListener(this);//点击中间弹出日历
        //日历取消监听
        calendarPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //弹出框取消监听
                historyTrackIvArrow.setImageResource(R.mipmap.history_track_arrow_down);
            }
        });
        //高德地图marker点击监听事件
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                try {
                    if (marker.getPosition().latitude != 0 && mMoveMarker != null && mMoveMarker.getPosition().latitude != marker.getPosition().latitude && mMoveMarker.getPosition().longitude != marker.getPosition().longitude) {
                        for (int i = 0; i < aMap.getMapScreenMarkers().size(); i++) {
                            aMap.getMapScreenMarkers().get(i).setVisible(true);
                        }
                        marker.setVisible(false);
                        mMoveMarker.setPosition(marker.getPosition());
                        historyTrackTvTime.setText(marker.getSnippet());
                        getAddress(marker.getPosition());
                    }
                } catch (Exception e) {
                    L.e(e.toString());
                }
                return true;
            }
        });
        if (init_center.latitude != 0 && init_center.longitude != 0) {
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                    init_center, 10, 0, 0));
            aMap.animateCamera(update);
        }

    }

    String dataTime;
    private void getHistoryTrackListToDrowLine() {
        if (!CMethod.isNet(this)) {
            historyTrackTvAddress.setText(getResources().getString(R.string.no_network));
            historyTrackBtnPlay.setBackgroundResource(R.drawable.shape_button_gray_normal);
            historyTrackBtnPlay.setEnabled(false);
            loading.dismiss();
            return;
        }
        dataTime = calendarLists.get(present_position).getDateTime();
        String sign = Md5Util.stringsortmd5(service_day_location, imei, dataTime);
        String url = URL + "service=" + service_day_location + "&imei=" + imei + "&date=" + dataTime + "&sign=" + sign;
        L.e("sign===" + url);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                loading.dismiss();
                L.e("response" + response);
//                Gson gson = new Gson();
//                HistoryTrackData fromJson = gson.fromJson(response, HistoryTrackData.class);
//                if (fromJson == null)
//                    return;
//                List<HistoryTrackData.TrajectoryListEntity> trajectoryListEntities = fromJson.getTrajectoryList();
                List<HistoryTrackData.TrajectoryListEntity> trajectoryListEntities = new ArrayList<HistoryTrackData.TrajectoryListEntity>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsondata = jsonObject.getJSONObject("data");
                    JSONArray array = jsondata.getJSONArray("message");
                    for (int i = 0; i < array.length(); i++) {
                        HistoryTrackData.TrajectoryListEntity data = new HistoryTrackData.TrajectoryListEntity();
                        JSONObject object = array.getJSONObject(i);

                        if ("0".equals(object.getString("location_type"))) {
                            String lon = object.getString("location_lon");
                            lon = lon.substring(1,lon.length());
                            L.e("sign==" + lon);
                            data.setLat(object.getString("location_lat"));
                            data.setLon(lon);
                            data.setSysTime(dataTime);
                            trajectoryListEntities.add(data);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                if (trajectoryListEntities == null)
//                    return;
                if (aMap != null)
                    aMap.clear();
                loading.dismiss();
                tv_no_history_data.setVisibility(View.GONE);

                mTrajectoryListEntities = new ArrayList<HistoryTrackData.TrajectoryListEntity>();
                historyTrackBtnPlay.setBackgroundResource(R.drawable.button_common_on_selector_green);
                historyTrackBtnPlay.setEnabled(true);
                if (trajectoryListEntities.size() == 0) {
                    tv_no_history_data.setVisibility(View.VISIBLE);
                    if (init_center.latitude != 0 && init_center.longitude != 0) {
                        CameraUpdate update = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                                init_center, 10, 0, 0));
                        aMap.animateCamera(update);
                    }
                    noDataView();
                } else if (trajectoryListEntities.size() == 1) {
                    mTrajectoryListEntities = trajectoryListEntities;
                    historyTrackTvTime.setText(mTrajectoryListEntities.get(0).getSysTime());
                    getAddress(mTrajectoryListEntities.get(0).getLatLng());
                    addDotMarker();
                    noDataView();

                } else {
                    for (int i = 0; i < trajectoryListEntities.size(); i++) {
                        int j = i;
                        if (j + 1 == trajectoryListEntities.size()) {
                            j = -1;
                        }
                        double lat = parseDouble(trajectoryListEntities.get(i).getLat());
                        double lat2 = parseDouble(trajectoryListEntities.get(j + 1).getLat());
                        double lon = parseDouble(trajectoryListEntities.get(i).getLon());
                        double lon2 = parseDouble(trajectoryListEntities.get(j + 1).getLon());
                        if (lon != lon2 && lat != lat2) {
                            HistoryTrackData.TrajectoryListEntity data = new HistoryTrackData.TrajectoryListEntity();
                            data.setLat(trajectoryListEntities.get(i).getLat());
                            data.setLon(trajectoryListEntities.get(i).getLon());
                            data.setSysTime(trajectoryListEntities.get(i).getSysTime());
                            mTrajectoryListEntities.add(data);
                            L.e("gps::::"+mTrajectoryListEntities.size());
                            L.e("gps::::"+trajectoryListEntities.get(i).getLat() +":" + trajectoryListEntities.get(i).getLon());
                        }
                    }
                    if (mTrajectoryListEntities.size() > 0) {
                        historyTrackTvTime.setText(mTrajectoryListEntities.get(0).getSysTime());
                        getAddress(mTrajectoryListEntities.get(0).getLatLng());
                        addDotMarker();
                    } else {
                        tv_no_history_data.setVisibility(View.VISIBLE);
                        noDataView();
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
            }
        });
        queue.add(request);

//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("user_id", watchID);
//            jsonObject.put("gps_time", dataTime);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API, AppConstants.JAVA_TRAJECTORY_LIST_URL, jsonObject, new HttpClientListener() {
//            @Override
//            public void onSuccess(String result) {
//                Gson gson = new Gson();
//                HistoryTrackData fromJson = gson.fromJson(result, HistoryTrackData.class);
//                if (fromJson == null)
//                    return;
//                List<HistoryTrackData.TrajectoryListEntity> trajectoryListEntities = fromJson.getTrajectoryList();
//                if (trajectoryListEntities == null)
//                    return;
//                if (aMap != null)
//                    aMap.clear();
//                loading.dismiss();
//                tv_no_history_data.setVisibility(View.GONE);
//
//                mTrajectoryListEntities = new ArrayList<HistoryTrackData.TrajectoryListEntity>();
//                historyTrackBtnPlay.setBackgroundResource(R.drawable.button_common_on_selector_green);
//                historyTrackBtnPlay.setEnabled(true);
//                if (trajectoryListEntities.size() == 0) {
//                    tv_no_history_data.setVisibility(View.VISIBLE);
//                    if (init_center.latitude != 0 && init_center.longitude != 0) {
//                        CameraUpdate update = CameraUpdateFactory.newCameraPosition(new CameraPosition(
//                                init_center, 10, 0, 0));
//                        aMap.animateCamera(update);
//                    }
//                    noDataView();
//                } else if (trajectoryListEntities.size() == 1) {
//                    mTrajectoryListEntities = trajectoryListEntities;
//                    historyTrackTvTime.setText(mTrajectoryListEntities.get(0).getSysTime());
//                    getAddress(mTrajectoryListEntities.get(0).getLatLng());
//                    addDotMarker();
//                    noDataView();
//
//                } else {
//                    for (int i = 0; i < trajectoryListEntities.size(); i++) {
//                        int j = i;
//                        if (j + 1 == trajectoryListEntities.size()) {
//                            j = -1;
//                        }
//                        double lat = parseDouble(trajectoryListEntities.get(i).getLat());
//                        double lat2 = parseDouble(trajectoryListEntities.get(j + 1).getLat());
//                        double lon = parseDouble(trajectoryListEntities.get(i).getLon());
//                        double lon2 = parseDouble(trajectoryListEntities.get(j + 1).getLon());
//                        if (lon != lon2 && lat != lat2) {
//                            HistoryTrackData.TrajectoryListEntity data = new HistoryTrackData.TrajectoryListEntity();
//                            data.setLat(trajectoryListEntities.get(i).getLat());
//                            data.setLon(trajectoryListEntities.get(i).getLon());
//                            data.setSysTime(trajectoryListEntities.get(i).getSysTime());
//                            mTrajectoryListEntities.add(data);
//                            L.e("gps::::"+mTrajectoryListEntities.size());
//                        }
//                    }
//                    if (mTrajectoryListEntities.size() > 0) {
//                        historyTrackTvTime.setText(mTrajectoryListEntities.get(0).getSysTime());
//                        getAddress(mTrajectoryListEntities.get(0).getLatLng());
//                        addDotMarker();
//                    } else {
//                        tv_no_history_data.setVisibility(View.VISIBLE);
//                        noDataView();
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFailure(String result) {
//                T.s(result);
//                loading.dismiss();
//            }
//
//            @Override
//            public void onError() {
//                loading.dismiss();
//            }
//        });
    }

    private void noDataView() {
        historyTrackTvAddress.setText("");
        historyTrackTvTime.setText("");
        historyTrackBtnPlay.setBackgroundResource(R.drawable.shape_button_gray_normal);
        historyTrackBtnPlay.setEnabled(false);
    }

    @Override
    public void onClick(View v) {

        if (CMethod.isFastDoubleClick())
            return;
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.history_track_btn_front://前一天

                chooseDateToMap(present_position - 1);

                break;
            case R.id.history_track_btn_behind://后一天

                chooseDateToMap(present_position + 1);

                break;
            case R.id.history_track_btn_play://播放轨迹
                playHistoryTrack();

                break;
            case R.id.history_track_ll_middle://点击中间弹出日历

                historyTrackIvArrow.setImageResource(R.mipmap.history_track_arrow_up);
                calendarPopupWindow.showAsDropDown(this.findViewById(R.id.history_track_rl_top));

                break;

        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (calendarLists != null && position < calendarLists.size() && calendarLists.get(position).isCanSelect()) {
            chooseDateToMap(position);
        }

    }

    /**
     * 当前position的初始化
     */
    private void initPresentPosition() {
        for (int i = 0; i < calendarLists.size(); i++) {
            if (calendarLists.get(i).isToday()) {
                max_position = i;
                present_position = i;
            }
        }
    }

    /**
     * 弹出日历列表的初始化
     */
    private void initCalendarPopupWindow() {
        calendarLists = CalendarUtil.getCalendarList();
        calendarPopupWindow = new HistoryTrackCalendarPopupWindow(this, calendarLists);
        initPresentPosition();
        chooseDateToMap(present_position);

    }


    /**
     * 点击button或者日历更换历史轨迹和页面数据
     *
     * @param position
     */
    private void chooseDateToMap(int position) {
        loading.show();
        isMarkerMoveStop = true;
        present_position = position;

        if (position < calendarLists.size()) {
            if (!calendarLists.get(position).isCanSelect()) {
                return;
            }
            if (calendarLists.get(position).isToday()) {
                historyTrackTvDay.setText(getResources().getString(R.string.today));
            } else {
                historyTrackTvDay.setText(calendarLists.get(position).getTitle());
            }
        }
        calendarPopupWindow.notifyAdapter(position);
        calendarPopupWindow.dismiss();
        notifyButtonColor();
        getHistoryTrackListToDrowLine();

    }

    /**
     * 前后天颜色值变化
     */
    private void notifyButtonColor() {
        L.e("history_present_position==" + present_position);
        L.e("history_min_position==" + min_position);
        L.e("history_max_position==" + max_position);
        if (present_position <= min_position) {
            historyTrackBtnFront.setTextColor(getResources().getColor(R.color.gray_text_color));
            historyTrackBtnFront.setBackgroundResource(R.drawable.my_round_text_gray);
            historyTrackBtnFront.setEnabled(false);

            historyTrackBtnBehind.setTextColor(getResources().getColor(R.color.green_normal_color));
            historyTrackBtnBehind.setBackgroundResource(R.drawable.my_round_text_blue);
            historyTrackBtnBehind.setEnabled(true);

        } else if (present_position >= max_position) {
            historyTrackBtnBehind.setTextColor(getResources().getColor(R.color.gray_text_color));
            historyTrackBtnBehind.setBackgroundResource(R.drawable.my_round_text_gray);
            historyTrackBtnBehind.setEnabled(false);

            historyTrackBtnFront.setTextColor(getResources().getColor(R.color.green_normal_color));
            historyTrackBtnFront.setBackgroundResource(R.drawable.my_round_text_blue);
            historyTrackBtnFront.setEnabled(true);
        } else {
            historyTrackBtnFront.setTextColor(getResources().getColor(R.color.green_normal_color));
            historyTrackBtnFront.setBackgroundResource(R.drawable.my_round_text_blue);
            historyTrackBtnFront.setEnabled(true);

            historyTrackBtnBehind.setTextColor(getResources().getColor(R.color.green_normal_color));
            historyTrackBtnBehind.setBackgroundResource(R.drawable.my_round_text_blue);
            historyTrackBtnBehind.setEnabled(true);
        }

    }


    /**
     * 初始各个经纬度撒点
     */
    private void addDotMarker() {
        if (aMap == null || mTrajectoryListEntities == null || mTrajectoryListEntities.size() == 0)
            return;
        if (aMap != null)
            aMap.clear();
        for (int i = 0; i < mTrajectoryListEntities.size(); i++) {
            aMap.addMarker(getDotMarkerOptions(mTrajectoryListEntities.get(i)));
        }
        mMoveMarker = aMap.addMarker(getBabyMarkerOptions(baby_sex, mTrajectoryListEntities.get(0).getLatLng()));
        countMapOrder(mTrajectoryListEntities);
    }

    private void countTimeInterval() {
        if (aMap == null)
            return;
        float zoom = aMap.getCameraPosition().zoom;
        if (zoom <= 19 && zoom > 10)
            TIME_INTERVAL = (int) (11 * (zoom - 10));
        else
            TIME_INTERVAL = 1;

        L.e("TIME_INTERVAL===" + TIME_INTERVAL);

    }

    /**
     * 播放轨迹
     */
    private void playHistoryTrack() {

        if (aMap == null || mTrajectoryListEntities == null || mTrajectoryListEntities.size() == 0)
            return;

        if (aMap != null)
            aMap.clear();


        mPolyline = drawPolyline(mTrajectoryListEntities);
        drawArrow();
        moveMap();
        mMoveMarker = aMap.addMarker(getBabyMarkerOptionsFrameAnimation(baby_sex, mTrajectoryListEntities.get(0).getLatLng()));
        if (moveBabyMarker != null && moveBabyMarker.getStatus() == AsyncTask.Status.RUNNING) {
            isMarkerMoveStop = true;
        } else {
            isMarkerMoveStop = false;
            moveBabyMarker = new MoveBabyMarkerTask();
            moveBabyMarker.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            historyTrackBtnPlay.setText(getResources().getString(R.string.cancel_play));
        }
    }

    private boolean isMarkerMoveStop = true;

    /**
     * 异步处理播放轨迹
     */
    private class MoveBabyMarkerTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            moveBabyMarker();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            historyTrackBtnPlay.setText(getResources().getString(R.string.play_track));
            addDotMarker();
        }
    }

    /**
     * 历史轨迹的播放具体执行（宝贝头像移动）
     */
    private void moveBabyMarker() {

        L.e("start_history_track");
        for (int i = 0; i < mPolyline.getPoints().size() - 1; i++) {
            LatLng startPoint = mPolyline.getPoints().get(i);
            LatLng endPoint = mPolyline.getPoints().get(i + 1);
            L.e("start_history_track=" + i + "==" + startPoint + "-->" + endPoint);
            mMoveMarker.setPosition(startPoint);

            double slope = getSlope(startPoint, endPoint);
            //是不是正向的标示（向上设为正向）
            boolean isReverse = (startPoint.latitude > endPoint.latitude);
            double intercept = getInterception(slope, startPoint);
            double xMoveDistance = isReverse ? getXMoveDistance(slope) : -1 * getXMoveDistance(slope);
            for (double j = startPoint.latitude; !((j > endPoint.latitude) ^ isReverse); j = j - xMoveDistance) {
                L.e("start_history_track2==" + isMarkerMoveStop);
                if (isMarkerMoveStop)
                    return;
                LatLng latLng;
                if (slope != Double.MAX_VALUE) {
                    latLng = new LatLng(j, (j - intercept) / slope);
                } else {
                    latLng = new LatLng(j, startPoint.longitude);
                }

                //宝贝头像离开可视区域时，中心的跳至该点
                VisibleRegion visibleRegion = aMap.getProjection().getVisibleRegion(); // 获取可视区域
                LatLngBounds latLngBounds = visibleRegion.latLngBounds;// 获取可视区域的Bounds
                boolean isContain = latLngBounds.contains(latLng);// 判断
                if (!isContain) {
                    if (aMap == null || aMap.getCameraPosition() == null) {
                        return;
                    }
                    CameraUpdate update = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                            latLng, aMap.getCameraPosition().zoom, 0, 0));

                    aMap.animateCamera(update);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                mMoveMarker.setPosition(latLng);
                //移动速度随着缩放的大小变化，待优化
                countTimeInterval();
                try {
                    Thread.sleep(TIME_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

    }


    /**
     * 通过所有值得经纬度计算缩放比例及中心点
     */
    private void countMapOrder(List<HistoryTrackData.TrajectoryListEntity> lists) {
        //没有为0的经纬度才能参与计算
        List<LatLng> latlngs = new ArrayList<LatLng>();
        for (int i = 0; i < lists.size(); i++) {
            if (lists.get(i).getLatLng().latitude != 0) {
                latlngs.add(lists.get(i).getLatLng());
            }
        }
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

        if (num == 0 && latlngs != null) {
            present_center = latlngs.get(0);
        } else {
            present_center = new LatLng(all_lat / num, all_lng / num);
        }
        moveMap();
    }

    /**
     * 地图的移动
     */
    private void moveMap() {
        if (present_center == null)
            return;
        if (present_center.latitude != 0 && present_center.longitude != 0) {
            CameraUpdate update = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                    present_center, present_order, 0, 0));
            aMap.animateCamera(update);
        }
    }

    private List<LatLng> getLatLngList(List<HistoryTrackData.TrajectoryListEntity> paramArrayList) {
        List<LatLng> latLngLists = new ArrayList<LatLng>();
        for (int i = 0; i < paramArrayList.size(); i++) {
            latLngLists.add(paramArrayList.get(i).getLatLng());
        }
        return latLngLists;
    }

    /**
     * 地图上的路线
     *
     * @param paramArrayList
     * @return
     */
    private Polyline drawPolyline(List<HistoryTrackData.TrajectoryListEntity> paramArrayList) {
        PolylineOptions localPolylineOptions = new PolylineOptions();
        localPolylineOptions.visible(true);
        localPolylineOptions.color(0xFF009fda);
        localPolylineOptions.width(this.lineWidth);
        localPolylineOptions.setDottedLine(true);
        localPolylineOptions.geodesic(true);
        localPolylineOptions.addAll(getLatLngList(paramArrayList));
        return aMap.addPolyline(localPolylineOptions);
    }

    /**
     * 计算x方向每次移动的距离
     */
    private double getXMoveDistance(double slope) {
        if (slope == Double.MAX_VALUE) {
            return DISTANCE;
        }
        return Math.abs((DISTANCE * slope) / Math.sqrt(1 + slope * slope));
    }

    /**
     * 画箭头的marker
     */
    private void drawArrow() {

        for (int i = 0; i < mPolyline.getPoints().size() - 1; i++) {
            LatLng startPoint = mPolyline.getPoints().get(i);
            LatLng endPoint = mPolyline.getPoints().get(i + 1);
            dotmarker = aMap.addMarker(getArrowMarkerOptions(mTrajectoryListEntities.get(i).getLatLng()));

            getAngle(startPoint, endPoint);
        }
    }
    /**
     * 设置MarkerOptions（奔跑的动画）
     *
     * @param baby_sex
     * @param paramLatLng
     * @return
     */
    private MarkerOptions getBabyMarkerOptionsFrameAnimation(int baby_sex, LatLng paramLatLng) {

        BitmapDescriptor paramBitmapDescriptor;
        MarkerOptions localMarkerOptions = new MarkerOptions();
        localMarkerOptions.position(paramLatLng);
        ArrayList<BitmapDescriptor> arrayList=new ArrayList<BitmapDescriptor>();
        if (baby_sex == 1) {

            BitmapDescriptor paramBitmapDescriptor0 = BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(),
                            R.mipmap.run0));
            BitmapDescriptor paramBitmapDescriptor1 = BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(),
                            R.mipmap.run1));
            BitmapDescriptor paramBitmapDescriptor2 = BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(),
                            R.mipmap.run2));

            arrayList.add(paramBitmapDescriptor0);
            arrayList.add(paramBitmapDescriptor1);
            arrayList.add(paramBitmapDescriptor2);

/*            paramBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(),
                            R.mipmap.history_track_boy));*/
        } else {
            paramBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(),
                            R.mipmap.history_track_girl));
        }
        localMarkerOptions.icons(arrayList);
        localMarkerOptions.anchor(0.5F, 0.9F);//轨迹覆盖物位置调整
        localMarkerOptions.title("");
        return localMarkerOptions;
    }
    /**
     * 设置MarkerOptions
     *
     * @param baby_sex
     * @param paramLatLng
     * @return
     */
    private MarkerOptions getBabyMarkerOptions(int baby_sex, LatLng paramLatLng) {

        BitmapDescriptor paramBitmapDescriptor;
        MarkerOptions localMarkerOptions = new MarkerOptions();
        localMarkerOptions.position(paramLatLng);
        if (baby_sex == 1) {
            paramBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(),
                            R.mipmap.history_track_boy));
        } else {
            paramBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(),
                            R.mipmap.history_track_girl));
        }
        localMarkerOptions.icon(paramBitmapDescriptor);
        localMarkerOptions.anchor(0.5F, 0.9F);//轨迹覆盖物位置调整
        localMarkerOptions.title("");
        return localMarkerOptions;
    }


    /**
     * 设置点的MarkerOptions
     *
     * @return
     */
    private MarkerOptions getDotMarkerOptions(HistoryTrackData.TrajectoryListEntity data) {
        BitmapDescriptor paramBitmapDescriptor;
        MarkerOptions localMarkerOptions = new MarkerOptions();
        localMarkerOptions.position(data.getLatLng());
        paramBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(),
                        R.mipmap.history_track_dot));
        localMarkerOptions.icon(paramBitmapDescriptor);
        localMarkerOptions.anchor(0.5F, 0.5F);
        localMarkerOptions.title("");
        localMarkerOptions.snippet(data.getSysTime());
        return localMarkerOptions;
    }

    /**
     * 设置箭头的MarkerOptions
     *
     * @param paramLatLng
     * @return
     */
    private MarkerOptions getArrowMarkerOptions(LatLng paramLatLng) {

        BitmapDescriptor paramBitmapDescriptor;
        MarkerOptions localMarkerOptions = new MarkerOptions();
        localMarkerOptions.position(paramLatLng);
        paramBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(),
                        R.mipmap.history_track_arrow));
        localMarkerOptions.icon(paramBitmapDescriptor);
        localMarkerOptions.anchor(0.5F, 0.5F);
        localMarkerOptions.title("");
        return localMarkerOptions;
    }


    /**
     * 根据点和斜率算取截距
     */
    private double getInterception(double slope, LatLng point) {

        double interception = point.latitude - slope * point.longitude;
        return interception;
    }

    /**
     * 倾斜度
     *
     * @param fromPoint
     * @param toPoint
     * @return
     */
    private double getAngle(LatLng fromPoint, LatLng toPoint) {
        double slope = getSlope(fromPoint, toPoint);
        if (slope == Double.MAX_VALUE) {
            if (toPoint.latitude > fromPoint.latitude) {
                return 0;
            } else {
                return 180;
            }
        }
        float deltAngle = 0;
        if ((toPoint.latitude - fromPoint.latitude) * slope < 0) {
            deltAngle = 180;
        }
        double radio = Math.atan(slope);
        double angle = 180 * (radio / Math.PI) + deltAngle - 90;
        dotmarker.setRotateAngle((float) angle);
        return angle;
    }

    /**
     * 算斜率
     */
    private double getSlope(LatLng fromPoint, LatLng toPoint) {
        if (toPoint.longitude == fromPoint.longitude) {
            return Double.MAX_VALUE;
        }
        double slope = ((toPoint.latitude - fromPoint.latitude) / (toPoint.longitude - fromPoint.longitude));
        return slope;

    }


    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLng latLng) {

        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        historyTrackTvAddress.setText(getResources().getString(R.string.get_address));
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
                addressName = addressName.equals("") ? getResources().getString(R.string.geographical_failure) : addressName + getResources().getString(R.string.nearby);
                if (tv_no_history_data.getVisibility() != View.VISIBLE) {
                    historyTrackTvAddress.setText(addressName);
                }

            } else {
                if (tv_no_history_data.getVisibility() != View.VISIBLE)
                    historyTrackTvAddress.setText(getResources().getString(R.string.geographical_failure));
                L.e("无结果");
            }
        } else if (rCode == 27) {
            if (tv_no_history_data.getVisibility() != View.VISIBLE)
                historyTrackTvAddress.setText(getResources().getString(R.string.geographical_failure));
            L.e("网络错误");
        } else if (rCode == 32) {
            if (tv_no_history_data.getVisibility() != View.VISIBLE)
                historyTrackTvAddress.setText(getResources().getString(R.string.geographical_failure));
            L.e("错误的kEy");
        } else {
            if (tv_no_history_data.getVisibility() != View.VISIBLE)
                historyTrackTvAddress.setText(getResources().getString(R.string.geographical_failure));
            L.e("其他错误");
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

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
        isMarkerMoveStop = true;
        mapView.onDestroy();
    }
}
