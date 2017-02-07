package com.jajale.watch.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jajale.watch.BroadcastConstants;
import com.jajale.watch.R;
import com.jajale.watch.activity.ChildMsgActivity;
import com.jajale.watch.activity.SystemMsgActivity;
import com.jajale.watch.adapter.ContactAdapter;
import com.jajale.watch.entitydb.MsgMember;
import com.jajale.watch.helper.GetMessageHelper;
import com.jajale.watch.interfaces.CreateSuccessInterface;
import com.jajale.watch.listener.GetMessageListener;
import com.jajale.watch.message.MySocketUtils;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.DisplayUtil;
import com.jajale.watch.utils.FileUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.MessageUtils;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;



/**
 * Created by athena on 2015/11/13.
 * Email: lizhiqiang@bjjajale.com
 */
public class MsgFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final String TAG = "MsgFragment";
    private CreateSuccessInterface createSuccessInterface;
    public LoadingDialog loadingDialog;
    private ListView listView;
    private ContactAdapter mAdapter;
    private PtrClassicFrameLayout mPtrFrame;

    private int clickIndex = 0;

    //----------------single instance start-----------------//
    private static MsgFragment instance = null;

    public MsgFragment() {

    }

    public static MsgFragment getInstance() {
        MsgFragment fragment = new MsgFragment();
        return fragment;
    }

    //----------------single instance end  -----------------//
    private class WaitTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
            refreshListView();

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.e(TAG + "---onCreate");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastConstants.ACTION_MESSAGE_UPDATA);
        getActivity().registerReceiver(updateReceiver, intentFilter);
        if (getArguments() != null) {
        }
    }

    private MySocketUtils socketUtils;

    private BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastConstants.ACTION_MESSAGE_UPDATA)) {
                new WaitTask().execute();
            }

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(updateReceiver);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message, container, false);
        L.e("MSG  on view crteate");
        L.e(TAG + "---onCreateView");
        if (createSuccessInterface != null) {
            createSuccessInterface.createSuccess();
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        L.e(TAG + "---onViewCreated");
        // Title 文字
        View title = view.findViewById(R.id.title);
        TextView midTitle = (TextView) title.findViewById(R.id.tv_middle);
        midTitle.setText(getResources().getString(R.string.function_name_msg));


    }

    @Override
    public void onResume() {
        super.onResume();
        refreshListView();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        refreshListView();

    }

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    private void refreshListView() {
        loadingDialog.dismiss();
        sp = getActivity().getSharedPreferences("NotDisturb", getActivity().MODE_PRIVATE);// 创建对象
        editor = sp.edit();
        List<MsgMember> watches = new ArrayList<MsgMember>();
        MsgMember msg = new MsgMember();
        msg.setUser_id("1");//user_id
        msg.setHeader_img_url("");//头像地址
        msg.setUser_identity(2);//用户分类 系统1 默认用户2
        msg.setNick_name(sp.getString("nick_name", ""));//昵称
        msg.setSex("0".equals(sp.getString("sex", ""))?0:1);
        msg.setRelation(sp.getString("relation", ""));
        msg.setCount_msg(0);//手表用户消息总数
        msg.setCount_unread(0);//手表用户未读消息数
        msg.setWatch_imei_binded("");//帮定的 watch id
        msg.setUpdate_time("16:01:17");//手表用户数据更新时间
        msg.setLast_msg_desc(getResources().getString(R.string.new_message));//最后一封消息的描述

        watches.add(msg);
        if (watches != null && watches.size() != 0) {
            if (mAdapter != null) {
                if (listView.getAdapter() == null) {
                    listView.setAdapter(mAdapter);
                }
                mAdapter.refresh(watches);
            }
        }

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
        if (CMethod.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_right:
                //L.e("保存被点击");
                break;

        }
    }

    private void initView() {
        loadingDialog = new LoadingDialog(getActivity());
        listView = (ListView) getView().findViewById(R.id.lv_msg);
        mAdapter = new ContactAdapter(getActivity());
        listView.setOnItemClickListener(this);
        mPtrFrame = (PtrClassicFrameLayout) getView().findViewById(R.id.rotate_header_list_view_frame);
//        mPtrFrame.setLastUpdateTimeRelateObject(this);
        final MaterialHeader header=new MaterialHeader(getActivity());
        int[] colors = getResources().getIntArray(R.array.material_colors);
        header.setColorSchemeColors(colors);
        header.setPtrFrameLayout(mPtrFrame);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, DisplayUtil.designedDP2px(15),0,DisplayUtil.designedDP2px(10));
        mPtrFrame.setDurationToClose(100);
        mPtrFrame.setPinContent(true);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                GetMessageHelper helper = new GetMessageHelper(getActivity(), new GetMessageListener() {
                    @Override
                    public void onSuccess(String userinfoJson) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                mPtrFrame.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        update();
                                        mPtrFrame.refreshComplete();

                                    }
                                }, 2000);
                            }
                        }).start();

                    }

                    @Override
                    public void onFailure() {
                        mPtrFrame.refreshComplete();
                    }
                });
                helper.getMessage();
            }
        });
        // the following are default settings
        mPtrFrame.setResistance(2.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(100);
        // default is false
        mPtrFrame.setPullToRefresh(false);
        // default is true
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh();
            }
        },100);


    }


    private void update() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadingDialog.show();
                refreshListView();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        L.e("position is :" + position);
        if (!CMethod.isFastDoubleClick()) {
            MsgMember msgMember = (MsgMember) mAdapter.getItem(position);
            loadingDialog.show();
            updateChild(msgMember);
            openChatActivity(msgMember);
        }

    }

    private void updateChild(MsgMember msgMember) {
        msgMember.setCount_unread(0);
        MessageUtils.updateMsgMember(msgMember);
    }


    private void openChatActivity(final MsgMember msgMember) {
        Intent intent = new Intent();
        if (msgMember.getUser_identity() == 1) {
            intent.setClass(getActivity(), SystemMsgActivity.class);
        } else {
            intent.setClass(getActivity(), ChildMsgActivity.class);
        }
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(MsgMember.KEY, msgMember);//传递宝贝信息
        intent.putExtras(mBundle);
        intent.putExtra("from_tag", "msgcenter");
        loadingDialog.dismiss();
        startActivity(intent);


    }

}
