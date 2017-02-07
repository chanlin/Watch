package com.jajale.watch.cviews;import android.content.Context;import android.view.View;import android.widget.FrameLayout;import android.widget.ImageView;import android.widget.TextView;import com.amap.api.maps.AMap;import com.amap.api.maps.TextureMapView;import com.amap.api.maps.model.LatLng;import com.jajale.watch.R;import com.jajale.watch.entity.LocationUserInfoEntity;import com.jajale.watch.fragment.Location2Fragment;import com.jajale.watch.utils.CMethod;import com.jajale.watch.utils.DensityUtil;/** * Created by yinzhiqun on 2015/10/22. */public class InfoWindow {    private Context context;    private Location2Fragment fragment;    private ViewHolder holder;    private int defaultDiffY;    private int circleDiffY;    private boolean lastIsCircle;    private final int diffX;    public InfoWindow(Context context, Location2Fragment fragment) {        this.context = context;        this.fragment = fragment;//        //显示大头针时的偏移量        defaultDiffY = DensityUtil.dip2px(context, 50);//        //显示小圈时的偏移量        circleDiffY = DensityUtil.dip2px(context, 10);        diffX = DensityUtil.dip2px(context, 115);    }    public void init() {        holder = new ViewHolder();        View custom_info_window = View.inflate(context, R.layout.reai_info_window, null);        final FrameLayout contener = fragment.getInfowindowContener();        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(                DensityUtil.dip2px(context, 230), DensityUtil.dip2px(context, 155));        contener.addView(custom_info_window, params);        holder.infoWindow = custom_info_window;        holder.satellite_iv = (ImageView) custom_info_window.findViewById(R.id.infowindow_satellite_iv);        holder.name_tv = (TextView) custom_info_window.findViewById(R.id.infowindow_name_tv);        holder.address_tv = (TextView) custom_info_window.findViewById(R.id.infowindow_address_tv);        holder.date_tv = (TextView) custom_info_window.findViewById(R.id.infowindow_date_tv);        holder.battery_tv = (TextView) custom_info_window.findViewById(R.id.infowindow_battery_tv);        holder.battery_iv = (ImageView) custom_info_window.findViewById(R.id.infowindow_battery_iv);        holder.navigate_view = custom_info_window.findViewById(R.id.navigate_view);        holder.poweroff_tv = custom_info_window.findViewById(R.id.poweroff_tv);        holder.navigate_view.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View view) {//                LatLng latLng = (LatLng) holder.infoWindow.getTag();                //开启导航                fragment.startNavigation();            }        });    }    public void show(LocationUserInfoEntity locus, AMap aMap, TextureMapView mapView) {        holder.name_tv.setText(locus.getNickName());        if (!CMethod.isEmpty(locus.getAddress())) {//有address，直接设置            holder.address_tv.setText(locus.getAddress());        } else {//没有address，反向编码            holder.address_tv.setText("");            fragment.getAddress(locus, holder.address_tv);        }        setDate(holder.date_tv, locus.getBaseTime());        setBattery(holder.battery_iv, holder.battery_tv, locus.getElectricity());        setPositionType(holder.satellite_iv, locus.getBaseType());        holder.infoWindow.setTag(locus.getLatLng());        setIsPowerOff(false);        updateInfoWindow(locus.getLatLng(), true, true, aMap, mapView);    }//    public void show(LocationResult.Data location,AMap aMap,MapView mapView) {//        holder.name_tv.setText(terminalName);//        if (!TextUtils.isEmpty(location.address)) {//有address，直接设置//            holder.address_tv.setText(location.address);//        }else {//没有address，反向编码//            holder.address_tv.setText("");//            context.getAddress(location, holder.address_tv);//        }//        setDate(holder.date_tv, location.datetime);//        setBattery(holder.battery_iv, holder.battery_tv, location.battery, location.batteryBar);//        setPositionType(holder.satellite_iv, location.positiontype);//        holder.infoWindow.setTag(location.latlng);////        setIsPowerOff(location.status==2);////        updateInfoWindow(location.latlng,true,true,aMap,mapView);//    }    public void update(AMap aMap, TextureMapView mapView) {//拖动地图时，改变infowindow的位置        if (isShowing()) {            LatLng latLng = (LatLng) holder.infoWindow.getTag();            updateInfoWindow(latLng, false, true, aMap, mapView);        }    }    /**     * @param ll         infowindow位置     * @param animate    是否使用弹出动画     * @param useDefault true：起点，终点，track图标，false：基站gps圆圈图标     */    private void updateInfoWindow(LatLng ll, boolean animate, boolean useDefault, AMap aMap, TextureMapView mapView) {        // 1.首先通过latlng转成屏幕坐标系，获取在屏幕上的位置        android.graphics.Point point = null;        try {            point = aMap.getProjection().toScreenLocation(ll);        } catch (Exception e) {            e.printStackTrace();        }        if (point == null) {            return;        }        if (!useDefault || (lastIsCircle && !animate)) {//要求用圆圈的或者上一个弹出是圆圈，现在只是改变位置            holder.infoWindow.setTranslationY(point.y - circleDiffY - holder.infoWindow.getHeight());        } else {            holder.infoWindow.setTranslationY(point.y - defaultDiffY - holder.infoWindow.getHeight());        }//        holder.infoWindow.setTranslationX(point.x - holder.infoWindow.getWidth() / 2);        holder.infoWindow.setTranslationX(point.x - diffX);        if (animate) {//弹出动画            lastIsCircle = !useDefault;            holder.infoWindow.setVisibility(View.VISIBLE);        }    }    public void hide() {        if (isShowing()) {//            //执行关闭动画            holder.infoWindow.setVisibility(View.INVISIBLE);            lastIsCircle = false;        }    }    private void setDate(TextView tv, String datetime) {        tv.setText(datetime);    }    public static void setBattery(ImageView infoWindow_Battery, TextView infoWindow_Battery_tv, int battery) {        infoWindow_Battery_tv.setText(battery + "%");        infoWindow_Battery_tv.setTag(battery + "%");        if (battery <= 33) {            infoWindow_Battery.setBackgroundResource(R.mipmap.battery_one);        } else if (battery > 33 && battery <= 66) {            infoWindow_Battery.setBackgroundResource(R.mipmap.battery_two);        } else if (battery > 66 && battery <=100) {            infoWindow_Battery.setBackgroundResource(R.mipmap.battery_three);        }    }    private void setPositionType(ImageView infoWindow_Satellite, int positiontype) {        switch (positiontype) {            case 0:                infoWindow_Satellite.setImageResource(R.mipmap.showinfo_satellite_image);                infoWindow_Satellite.setTag(context.getString(R.string.gps_location));                break;            case 1:                infoWindow_Satellite.setImageResource(R.mipmap.infowindow_positiontype_wifi);                infoWindow_Satellite.setTag(context.getString(R.string.wifi_location));                break;            case 2:                infoWindow_Satellite.setImageResource(R.mipmap.showinfo_tracksign_image);                infoWindow_Satellite.setTag(context.getString(R.string.base_station_location));                break;        }    }    public boolean isShowing() {        return holder.infoWindow.getVisibility() == View.VISIBLE;    }    public void updateTimeStr(String dateTime) {        if (isShowing()) {            setDate(holder.date_tv, dateTime);        }    }    public void setIsPowerOff(boolean b) {        holder.poweroff_tv.setVisibility(b ? View.VISIBLE : View.INVISIBLE);    }    static class ViewHolder {        public ImageView satellite_iv;        public TextView name_tv;        public TextView address_tv;        public TextView date_tv;        public TextView battery_tv;        public ImageView battery_iv;        public View infoWindow;        public View right_rl;        public View left_rl;        public View navigate_view;        public View poweroff_tv;    }}