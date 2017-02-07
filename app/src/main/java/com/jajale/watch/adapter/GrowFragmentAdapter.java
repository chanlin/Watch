package com.jajale.watch.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.jajale.watch.R;
import com.jajale.watch.entity.GrowRecordData;
import com.jajale.watch.fragment.HealthBaseFragment;
import com.jajale.watch.fragment.HealthyRecordFragment;
import com.jajale.watch.fragment.HeightChartFragment;
import com.jajale.watch.fragment.WeightChartFragment;

/**
 * Created by chunlongyuan on 12/8/15.
 */
public class GrowFragmentAdapter extends FragmentStatePagerAdapter {

    private static final String[] CONTENT = new String[3];

    private HealthBaseFragment[] fragments;
    public  GrowRecordData growRecordData;

    public GrowFragmentAdapter(Context context, GrowRecordData growRecordData ,FragmentManager fm) {
        super(fm);
        this.growRecordData=growRecordData;
        CONTENT[0] = context.getString(R.string.grow_record);
        CONTENT[1] = context.getString(R.string.grow_height_curve);
        CONTENT[2] = context.getString(R.string.grow_weight_curve);
        fragments = new HealthBaseFragment[CONTENT.length];
    }


    public HealthBaseFragment getFragment(int position) {
        return fragments[position];
    }

    public void refresh(int position,GrowRecordData growRecordData) {
        fragments[position].refresh(growRecordData);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        HealthBaseFragment fragment = (HealthBaseFragment) super.instantiateItem(container, position);
        fragments[position] = fragment;
        return fragment;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return HealthyRecordFragment.getInstance(growRecordData);
            case 1:
                return HeightChartFragment.newInstance(growRecordData);
            case 2:
                return WeightChartFragment.newInstance(growRecordData);
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return CONTENT[position % CONTENT.length].toUpperCase();
    }

    @Override
    public int getCount() {
        return CONTENT.length;
    }


}
