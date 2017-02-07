package com.jajale.watch.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jajale.watch.R;
import com.jajale.watch.adapter.TabPageIndicatorAdapter;
import com.jajale.watch.entity.QaEnum;
import com.viewpagerindicator.TabPageIndicator;

public class IndexPerSchoolFragment extends Fragment {

    private static String[] title = {"趣味数学", "成长常识", "看图识字", "语音辨别"};
    private static String[] args = {QaEnum.math + "", QaEnum.grown + "", QaEnum.look + "", QaEnum.voice + ""};
    private ViewPager viewPager;
    private TabPageIndicator indicator;
    private TabPageIndicatorAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index_perschool, null);
//		initFragments();
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        indicator = (TabPageIndicator) view.findViewById(R.id.indicator);
        adapter = new TabPageIndicatorAdapter(getFragmentManager(), title, args);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

                if (arg0 == 3)
                    adapter.play(arg0);

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        return view;
    }
}