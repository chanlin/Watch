package com.jajale.watch.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jajale.watch.R;
import com.jajale.watch.fragment.AQYFragment;
import com.jajale.watch.fragment.XiMaLaYaFragment;

/**
 * Created by llh on 16-5-23.
 */
public class SeeTheWordFragmentAdapter extends FragmentStatePagerAdapter {
    private static final String[] CONTENT = new String[2];

    public SeeTheWordFragmentAdapter(Context context ,FragmentManager fm) {
        super(fm);
        CONTENT[0] = context.getString(R.string.see_the_word_one);
        CONTENT[1] = context.getString(R.string.see_the_word_two);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return XiMaLaYaFragment.newInstance();

            case 1:
                return AQYFragment.newInstance();
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
