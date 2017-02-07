package com.jajale.watch.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseActivity;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.IntentAction;
import com.jajale.watch.R;
import com.jajale.watch.adapter.FamilyMemberListAdapter;
import com.jajale.watch.dialog.FamilySettingDialog;
import com.jajale.watch.entity.FamilyMemberData;
import com.jajale.watch.entity.SmartWatchListData;
import com.jajale.watch.entitydb.FamilyMember;
import com.jajale.watch.entitydb.SmartWatch;
import com.jajale.watch.listener.FamilyDialogListener;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.DialogUtils;
import com.jajale.watch.utils.FamilyMemberUtils;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.UrlAddressUrils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 宝贝家庭成员
 * <p/>
 * Created by lilonghui on 2015/11/26.
 * Email:lilonghui@bjjajale.com
 */
public class FamilyMemberActivity extends BaseActivity implements View.OnClickListener, FamilyDialogListener {


    private SmartWatch watch;
    @InjectView(R.id.iv_left)
    ImageView ivLeft;
    @InjectView(R.id.tv_left_2)
    TextView tvLeft2;
    @InjectView(R.id.tv_middle)
    TextView tvMiddle;
    @InjectView(R.id.tv_right)
    TextView tvRight;
    @InjectView(R.id.iv_right)
    ImageView ivRight;
    @InjectView(R.id.family_member_iv_head)
    ImageView familyMemberIvHead_baby;
    @InjectView(R.id.family_member_iv_name)
    TextView familyMemberIvName_baby;
    @InjectView(R.id.family_member_iv_phone)
    TextView familyMemberIvPhone_baby;
    @InjectView(R.id.family_member_lv_content)
    ListView familyMemberLvContent;
    @InjectView(R.id.line)
    View line;
    private LoadingDialog loadingDialog;
    private FamilyMemberListAdapter adapter;
    private String userID;

    boolean isManager = false;
    private FamilyMember entity_familyMember;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String baby_name, baby_phone, baby_sex, user_id, relation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_family_member);
        ButterKnife.inject(this);
        tvMiddle.setText(getResources().getString(R.string.family_member_title_text));
        ivLeft.setImageResource(R.drawable.title_goback_selector);
        ivLeft.setOnClickListener(this);
        //设置右边按钮，为添加新家长
        ivRight.setImageResource(R.mipmap.add_family_member);
        ivRight.setOnClickListener(this);
        ivRight.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
        loadingDialog = new LoadingDialog(this);

        watch = getIntent().getParcelableExtra(SmartWatchListData.KEY);//获取intent传递过来的数据
//        userID = BaseApplication.getUserInfo().userID;

        sp = getApplicationContext().getSharedPreferences("NotDisturb", getApplicationContext().MODE_PRIVATE);// 创建对象
        editor = sp.edit();

        user_id = sp.getString("Telephone_Number", "");
        baby_name = sp.getString("nick_name", "");
        baby_phone = sp.getString("phone", "");
        baby_sex = sp.getString("sex", "");
        if (!"".equals(baby_name) && !"".equals(baby_phone)) {//宝贝信息
            familyMemberIvName_baby.setText(baby_name);
            familyMemberIvPhone_baby.setText(baby_phone);
        }
        if (baby_sex.equals("0")) {
            familyMemberIvHead_baby.setImageResource(R.mipmap.head_image_girl);
        } else {
            familyMemberIvHead_baby.setImageResource(R.mipmap.head_image_boy);
        }
        //获取家长列表
        getFamilyMemberListFromNetWork();


    }

    @Override
    protected void onResume() {
        super.onResume();
//        refreshDB();
        getFamilyMemberListFromNetWork();
    }


    private void refreshDB() {
        List<FamilyMember> familyMemberList = FamilyMemberUtils.getFamilyMemberListFromDB(watch.getUser_id());
        if (familyMemberList != null && familyMemberList.size() != 0) {
            setFamilyMemberListView(familyMemberList);
        } else {
            getFamilyMemberListFromNetWork();
        }
    }

    //家庭成员列表
    private void setFamilyMemberListView(final List<FamilyMember> familyList) {

        if (adapter != null && adapter.mFamilyList.size() != 0) {
            adapter.mFamilyList = familyList;
            adapter.notifyDataSetChanged();
        } else {
            adapter = new FamilyMemberListAdapter(this, familyList);
            familyMemberLvContent.setAdapter(adapter);
        }

        familyMemberLvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (CMethod.isFastDoubleClick()) {
                    return;
                }

                entity_familyMember = (FamilyMember) adapter.getItem(position);


                if (entity_familyMember.getUser_id().equals(user_id)) {//本人
                    DialogUtils.familyMemberSetting(FamilyMemberActivity.this, FamilySettingDialog.FAMILY_TYPE_TWO, FamilyMemberActivity.this);
                } else {//其他人

                    if (isManager)//我是管理员
                    {
                        DialogUtils.familyMemberSetting(FamilyMemberActivity.this, FamilySettingDialog.FAMILY_TYPE_ONE, FamilyMemberActivity.this);
                    }
                }
            }
        });

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
            case R.id.iv_right://添加新家长
//                Intent intent = new Intent(FamilyMemberActivity.this, AddFamilyMemberActivity.class);
//                intent.putExtra(IntentAction.KEY_IMEI_CODE, watch.getWatch_imei_binded());
//                startActivity(intent);
                break;
        }
    }


    /**
     * java获取家庭成员列表
     */
    private void getFamilyMemberListFromNetWork() {

        relation = sp.getString("relation", "");
        List<FamilyMember> familyMembers = new ArrayList<FamilyMember>();
        FamilyMember family = new FamilyMember();
        family.setWatchId("1");
        family.setId("2");
        family.setPhone(user_id);
        family.setIsManage(1);//管理员
        family.setUser_id(user_id);
        family.setRelation(relation);
        familyMembers.add(family);

        setFamilyMemberListView(familyMembers);
    }

    /**
     * java移交管理员权限
     */
    private void grantedManagerPermissions() {
        loadingDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", BaseApplication.getUserInfo().userID);
            jsonObject.put("target_id", entity_familyMember.getUser_id());
            jsonObject.put("friend_id", watch.getUser_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API,AppConstants.JAVA_MOVE_MANAGE_URL, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                loadingDialog.dismiss();
                isManager = !isManager;//管理权限关闭
                T.s("操作成功！");
                sendRefreshWatchListReceiver();
                getFamilyMemberListFromNetWork();
            }
            @Override
            public void onFailure(String result) {
                loadingDialog.dismiss();
                T.s(result);
            }
            @Override
            public void onError() {
                loadingDialog.dismiss();
            }
        });
    }




    /**
     * java删除管理员
     */
    private void removeOtherFamilyMember() {
        loadingDialog.show();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("friend_id", watch.getUser_id());//手表ID
            jsonObject.put("user_id", userID);//当前执行操作的用户ID
            jsonObject.put("unbind_id", entity_familyMember.getUser_id());//解除绑定的用户ID
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.PostDataToWeb(UrlAddressUrils.CODE_API,AppConstants.JAVA_DELETE_RELATION_URL, jsonObject, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                loadingDialog.dismiss();
                FamilyMemberUtils.deleteWatch(entity_familyMember);//本地数据库删除该成员信息
                refreshDB();//刷新列表
            }
            @Override
            public void onFailure(String result) {
                loadingDialog.dismiss();
                T.s(result);
            }
            @Override
            public void onError() {
                loadingDialog.dismiss();
            }
        });
    }



    /**
     * dialog 编辑关系
     */
    @Override
    public void onEditRelation() {

        Intent intent = new Intent(FamilyMemberActivity.this, FamilySelectActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(SmartWatchListData.KEY, watch);//传递宝贝信息
        intent.putExtra("isFromFamilyMember", true);
        intent.putExtras(mBundle);
        startActivity(intent);

    }

    /**
     * dialog 设为管理员
     */
    @Override
    public void onManger() {
//        grantedManagerPermissions();
    }

    /**
     * dialog 删除管理员
     */
    @Override
    public void onDelete() {
//        removeOtherFamilyMember();
    }

    /**
     * dialog 取消
     */
    @Override
    public void cancle() {

    }
}
