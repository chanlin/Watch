package com.jajale.watch.activity;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.weather.LocalDayWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearch.OnWeatherSearchListener;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.jajale.watch.BaseActivity;
import com.jajale.watch.IntentAction;
import com.jajale.watch.R;
import com.jajale.watch.adapter.WeatherListAdapter;
import com.jajale.watch.dao.AMapGeocodeHelper;
import com.jajale.watch.entity.CityListEntity;
import com.jajale.watch.interfaces.GeocodeEventHandler;
import com.jajale.watch.listener.WeatherSearchListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WeatherSearchActivity extends BaseActivity implements OnWeatherSearchListener, View.OnClickListener, WeatherSearchListener {
    private TextView weather_tv;
    private TextView temperature_tv;
    private ImageView weather_iv;
    private TextView city;
    private ListView weather_lv;
    private WeatherSearchQuery mquery;
    private WeatherSearch mweathersearch;
    private LocalWeatherLive weatherlive;
    private LocalWeatherForecast weatherforecast;
    private List<LocalDayWeatherForecast> forecastlist = null;
    private String cityname = "";//天气搜索的城市，可以写名称或adcode；
    private List<CityListEntity> cityList = new ArrayList();

    WeatherListAdapter adapter;
    HashMap<String, Integer> mBWeatherMap = new HashMap<String, Integer>() {
        {
            put("晴", R.mipmap.w_b_sunny);
            put("多云", R.mipmap.w_b_cloudy);
            put("阴", R.mipmap.w_b_overcast);
            put("阵雨", R.mipmap.w_b_shower);
            put("雷阵雨", R.mipmap.w_b_thundershower);
            put("雷阵雨并伴有冰雹", R.mipmap.w_b_thundershower_with_hail);
            put("雨夹雪", R.mipmap.w_b_sleet);
            put("小雨", R.mipmap.w_b_light_rain);
            put("中雨", R.mipmap.w_b_moderate_rain);
            put("大雨", R.mipmap.w_b_heavy_rain);
            put("暴雨", R.mipmap.w_b_storm);
            put("大暴雨", R.mipmap.w_b_heavy_storm);
            put("特大暴雨", R.mipmap.w_b_severe_storm);
            put("阵雪", R.mipmap.w_b_snow_flurry);
            put("小雪", R.mipmap.w_b_light_snow);
            put("中雪", R.mipmap.w_b_moderate_snow);
            put("大雪", R.mipmap.w_b_heavy_snow);
            put("暴雪", R.mipmap.w_b_snowstorm);
            put("雾", R.mipmap.w_b_foggy);
            put("冻雨", R.mipmap.w_b_ice_rain);
            put("沙尘暴", R.mipmap.w_b_duststorm);
            put("小雨-中雨", R.mipmap.w_b_light_to_moderate_rain);
            put("中雨-大雨", R.mipmap.w_b_moderate_to_heavy_rain);
            put("大雨-暴雨", R.mipmap.w_b_heavy_rain_to_storm);
            put("暴雨-大暴雨", R.mipmap.w_b_storm_to_heavy_storm);
            put("大暴雨-特大暴雨", R.mipmap.w_heavy_to_severe_storm);
            put("小雪-中雪", R.mipmap.w_b_light_to_moderate_snow);
            put("中雪-大雪", R.mipmap.w_b_moderate_to_heavy_snow);
            put("大雪-暴雪", R.mipmap.w_b_heavy_snow_to_snowstorm);
            put("浮尘", R.mipmap.w_b_dust);
            put("扬沙", R.mipmap.w_b_sand);
            put("强沙尘暴", R.mipmap.w_b_sandstorm);
            put("飑", R.mipmap.w_b_sandstorm);
            put("龙卷风", R.mipmap.w_b_sandstorm);
            put("弱高吹雪", R.mipmap.w_b_sandstorm);
            put("轻霾", R.mipmap.w_b_haze);
            put("霾", R.mipmap.w_b_haze);
        }
    };

    /**********************************************/
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDialog = new LoadingDialog(this);
        loadingDialog.Cancelable(true);
//        cityList.add(new CityListEntity("深圳市", "小黑"));
//        cityList.add(new CityListEntity("西安市", "小王"));
//        cityList.add(new CityListEntity("拉萨市", "小白"));


        cityList = getIntent().getParcelableArrayListExtra(IntentAction.KEY_WEATHER_DATA);
//        cityList.add(new CityListEntity("西安市", "小王"));
//        cityList.add(new CityListEntity("拉萨市", "小白"));
        L.i("---------" + cityList.toString());

        if(cityList==null){
            return;
        }
        if (getCityCount(cityList) > 1) {
            setContentView(R.layout.weather_multi_activity);
            init();
            adapter = new WeatherListAdapter(WeatherSearchActivity.this);
            adapter.setCityList(cityList);
            weather_lv.setAdapter(adapter);
        } else {
            setContentView(R.layout.weather_activity);
            cityname = cityList.get(0).getCityName();

            init();

            if (cityname == null || CMethod.isEmpty(cityname)) {

                getAddress(cityList.get(0).getLatLng(), null, null, null,0);

            } else {
                searchliveweather(cityname);
            }

            city.setText(cityname);
        }

    }

    private int getCityCount(List cityList) {
        int count = cityList.size();
        return count;
    }


    private void init() {
        initTitleView();
        city = (TextView) findViewById(R.id.city_tv);
        weather_tv = (TextView) findViewById(R.id.weather_tv);
        temperature_tv = (TextView) findViewById(R.id.temperature_tv);
        weather_iv = (ImageView) findViewById(R.id.weather_iv);
        weather_lv = (ListView) findViewById(R.id.weather_lv);
    }


    /**
     * 初始化titleview
     */
    private void initTitleView() {
        View title = findViewById(R.id.title);
        TextView midTitle = (TextView) title.findViewById(R.id.tv_middle);
        midTitle.setText(getResources().getString(R.string.weather_title_text));
        ImageView iv_left = (ImageView) title.findViewById(R.id.iv_left);
        TextView tvRight = (TextView) title.findViewById(R.id.tv_right);

        iv_left.setImageResource(R.drawable.title_goback_selector);
        iv_left.setOnClickListener(this);
        tvRight.setText(getResources().getString(R.string.weather_refresh));
        tvRight.setTextSize(14);
        tvRight.setOnClickListener(this);
        tvRight.setTextColor(getResources().getColor(R.color.white));
    }

    /**
     * @param cityname 查询所传城市的天气
     */
    private void searchliveweather(String cityname) {
        mquery = new WeatherSearchQuery(cityname, WeatherSearchQuery.WEATHER_TYPE_LIVE);//检索参数为城市和天气类型，实时天气为1、天气预报为2
        mweathersearch = new WeatherSearch(this);
        mweathersearch.setOnWeatherSearchListener(this);
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn(); //异步搜索
    }

    /**
     * 实时天气查询回调
     */
    @Override
    public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult, int rCode) {
        if (rCode == 0) {
            if (weatherLiveResult != null && weatherLiveResult.getLiveResult() != null) {
                weatherlive = weatherLiveResult.getLiveResult();
                weather_iv.setImageResource(mBWeatherMap.get(weatherlive.getWeather()));
                weather_tv.setText(weatherlive.getWeather());
                temperature_tv.setText(weatherlive.getTemperature() + "°");
                loadingDialog.dismiss();
            } else {
                loadingDialog.dismiss();
                T.s(getResources().getString(R.string.weather_result));
//                     ToastUtil.show(WeatherSearchActivity.this, R.string.no_result);
            }
        } else {
            T.s(rCode);
            L.i("guokm", "rCode:" + rCode);
            loadingDialog.dismiss();
//                ToastUtil.showerror(WeatherSearchActivity.this, rCode);/**/
        }

    }

    /**
     * 天气预报查询结果回调
     */
    @Override
    public void onWeatherForecastSearched(
            LocalWeatherForecastResult weatherForecastResult, int rCode) {
        if (rCode == 1000) {
            if (weatherForecastResult != null && weatherForecastResult.getForecastResult() != null
                    && weatherForecastResult.getForecastResult().getWeatherForecast() != null
                    && weatherForecastResult.getForecastResult().getWeatherForecast().size() > 0) {
                weatherforecast = weatherForecastResult.getForecastResult();
                forecastlist = weatherforecast.getWeatherForecast();
                fillforecast();

            } else {
                T.s(getResources().getString(R.string.weather_result));
            }
        } else {
            T.s(rCode);
        }
    }

    private void fillforecast() {
//        reporttime2.setText(weatherforecast.getReportTime() + "发布");
        String forecast = "";
        for (int i = 0; i < forecastlist.size(); i++) {
            LocalDayWeatherForecast localdayweatherforecast = forecastlist.get(i);
            String week = null;
            switch (Integer.valueOf(localdayweatherforecast.getWeek())) {
                case 1:
                    week = "周一";
                    break;
                case 2:
                    week = "周二";
                    break;
                case 3:
                    week = "周三";
                    break;
                case 4:
                    week = "周四";
                    break;
                case 5:
                    week = "周五";
                    break;
                case 6:
                    week = "周六";
                    break;
                case 7:
                    week = "周日";
                    break;
                default:
                    break;
            }
            String temp = String.format("%-3s/%3s",
                    localdayweatherforecast.getDayTemp() + "°",
                    localdayweatherforecast.getNightTemp() + "°");
            String date = localdayweatherforecast.getDate();
            forecast += date + "  " + week + "                       " + temp + "\n\n";
        }
//        forecasttv.setText(forecast);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
            case R.id.tv_right:
                //刷新操作
                if (!CMethod.isNetWorkEnable(WeatherSearchActivity.this)) {
                    T.s(WeatherSearchActivity.this.getString(R.string.net_poor));
                    return;
                }
                if (adapter != null) {

                    adapter.refresh();
                    loadingDialog.show("",1000);


                } else {
                    searchliveweather(cityname);
                    loadingDialog.show();
                }

                break;
        }
    }

    @Override
    public void search(String cityName, final TextView cityTextView, final TextView textView, final ImageView imageView,final int position) {

        mquery = new WeatherSearchQuery(cityName, WeatherSearchQuery.WEATHER_TYPE_LIVE);//检索参数为城市和天气类型，实时天气为1、天气预报为2
        mweathersearch = new WeatherSearch(this);
        mweathersearch.setOnWeatherSearchListener(new OnWeatherSearchListener() {
            @Override
            public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult, int rCode) {

                L.e("123==rCode===" + rCode);
                if (rCode == 0) {
                    if (weatherLiveResult != null && weatherLiveResult.getLiveResult() != null) {
                        weatherlive = weatherLiveResult.getLiveResult();
                        cityTextView.setText(weatherlive.getCity());
                        imageView.setImageResource(mBWeatherMap.get(weatherlive.getWeather()));
                        textView.setText(weatherlive.getTemperature() + "°");
                        adapter.cityList.get(position).setTemperature(weatherlive.getTemperature());
                        adapter.cityList.get(position).setWeather(weatherlive.getWeather());
                        adapter.cityList.get(position).setCityName(weatherlive.getCity());
                        loadingDialog.dismiss();
                    } else {
                        loadingDialog.dismiss();
                    }
                } else {
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {

            }
        });
        mweathersearch.setQuery(mquery);
        mweathersearch.searchWeatherAsyn(); //异步搜索

    }

    @Override
    public void getAddress(LatLng latLng, final TextView cityTextView, final TextView textView, final ImageView imageView,final int position) {
        AMapGeocodeHelper aMapGeocodeModel = new AMapGeocodeHelper(new GeocodeEventHandler() {
            @Override
            public void onRegeocodeAMap(int code, RegeocodeResult regeocodeResult) {
                L.e("123==code===" + code);
                if (code == 0) {
                    String city = regeocodeResult.getRegeocodeAddress().getCity();
                    String province = regeocodeResult.getRegeocodeAddress().getProvince();
                    if (textView == null || imageView == null) {
                        searchliveweather(city);
                    } else {

                        city = city.equals("") ? province : city;
                        search(city, cityTextView, textView, imageView,position);
                    }

                }
            }

            @Override
            public void onGeocodeAMap(int code, GeocodeResult geocodeResult) {
            }
        });
        aMapGeocodeModel.regeocodeSearch(latLng.latitude, latLng.longitude);
    }


    @Override
    public void onStop() {
        super.onStop();

        int size = models.size();
        for (int i = 0; i < size; i++) {
            AMapGeocodeHelper aMapGeocodeModel = models.valueAt(size);
            if (aMapGeocodeModel != null) {
                aMapGeocodeModel.onDestroy();//退出时取消所有请求，避免内存回收问题
            }
        }
        models.clear();
    }

    SparseArray<AMapGeocodeHelper> models = new SparseArray<AMapGeocodeHelper>();
    int runningProcessId;

}
