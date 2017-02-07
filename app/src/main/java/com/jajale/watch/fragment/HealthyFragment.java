package com.jajale.watch.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.activity.GrowActivity;
import com.jajale.watch.activity.VaccineActivity;
import com.jajale.watch.interfaces.CreateSuccessInterface;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;

/**
 * Created by athena on 2015/11/13.
 * Email: lizhiqiang@bjjajale.com
 */
public class HealthyFragment extends BaseFragment implements View.OnClickListener {

    public static final String TAG = "HealthyFragment";
    private CreateSuccessInterface createSuccessInterface;
    private LoadingDialog loadingDialog;

    //----------------single instance start-----------------//
    private static HealthyFragment instance = null;

    public HealthyFragment() {
    }

    public static HealthyFragment getInstance() {
        HealthyFragment fragment = new HealthyFragment();
        return fragment;
    }
    //----------------single instance end  -----------------//

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.e(TAG + "---onCreate");
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_healthy, container, false);
        L.e(TAG + "---onCreateView");
        if (createSuccessInterface != null) {
            createSuccessInterface.createSuccess();
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingDialog = new LoadingDialog(getActivity());

        L.e("Healthy  on view crteate");
        L.e(TAG + "---onViewCreated");
        // Title 文字
        View title = view.findViewById(R.id.title);
        TextView midTitle = (TextView) title.findViewById(R.id.tv_middle);
        midTitle.setText(getResources().getString(R.string.function_name_helthy));

        getView().findViewById(R.id.btn_grow).setOnClickListener(this);
        getView().findViewById(R.id.btn_vaccine).setOnClickListener(this);
        getView().findViewById(R.id.btn_expect).setOnClickListener(this);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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

    public void setCreateSuccessInterface(CreateSuccessInterface successInterface) {
        this.createSuccessInterface = successInterface;
    }

    @Override
    public boolean fragmentOnActivityResult(int requestCode, int resultCode, Intent data) {
        return super.fragmentOnActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        if (CMethod.isFastDoubleClick()){
            return;
        }
        Intent intent = null;
        switch (v.getId()) {
            case R.id.btn_grow:
                startActivity(new Intent(getActivity(), GrowActivity.class));

                break;
            case R.id.btn_vaccine:
                intent = new Intent(getActivity(), VaccineActivity.class);
                startActivity(intent);

                break;
            case R.id.btn_expect:

                break;
        }
    }
}
