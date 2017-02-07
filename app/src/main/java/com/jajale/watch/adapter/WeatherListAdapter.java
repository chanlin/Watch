package com.jajale.watch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.entity.CityListEntity;

import java.util.HashMap;
import java.util.List;

import com.jajale.watch.listener.WeatherSearchListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.L;


public class WeatherListAdapter extends BaseAdapter {

    public List<CityListEntity> cityList;
    private LayoutInflater mInflater;
    private Context mcontext;
    private ViewHolder holder;
    WeatherSearchListener weatherSearchListener;

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

    public WeatherListAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mcontext = context;
        weatherSearchListener = (WeatherSearchListener) mcontext;
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return cityList == null ? 0 : cityList.size();
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return cityList.get(arg0);
    }

    @SuppressWarnings("deprecation")
    public View getView(int position, View convertView, ViewGroup parent) {


        View view = convertView;
        holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.item_weather_multi, null);
            holder.weather_tv_s = (TextView) view.findViewById(R.id.weather_tv_s);
            holder.weather_iv_small = (ImageView) view.findViewById(R.id.weather_iv_small);
            holder.city_tv = (TextView) view.findViewById(R.id.city_tv);
            holder.name_tv = (TextView) view.findViewById(R.id.name_tv);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        CityListEntity entity = cityList.get(position);


        holder.city_tv.setText(entity.getCityName());
        holder.name_tv.setText(entity.getChildName());
        holder.weather_iv_small.setVisibility(View.VISIBLE);

        if (entity.getWeather() == null) {
            L.e("123-----------------------1--position--" + position);
            if (entity.getCityName() == null || CMethod.isEmpty(entity.getCityName())) {
                L.e("123-----------------------entity.getLatLng()==" + entity.getLatLng().latitude + "," + entity.getLatLng().longitude);
                if (entity.getLatLng().latitude == 0 || entity.getLatLng().longitude == 0) {
                    holder.weather_iv_small.setVisibility(View.INVISIBLE);
                    holder.weather_tv_s.setText("暂未获取经纬度");
                } else {

                    weatherSearchListener.getAddress(entity.getLatLng(), holder.city_tv, holder.weather_tv_s, holder.weather_iv_small, position);
                }

            } else {
                weatherSearchListener.search(entity.getCityName(), holder.city_tv, holder.weather_tv_s, holder.weather_iv_small, position);
            }
        } else {
            L.e("123-----------------------2--position--" + position);
            holder.weather_iv_small.setImageResource(mBWeatherMap.get(entity.getWeather()));
            holder.weather_tv_s.setText(entity.getTemperature() + "°");
        }


        return view;
    }

    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    public void refresh() {

        notifyDataSetInvalidated();
    }

    public void clear() {
        cityList.clear();
    }

    public void setCityList(List<CityListEntity> cityList) {
        this.cityList = cityList;
    }

    public final class ViewHolder {


        TextView weather_tv_s;
        TextView city_tv;
        TextView name_tv;
        ImageView weather_iv_small;
//        ImageView weather_iv_small;
//        TextView weather_tv_s;

    }
}
