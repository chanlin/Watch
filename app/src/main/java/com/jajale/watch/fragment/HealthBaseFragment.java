package com.jajale.watch.fragment;

import android.support.v4.app.Fragment;

import com.jajale.watch.entity.GrowRecordData;

/**
 * Created by chunlongyuan on 12/7/15.
 */
public abstract class HealthBaseFragment extends Fragment {
    public abstract void refresh(GrowRecordData growRecordData);
}
