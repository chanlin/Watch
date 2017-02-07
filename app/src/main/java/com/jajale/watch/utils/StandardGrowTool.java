package com.jajale.watch.utils;

import android.content.Context;

import com.jajale.watch.dao.GrowRecordHelper;
import com.jajale.watch.entity.GrowChart;
import com.jajale.watch.entitydb.DbHelper;
import com.jajale.watch.entitydb.StandardGrow;

import java.io.File;
import java.util.List;

/**
 * 操作标准数据的数据库
 * Created by chunlongyuan on 12/7/15.
 */
public class StandardGrowTool {

    GrowRecordHelper recordHelper;

    public StandardGrowTool() {
        this.recordHelper = new GrowRecordHelper();
    }

    /**
     * 拷贝成长标准数据库
     *
     * @param context
     */
    public static void copyGrowDB(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String target = "/data/data/" + context.getPackageName() + "/databases/grow_record_data";
                File file = new File(target);
                if (!file.exists()) {
                    file.mkdirs();
                    boolean copyAssets = FileUtils.copyAssets(context, "grow_record_data", target);
                    L.d("拷贝成长数据库" + (copyAssets ? "成功" : "失败"));
                }
            }
        }).start();

    }


    /**
     * 获取身高图标数据
     *
     * @param isMale
     * @return
     */
    public GrowChart getHeightGrowChart(boolean isMale) {
        GrowChart growChart = new GrowChart();
        List<StandardGrow> allData = getAllData(isMale);
        int size = allData.size();
        growChart.ages = new String[size];
        growChart.lowers = new int[size];
        growChart.upers = new int[size];
        growChart.standards = new int[size];
        growChart.month = new int[size];
        for (int i = 0; i < allData.size(); i++) {
            StandardGrow standardGrow = allData.get(i);
            growChart.ages[i] = month2Age(standardGrow.month);
            growChart.month[i] =standardGrow.month;
            growChart.lowers[i] = (int) standardGrow.heightLower;
            growChart.upers[i] = (int) standardGrow.heightUper;
            growChart.standards[i] = (int) standardGrow.heightStander;
        }
        return growChart;
    }

    /**
     * 把月份转为年龄
     *
     * @param month
     * @return
     */
    private String month2Age(int month) {
        if (month < 12) {
            return month + "月";
        } else {
            return month / 12 + "岁";
        }
    }

    /**
     * 获取体重图标数据
     *
     * @param isMale
     * @return
     */
    public GrowChart getWeightGrowChart(boolean isMale) {
        GrowChart growChart = new GrowChart();
        List<StandardGrow> allData = getAllData(isMale);
        int size = allData.size();
        growChart.ages = new String[size];
        growChart.lowers = new int[size];
        growChart.upers = new int[size];
        growChart.standards = new int[size];
        growChart.month = new int[size];
        for (int i = 0; i < allData.size(); i++) {
            StandardGrow standardGrow = allData.get(i);
            growChart.ages[i] = month2Age(standardGrow.month);
            growChart.month[i] = standardGrow.month;
            growChart.lowers[i] = (int) standardGrow.weightLower;
            growChart.upers[i] = (int) standardGrow.weightUper;
            growChart.standards[i] = (int) standardGrow.weightStander;
        }
        return growChart;
    }


    public List<StandardGrow> getAllData(boolean isMale) {
        DbHelper<StandardGrow> dbHelper = new DbHelper<StandardGrow>(recordHelper, StandardGrow.class);
        return dbHelper.queryForEq("gender", (isMale ? 1 : 0));
    }

    public static void test() {
        GrowRecordHelper recordHelper = new GrowRecordHelper();
        DbHelper<StandardGrow> dbHelper = new DbHelper<StandardGrow>(recordHelper, StandardGrow.class);
        List<StandardGrow> growRecords = dbHelper.queryForEq("month", 15);
        if (growRecords != null && growRecords.size() > 0) {
            dbHelper.delete(growRecords.get(0));
        }
        growRecords = dbHelper.queryForEq("month", 18);
        if (growRecords != null && growRecords.size() > 0) {
            dbHelper.delete(growRecords.get(0));
        }
        growRecords = dbHelper.queryForEq("month", 21);
        if (growRecords != null && growRecords.size() > 0) {
            dbHelper.delete(growRecords.get(0));
        }
    }

//入库
//    public static void enterDB(Context context) {
//
//        RecordDictionary baseDictionary = BaseRecordDictionary.getBaseDictionary(context);
//
//
//        GrowRecordHelper growRecordHelper = new GrowRecordHelper();
//        DbHelper<StandardGrow> dbHelper = new DbHelper<StandardGrow>(growRecordHelper, StandardGrow.class);
//        List<RecordItem> dictionaryRecord = baseDictionary.dictionary_record;
//
//        for (RecordItem recordItem : dictionaryRecord) {
//            StandardGrow femaleStandardGrow = new StandardGrow();
//            femaleStandardGrow.month = recordItem.month;
//            femaleStandardGrow.gender = 0;
//
//            RecordEntity entity_female = recordItem.entity_female;
//            femaleStandardGrow.heightStander = entity_female.height.stander;
//            femaleStandardGrow.heightLower = entity_female.height.lower;
//            femaleStandardGrow.heightUper = entity_female.height.uper;
//
//            femaleStandardGrow.weightStander = entity_female.weight.stander;
//            femaleStandardGrow.weightLower = entity_female.weight.lower;
//            femaleStandardGrow.weightUper = entity_female.weight.uper;
//
//            dbHelper.createIfNotExists(femaleStandardGrow);
//
//
//
//            StandardGrow maleStandardGrow = new StandardGrow();
//            maleStandardGrow.month = recordItem.month;
//            maleStandardGrow.gender = 1;
//
//            RecordEntity entity_male = recordItem.entity_male;
//            maleStandardGrow.heightStander = entity_male.height.stander;
//            maleStandardGrow.heightLower = entity_male.height.lower;
//            maleStandardGrow.heightUper = entity_male.height.uper;
//
//            maleStandardGrow.weightStander = entity_male.weight.stander;
//            maleStandardGrow.weightLower = entity_male.weight.lower;
//            maleStandardGrow.weightUper = entity_male.weight.uper;
//
//            dbHelper.createIfNotExists(maleStandardGrow);
//
//        }
//    }
}
