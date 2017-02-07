package com.jajale.watch.fragment;

import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by lilonghui on 2016/1/15.
 * Email:lilonghui@bjjajale.com
 */
public class HistoryTrackFragment {

    private float q = 4.0F;



    private MarkerOptions a(BitmapDescriptor paramBitmapDescriptor, LatLng paramLatLng)
    {
        MarkerOptions localMarkerOptions = new MarkerOptions();
        localMarkerOptions.position(paramLatLng);
        localMarkerOptions.icon(paramBitmapDescriptor);
        localMarkerOptions.anchor(0.5F, 0.5F);
        localMarkerOptions.title("");
        return localMarkerOptions;
    }

    private Polyline drawPolyline(ArrayList paramArrayList)
    {
        PolylineOptions localPolylineOptions = new PolylineOptions();
        localPolylineOptions.visible(true);
        localPolylineOptions.setCustomTexture(null);
        localPolylineOptions.width(this.q);
        localPolylineOptions.addAll(paramArrayList);
        return null;
    }
}
