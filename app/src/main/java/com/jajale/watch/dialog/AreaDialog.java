package com.jajale.watch.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jajale.watch.R;
import com.jajale.watch.cviews.AlarmPickerView;
import com.jajale.watch.entityno.Area;
import com.jajale.watch.entityno.BaseArea;
import com.jajale.watch.entityno.City;
import com.jajale.watch.entityno.Province;
import com.jajale.watch.fragment.BaseAreaManager;
import com.jajale.watch.listener.AreaListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by athena on 2015/12/5.
 * Email: lizhiqiang@bjjajale.com
 */
public class AreaDialog extends Dialog implements View.OnClickListener {


    private List<String> areaNames;
    private List<String> cityNames;
    private Context mContext;
    private List<String> provinceNames;
    private AreaListener mListener;
    private AlarmPickerView number_picker_province;
    private AlarmPickerView number_picker_city;
    private AlarmPickerView number_picker_area;


    public AreaDialog(Context context,AreaListener listener) {
        super(context);

        this.mContext = context;
        this.mListener = listener;

        setContentView(R.layout.change_dialog_area);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(params);
        setCanceledOnTouchOutside(true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseArea baseArea = null;
//        try {
//            InputStream is = getContext().getAssets().open("area", 0);
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
//            String areas = bufferedReader.readLine();
//            baseArea = new Gson().fromJson(areas, BaseArea.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        baseArea = BaseAreaManager.getBaseArea(getContext());

        number_picker_province = (AlarmPickerView) findViewById(R.id.number_picker_province);
        number_picker_city = (AlarmPickerView) findViewById(R.id.number_picker_city);
        number_picker_area = (AlarmPickerView) findViewById(R.id.number_picker_area);


//        findViewById(R.id.num_up_province).setOnClickListener(this);
//        findViewById(R.id.num_down_province).setOnClickListener(this);
//        findViewById(R.id.num_up_city).setOnClickListener(this);
//        findViewById(R.id.num_down_city).setOnClickListener(this);
//        findViewById(R.id.num_up_area).setOnClickListener(this);
//        findViewById(R.id.num_down_area).setOnClickListener(this);

        findViewById(R.id.btn_ok).setOnClickListener(this);

        provinceNames = new ArrayList<String>();
        proList = baseArea.provinces;
        for (Province province : baseArea.provinces) {
            provinceNames.add(province.name);
        }
//        String reuslt_p ="";
//        if(!CMethod.isEmpty(AppInfo.getInstace().getCity())){
//            reuslt_p = AppInfo.getInstace().getCity().replace("市","").replace("省","");
//        }
//
//        cityNames = new ArrayList<String>();
//        if(!reuslt_p.equals("")){
//            int index = 1 ;
//            for (int i = 0 ; i < proList.size();i++){
//                L.e(proList.get(i)+"---------------------------"+reuslt_p+"<------------>"+i);
//                if(provinceNames.get(i).equals(reuslt_p)){
//                    index = i ;
//                    break;
//                }
//            }
//            cityList = proList.get(index).cityList;
//        }else{
//            cityList = proList.get(proList.size() / 2).cityList;
//        }


        cityNames = new ArrayList<String>();
        cityList = proList.get(proList.size() / 2).cityList;
        for (City city : cityList) {
            cityNames.add(city.name);
        }
        areaList = cityList.get(cityList.size() / 2).areaList;
        areaNames = new ArrayList<String>();
        for (Area area : areaList) {
            areaNames.add(area.name);
        }


        number_picker_province.setData(provinceNames);
        number_picker_city.setData(cityNames);
        number_picker_area.setData(areaNames);
//        if(!reuslt_p.equals("")){
//            number_picker_province.setSelected(reuslt_p);
//        }

        number_picker_province.setOnSelectListener(new AlarmPickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                updateCity();
            }
        });
        number_picker_city.setOnSelectListener(new AlarmPickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                updateArea();
            }
        });
        number_picker_area.setOnSelectListener(new AlarmPickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                String areaName = number_picker_area.getResult();
            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.num_down_province:
//                number_picker_province.next();
//                break;
//            case R.id.num_up_province:
//                number_picker_province.previous();
//                break;
//            case R.id.num_down_city:
//                number_picker_city.next();
//                break;
//            case R.id.num_up_city:
//                number_picker_city.previous();
//                break;
//            case R.id.num_down_area:
//                number_picker_area.next();
//                break;
//            case R.id.num_up_area:
//                number_picker_area.previous();
//                break;
            case R.id.btn_ok:
                ok();
                dismiss();
                break;
        }
    }

    private void ok() {
        String provinceName = number_picker_province.getResult();
        String cityName = number_picker_city.getResult();
        String areaName = number_picker_area.getResult();

        Province province = null;
        for (Province pro : proList) {
            if (provinceName.equals(pro.name)) {
                province = pro;
                break;
            }
        }
        City city = null;
        for (City ci : cityList) {
            if (cityName.equals(ci.name)) {
                city = ci;
                break;
            }
        }
        Area area = null;
        for (Area ar : areaList) {
            if (areaName.equals(ar.name)) {
                area = ar;
                break;
            }
        }
//                T.s("--- "+(province.id+province.name+city.id+city.name+area.id+area.name));
        mListener.ok(province.id, province.name, city.id, city.name, area.id, area.name);
    }

    private List<Province> proList;
    private List<City> cityList;
    private List<Area> areaList;

//    @Override
//    public void onSelect(View view, String text) {
//        switch (view.getId()) {
//            case R.id.number_picker_province:
//                updateCity();
//                break;
//            case R.id.number_picker_city:
//                updateArea();
//                break;
//            case R.id.number_picker_area:
//                String areaName = number_picker_area.getResult();
//                break;
//        }
//    }

    private void updateCity() {
        String provinceName = number_picker_province.getResult();
        int proLen = proList.size();
        for (int i = 0; i < proLen; i++) {
            if (provinceName.equals(proList.get(i).name)) {
                cityList = proList.get(i).cityList;//该省对应的市
                cityNames.clear();
                for (City city : cityList) {
                    cityNames.add(city.name);
                }
                number_picker_city.setData(cityNames);
                updateArea();
                break;
            }
        }
    }

    private void updateArea() {
        String cityName = number_picker_city.getResult();
        int cityLen = cityList.size();
        for (int i = 0; i < cityLen; i++) {
            if (cityName.equals(cityList.get(i).name)) {
                areaList = cityList.get(i).areaList;//该市对应的区
                areaNames.clear();
                for (Area area : areaList) {
                    areaNames.add(area.name);
                }
                number_picker_area.setData(areaNames);
                break;
            }
        }
    }

}
