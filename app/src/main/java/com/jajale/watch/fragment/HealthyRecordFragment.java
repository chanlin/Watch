package com.jajale.watch.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.R;
import com.jajale.watch.activity.AddGrowRecordActivity;
import com.jajale.watch.activity.GrowActivity;
import com.jajale.watch.adapter.HealthyRecordAdapter;
import com.jajale.watch.entity.GrowRecordData;
import com.jajale.watch.entitydb.GrowRecord;
import com.jajale.watch.entitydb.SmartWatch;
import com.jajale.watch.interfaces.CreateSuccessInterface;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.listener.SimpleClickListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.DialogUtils;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 健康记录
 * Created by chunlongyuan on 11/23/15.
 */
public class HealthyRecordFragment extends HealthBaseFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public static final String TAG = "HealthyRecordFragment";
    private CreateSuccessInterface createSuccessInterface;
    public LoadingDialog loadingDialog;
    private ListView listView;
    private HealthyRecordAdapter adapter;
    //    private PtrClassicFrameLayout mPtrFrame;
    public SmartWatch watch;
    private int pageIndex = 1;
    private int pageSize = 12;

    //----------------single instance start-----------------//
    private static HealthyRecordFragment instance = null;
    private View tv_no_data;

    private GrowRecordData recordData;
    OnArticleSelectedListener mListener;

    public interface OnArticleSelectedListener {
        public void onRefresh();
    }

    public HealthyRecordFragment() {
    }

    public static HealthyRecordFragment getInstance(GrowRecordData recordData) {
        HealthyRecordFragment fragment = new HealthyRecordFragment();
        fragment.recordData = recordData;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grow_record, container, false);
        L.e(TAG + "---onCreateView");
        if (createSuccessInterface != null) {
            createSuccessInterface.createSuccess();
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.watch = ((GrowActivity) getActivity()).getCurrentWatch();
        initView();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnArticleSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        L.e(TAG + "---onDetach");
    }

    @Override
    public void onPause() {
        super.onPause();
        L.e(TAG + "---onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.e(TAG + "---onDestroy");
    }

    private void initView() {
        loadingDialog = new LoadingDialog(getActivity());

        listView = (ListView) getView().findViewById(R.id.lv_msg);

        tv_no_data = getView().findViewById(R.id.tv_no_data);
        adapter = new HealthyRecordAdapter(getActivity());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);


        update();
    }


    private void resetNoData() {
        listView.setVisibility(View.VISIBLE);
        tv_no_data.setVisibility(View.GONE);
    }

    private void update() {

        if (!CMethod.isNet(getActivity())) {
            showNoData();
            return;
        }
        resetNoData();
        if (recordData.growthList.size()==0){
            showNoData();
        }

        adapter.loadNextPage(recordData);

    }

    private void showNoData() {
        listView.setVisibility(View.GONE);
        tv_no_data.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        L.e("position is :" + position);
        if (position < adapter.getCount())
            watch = ((GrowActivity) getActivity()).getCurrentWatch();
        if (watch.getIs_manage() == 1) {
            final GrowRecord record = adapter.getItem(position);
            Intent intent = new Intent(getActivity(), AddGrowRecordActivity.class);
            intent.putExtra(AddGrowRecordActivity.EXTRA_RECORD, record);
            intent.putExtra(AddGrowRecordActivity.EXTRA_WATCH, watch);
            getActivity().startActivityForResult(intent, AddGrowRecordActivity.TO_ADD_GROW_RECORD);
        } else {
            T.s("没有管理员权限");
        }
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        L.e("onLongClick position is :" + position);
        DialogUtils.deleteGrowRecordDialog(getActivity(), new SimpleClickListener() {
            @Override
            public void ok() {
                L.e("delete position is " + position);
                watch = ((GrowActivity) getActivity()).getCurrentWatch();
                if (watch.getIs_manage() == 1) {
                    loadingDialog.show("请稍后...", AppConstants.MaxIndex);
                    GrowRecord data = adapter.getItem(position);
                    deteteHealthRecord(BaseApplication.getUserInfo().userID, watch.getUser_id(), data.growth_id);
                } else {
                    T.s("没有管理员权限");
                }

            }

            @Override
            public void cancle() {
            }
        });
        return true;
    }


    private void deteteHealthRecord(String user_id, String friend_id, String growth_id) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", user_id);
            jsonObject.put("friend_id", friend_id);
            jsonObject.put("growth_id", growth_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API,AppConstants.JAVA_DEL_BABY_GROWTH_URL, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                T.s("删除成功");
                loadingDialog.dismissAndStopTimer();
                mListener.onRefresh();
            }

            @Override
            public void onFailure(String result) {
                loadingDialog.dismissAndStopTimer();
                T.s(result);
            }

            @Override
            public void onError() {
                loadingDialog.dismissAndStopTimer();
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        setUserVisibleHint(true);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void refresh(GrowRecordData growRecordData) {

        if (growRecordData.growthList.size() == 0) {
            showNoData();
        } else {
            resetNoData();
            adapter.refresh(growRecordData);
        }

    }
}
