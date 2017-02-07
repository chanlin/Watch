package com.jajale.watch.fragment;

import android.os.Bundle;

import com.jajale.watch.activity.GrowActivity;
import com.jajale.watch.entity.GrowChart;
import com.jajale.watch.entity.GrowRecordData;
import com.jajale.watch.utils.GrowRecordUtils;
import com.jajale.watch.utils.StandardGrowTool;

/**
 * 身高图表
 * Created by chunlongyuan on 12/8/15.
 */
public class HeightChartFragment extends LineChartFragment {

    private static GrowChart heightGrowChart;
    private GrowRecordData recordData;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        GrowActivity growActivity = (GrowActivity) getActivity();
        StandardGrowTool standardGrowTool = new StandardGrowTool();
        if (growActivity.currentWatch != null)
            heightGrowChart = standardGrowTool.getHeightGrowChart(growActivity.currentWatch.getSex()==1);
        else
            getActivity().finish();
        super.onCreate(savedInstanceState);
    }

    public static LineChartFragment newInstance(GrowRecordData recordData) {
        HeightChartFragment fragment = new HeightChartFragment();
        fragment.recordData=recordData;
        return fragment;
    }


    @Override
    int [] getMydatas() {
        if (recordData!=null&&recordData.growthList!=null&&recordData.growthList.size()>0)
        {
            return GrowRecordUtils.getMyArrayHeightRecord(recordData.growthList);
        }
        return new int[0];
    }
    @Override
    int[] getMyMonths() {
        if (recordData!=null&&recordData.growthList!=null&&recordData.growthList.size()>0)
        {
            return GrowRecordUtils.getMyArrayMonthRecord(recordData.growthList);
        }
        return new int[0];
    }
    @Override
    int[] getMonths() {
        return heightGrowChart.month;
    }
    @Override
    String[] getAges() {
        return heightGrowChart.ages;
    }

    @Override
    int[] getUpers() {
        return heightGrowChart.upers;
    }

    @Override
    int[] getStandards() {
        return heightGrowChart.standards;
    }

    @Override
    int[] getLowers() {
        return heightGrowChart.lowers;
    }

    @Override
    String getIndicate() {
        return "身高";
    }

    @Override
    String getHighText() {
        return "偏高";
    }

    @Override
    String getLowtext() {
        return "偏低";
    }

    @Override
    String unit() {
        return "cm";
    }

    @Override
    public void refresh(GrowRecordData growRecordData) {
        if (growRecordData!=null&&growRecordData.growthList!=null)
        {
            refreshMydata(GrowRecordUtils.getMyArrayMonthRecord(growRecordData.growthList),GrowRecordUtils.getMyArrayHeightRecord(growRecordData.growthList));
        }
    }
}
