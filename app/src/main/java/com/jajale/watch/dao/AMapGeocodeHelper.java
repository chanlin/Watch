package com.jajale.watch.dao;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.interfaces.GeocodeEventHandler;
import com.jajale.watch.interfaces.IGeocodeModel;


public class AMapGeocodeHelper implements IGeocodeModel, GeocodeSearch.OnGeocodeSearchListener {


    private final GeocodeSearch geocodeSearch;
    private GeocodeEventHandler handler;

    public AMapGeocodeHelper(GeocodeEventHandler handler) {
        this.handler = handler;//应该设计成可以多次请求而不会互相干扰，所以不能每次都setOnGeocodeSearchListener
        geocodeSearch = new GeocodeSearch(BaseApplication.getContext());
    }


    @Override
    public void regeocodeSearch(double lat, double lng) {
        geocodeSearch.setOnGeocodeSearchListener(this);
        geocodeSearch.getFromLocationAsyn(new RegeocodeQuery(new LatLonPoint(lat, lng), 200, GeocodeSearch.AMAP));
    }

    @Override
    public void onStop() {
        geocodeSearch.setOnGeocodeSearchListener(null);
    }

    public void onDestroy(){
        handler = null;
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
//        MyLogger.jLog().i("反向编码回调：code:"+i);
        if (handler!=null){
            handler.onRegeocodeAMap(i,regeocodeResult);
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        if (handler!=null){
            handler.onGeocodeAMap(i,geocodeResult);
        }
    }
}
