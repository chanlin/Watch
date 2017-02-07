package com.jajale.watch.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseActivity;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.IntentAction;
import com.jajale.watch.R;
import com.jajale.watch.adapter.FamilyMemberHeadGirdViewAdapter;
import com.jajale.watch.entity.SmartWatchListData;
import com.jajale.watch.entitydb.DbHelper;
import com.jajale.watch.entitydb.FamilyMember;
import com.jajale.watch.entitydb.SmartWatch;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.listener.SingleStringListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.DialogUtils;
import com.jajale.watch.utils.FamilyMemberUtils;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.LastLoginUtils;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.ParentHeadViewUtils;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * 和宝贝的关系选择
 * <p/>
 * Created by lilonghui on 2015/11/17.
 * Email:lilonghui@bjjajale.com
 */
public class FamilySelectActivity extends BaseActivity implements View.OnClickListener {


    private LoadingDialog loadingDialog;
    private boolean isFromFamilyMember;
    private SmartWatch watch;
    private FamilyMemberHeadGirdViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_familyselect);

        //判断是否从家庭成员进入
        isFromFamilyMember = getIntent().getBooleanExtra("isFromFamilyMember", false);
        watch = getIntent().getParcelableExtra(SmartWatchListData.KEY);
        initTitleView();
        loadingDialog = new LoadingDialog(this);
        GridView gridView = (GridView) findViewById(R.id.family_select_gb);
        adapter = new FamilyMemberHeadGirdViewAdapter(this);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (CMethod.isFastDoubleClick()) {
                    return;
                }

                if (position == getResources().getStringArray(R.array.family_member_relation).length - 1) {
                    DialogUtils.editRelation(FamilySelectActivity.this, new SingleStringListener() {
                        @Override
                        public void choiced(String result) {
//                            String userID = BaseApplication.getUserInfo().userID;
//                            editFamilyRelation(userID, watch.getUser_id(), result);
                            selecSuccess("", result);
                        }
                    });
                } else {
                    ImageView family_member_iv_head = (ImageView) view.findViewById(R.id.family_member_iv_head);
                    family_member_iv_head.setImageResource(ParentHeadViewUtils.mHeadView_press[position]);

//                    String userID = BaseApplication.getUserInfo().userID;

                    if (CMethod.isNet(FamilySelectActivity.this)) {
//                        editFamilyRelation(userID, watch.getUser_id(), ParentHeadViewUtils.mName[position]);
                        selecSuccess("", getResources().getStringArray(R.array.family_member_relation)[position]);
                    } else {
                        T.s("网络不给力");
                    }


                }
            }
        });
    }


    /**
     * 初始化title view
     */
    private void initTitleView() {
        View title = findViewById(R.id.title);
        TextView midTitle = (TextView) title.findViewById(R.id.tv_middle);
        midTitle.setText(getResources().getString(R.string.family_select_title_text));
        //当非家庭成员进入，则取消返回键
        if (isFromFamilyMember) {
            ImageView iv_left = (ImageView) title.findViewById(R.id.iv_left);
            iv_left.setImageResource(R.drawable.title_goback_selector);
            iv_left.setOnClickListener(this);
        }

    }


    @Override
    public void onClick(View v) {
        if (CMethod.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {

            case R.id.iv_left://返回键
                finish();
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isFromFamilyMember) {
                finish();
            } else {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * JAVA编辑成员关系
     */
    private void editFamilyRelation(String userID, final String watchID, final String relation) {
        loadingDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", userID);
            jsonObject.put("friend_id", watchID);
            jsonObject.put("relation", relation);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API, AppConstants.JAVA_INSERT_RELATION_URL, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                loadingDialog.dismiss();
                selecSuccess(watchID, relation);
            }

            @Override
            public void onFailure(String result) {
                loadingDialog.dismiss();
                adapter.mHeadView = ParentHeadViewUtils.mHeadView;
                adapter.notifyDataSetChanged();
                T.s(result);
            }

            @Override
            public void onError() {
                loadingDialog.dismiss();
                adapter.mHeadView = ParentHeadViewUtils.mHeadView;
                adapter.notifyDataSetChanged();
            }
        });


    }


    /**
     * 编辑成员关系
     */
//    private void editFamilyRelation(String userID, final int watchID, final String relation) {
//        loadingDialog.show("请稍后...");
//        try {
//
//            String url = AppConstants.POST_URL;
//
//            JSONObject paramJO = new JSONObject();
//            paramJO.put("code", CodeConstants.EDIT_FAMILY_RELATION_CODE);
//
//            JSONObject dataObj = new JSONObject();
//            dataObj.put("userID", userID);
//            dataObj.put("watchID", watchID);
//            dataObj.put("relation", relation);
//            paramJO.put("data", dataObj);
//
//
//            RequestManager.getInstance().addRequest(new SimpleStringRequest(getApplicationContext(), url, paramJO, new SimpleResponseListener() {
//
//
//                @Override
//                public void onSuccess(String url, String result) {
//                    selecSuccess(watchID, relation);
////                    L.e("family_select_onSuccess" + result);
////                    LastLoginUtils lastLoginUtils=new LastLoginUtils(FamilySelectActivity.this);
////                    PhoneBookUtils utils=new PhoneBookUtils(FamilySelectActivity.this, watch, lastLoginUtils.getPhone(), relation, new PhoneBookListener() {
////                        @Override
////                        public void onFinish() {
////                            selecSuccess(watchID, relation);
////                        }
////                    });
////                    utils.editPhoneBook();
//                }
//
//                @Override
//                public void onFailure(String url, SimpleResult response) {
//                    L.e("family_select_onFailure" + response.getMessage());
//                    loadingDialog.dismiss();
//                    adapter.mHeadView = ParentHeadViewUtils.mHeadView;
//                    adapter.notifyDataSetChanged();
//                    Toast.makeText(FamilySelectActivity.this, response.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onError(String url, SimpleResult result) {
//                    L.e("family_select_onError" + "error result ");
//                    adapter.mHeadView = ParentHeadViewUtils.mHeadView;
//                    adapter.notifyDataSetChanged();
//                    loadingDialog.dismiss();
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    L.e("onErrorResponse" + "error onErrorResponse ");
//                    adapter.mHeadView = ParentHeadViewUtils.mHeadView;
//                    adapter.notifyDataSetChanged();
//                    loadingDialog.dismiss();
//                }
//            }), this);
//        } catch (Exception e) {
//
//            loadingDialog.dismiss();
//        }
//
//    }
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private void selecSuccess(String watchID, String relation) {
        loadingDialog.dismiss();
        //watch数据库添加Relation


        //isFromFamilyMember是判断从哪里进来的（从家庭成员进来），从添加手表页面进来，则直接finish，从首页进来，则进入HomeActivity
        //如果该手表有编辑信息，则直接进入HomeAcivity，反之，则进入BabyInfoActivity
        if (isFromFamilyMember) {
            //FamilyMember数据库添加Relation

//            DbHelper<FamilyMember> dbHelper_familyMember = new DbHelper<FamilyMember>(BaseApplication.getBaseHelper(), FamilyMember.class);
//            HashMap<String, Object> hashMap = new HashMap<String, Object>();
//            LastLoginUtils lastLoginUtils = new LastLoginUtils(FamilySelectActivity.this);
//            hashMap.put(FamilyMember.ID, this.watch.getUser_id() + "_" + lastLoginUtils.getPhone());
//            List<FamilyMember> familyMembers = dbHelper_familyMember.queryForEq(hashMap);
//
//            if (familyMembers != null && familyMembers.size() > 0) {
//                FamilyMember familyMember = familyMembers.get(0);
//                familyMember.setRelation(relation);
//                FamilyMemberUtils.updateFamilyMemberData(familyMember);
//            }
            sp = getApplicationContext().getSharedPreferences("NotDisturb", getApplicationContext().MODE_PRIVATE);// 创建对象
            editor = sp.edit();
            editor.putString("relation", relation);
            editor.commit();

            sendRefreshWatchListReceiver();

        } else if (this.watch.getIs_manage() == 1 && this.watch.getPhone_num_binded().equals("")) {
            Intent intent = new Intent(FamilySelectActivity.this, BabyInfoActivity.class);
            intent.putExtra(IntentAction.KEY_WATCH_ID, watchID);
            startActivity(intent);
        } else {
            Intent intent = new Intent(FamilySelectActivity.this, HomeSecActivity.class);
            intent.putExtra(IntentAction.OPEN_HONE_FROM, "familyselect");
//            intent.putExtra(IntentAction.OPEN_HONE_ID,username);
//            intent.putExtra(IntentAction.OPEN_HONE_PW,password);
            startActivity(intent);
        }
//        HomeSecActivity.isChangeWatchData = true;//HomeActivity标记是否去判断实名认证
        finish();

    }


}
