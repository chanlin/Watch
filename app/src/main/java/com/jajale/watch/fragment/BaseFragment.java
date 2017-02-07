package com.jajale.watch.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jajale.watch.BaseApplication;
import com.jajale.watch.R;
import com.jajale.watch.dao.SqliteHelper;
import com.jajale.watch.entitydb.SmartWatch;
import com.jajale.watch.entitydb.User;

import java.util.List;

/**
 * Created by athena on 2015/11/13.
 * Email: lizhiqiang@bjjajale.com
 */
public class BaseFragment extends Fragment {


    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            User user = (User) savedInstanceState.getSerializable("User_data");
            BaseApplication.setUserInfo(user);
//            SqliteHelper sqliteHelper = new SqliteHelper(user.userID);
//            BaseApplication.setBaseHelper(sqliteHelper);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_base, container, false);
    }

    public boolean onBackPressed() {
        return false;
    }


    public boolean fragmentOnActivityResult(int requestCode, int resultCode, Intent data) {
        return false;
    }


    public void homeOnResume() {
    }
    public void refreshWatchList(List<SmartWatch> watches) {

    }

    /**
     * 相当于Fragment接收homeActivity指令
     */
    public void homeGetSocketCode(String code) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("User_data", BaseApplication.getUserInfo());
    }


}
