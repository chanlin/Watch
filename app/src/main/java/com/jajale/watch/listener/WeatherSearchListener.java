package com.jajale.watch.listener;

import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.model.LatLng;

/**
 * Created by athena on 2016/2/17.
 * Email: lizhiqiang@bjjajale.com
 */
public interface WeatherSearchListener {
    void search(String cityName,TextView cityTextView,TextView textView,ImageView imageView,int position);
    void getAddress(LatLng latLng,TextView cityTextView,TextView textView,ImageView imageView,int position);
}
