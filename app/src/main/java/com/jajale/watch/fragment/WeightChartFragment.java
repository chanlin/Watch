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
public class WeightChartFragment extends LineChartFragment {

    private static GrowChart weightGrowChart;
    private GrowRecordData recordData;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        GrowActivity growActivity = (GrowActivity) getActivity();
        StandardGrowTool standardGrowTool = new StandardGrowTool();
        weightGrowChart = standardGrowTool.getWeightGrowChart(growActivity.currentWatch.getSex()==1);
        super.onCreate(savedInstanceState);
    }

    public static LineChartFragment newInstance(GrowRecordData recordData) {
        WeightChartFragment fragment = new WeightChartFragment();
        fragment.recordData=recordData;
        return fragment;
    }
    @Override
    int [] getMydatas() {

        if (recordData!=null&&recordData.growthList!=null&&recordData.growthList.size()>0)
        {
            return GrowRecordUtils.getMyArrayWeightRecord(recordData.growthList);
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
        return weightGrowChart.month;
    }
    @Override
    String[] getAges() {
        return weightGrowChart.ages;
    }

    @Override
    int[] getUpers() {
        return weightGrowChart.upers;
    }

    @Override
    int[] getStandards() {
        return weightGrowChart.standards;
    }

    @Override
    int[] getLowers() {
        return weightGrowChart.lowers;
    }

    @Override
    String getIndicate() {
        return "体重";
    }

    @Override
    String getHighText() {
        return "偏胖";
    }

    @Override
    String getLowtext() {
        return "偏瘦";
    }

    @Override
    String unit() {
        return "kg";
    }

    @Override
    public void refresh(GrowRecordData growRecordData) {
        if (growRecordData!=null&&growRecordData.growthList!=null&&growRecordData.growthList.size()>0)
        {
            refreshMydata(GrowRecordUtils.getMyArrayMonthRecord(growRecordData.growthList),GrowRecordUtils.getMyArrayWeightRecord(growRecordData.growthList));
        }

    }
}
