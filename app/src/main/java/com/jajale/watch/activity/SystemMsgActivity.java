package com.jajale.watch.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jajale.watch.BaseActivity;
import com.jajale.watch.BroadcastConstants;
import com.jajale.watch.R;
import com.jajale.watch.adapter.SystemMessageAdapter;
import com.jajale.watch.entitydb.Message;
import com.jajale.watch.entitydb.MsgMember;
import com.jajale.watch.entitydb.SmartWatch;
import com.jajale.watch.helper.GetMessageHelper;
import com.jajale.watch.listener.GetMessageListener;
import com.jajale.watch.listener.MessageCommandListener;
import com.jajale.watch.listener.SimpleClickListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.DialogUtils;
import com.jajale.watch.utils.DisplayUtil;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.MessageUtils;
import com.jajale.watch.utils.NotificationUtils;
import com.jajale.watch.utils.ThreadUtils;

import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

/**
 * Created by athena on 2015/11/26.
 * Email: lizhiqiang@bjjajale.com
 */
public class SystemMsgActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public LoadingDialog loadingDialog;
    private ListView listView;
    private SystemMessageAdapter mAdapter;
    private PtrClassicFrameLayout mPtrFrame;
    private MsgMember msgMember;
    private SmartWatch watch;


    private BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(BroadcastConstants.ACTION_MESSAGE_UPDATA)) {
                update(false);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_msg);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastConstants.ACTION_MESSAGE_UPDATA);
        registerReceiver(updateReceiver, intentFilter);
        msgMember = getIntent().getParcelableExtra(MsgMember.KEY);
        if (msgMember == null) {
            finish();
            return;
        }
        msgMember.setCount_unread(0);
        MessageUtils.updateMsgMember(msgMember);
        NotificationUtils.cancel(this, NotificationUtils.SYSTEM_MESSAGE_ID);
//        watch = getIntent().getParcelableExtra(SmartWatchListData.KEY);

        initView();
    }

    private void initView() {
        loadingDialog = new LoadingDialog(SystemMsgActivity.this);

        View title = findViewById(R.id.title);
        TextView midTitle = (TextView) title.findViewById(R.id.tv_middle);
        midTitle.setText(getResources().getString(R.string.msg_system_list_title));

//
//        TextView tv_right= (TextView) findViewById(R.id.tv_right);
//        tv_right.setText("清空");
//        tv_right.setOnClickListener(this);
        ImageView iv_left = (ImageView) title.findViewById(R.id.iv_left);
        iv_left.setImageResource(R.drawable.title_goback_selector);
        iv_left.setOnClickListener(this);


        listView = (ListView) findViewById(R.id.lv_msg);
        listView.setOnItemClickListener(this);
        mAdapter = new SystemMessageAdapter(SystemMsgActivity.this, msgMember, new MessageCommandListener() {
            @Override
            public void deleteMsg(int index) {
                loadingDialog.show();
                Message msg = (Message) mAdapter.getItem(index);
//
                if (MessageUtils.deleteMsgByUUID(msg.getMsg_id()) != -1) {
                    mAdapter.deleteIndexMsg(index);
                }
                MsgMember system = MessageUtils.getMsgMemberByUserId("000");
                if (mAdapter.getCount() > 0) {
                    Message last_msg = mAdapter.getItem(mAdapter.getCount() - 1);
                    system.setLast_msg_desc(last_msg.content);
                    system.setLast_msg_type(last_msg.message_type);
                    system.setCount_unread(0);
                    system.setCount_msg(system.getCount_msg() >= 1 ? system.getCount_msg() - 1 : 0);
                } else {
                    system.setLast_msg_type("");
                    system.setLast_msg_desc("system_clean");
                    system.setCount_unread(0);
                    system.setCount_msg(0);
                    system.setUpdate_time("");
                }

                MessageUtils.updateMsgMember(system);
                loadingDialog.dismiss();
            }

            @Override
            public void resendText(Message resend_msg) {

            }

            @Override
            public void resendVoice(Message resend_voice) {

            }
        });
        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.rotate_header_list_view_frame);
//        mPtrFrame.setLastUpdateTimeRelateObject(this);
        final MaterialHeader header=new MaterialHeader(this);
        header.setPtrFrameLayout(mPtrFrame);
        int[] colors = getResources().getIntArray(R.array.material_colors);
        header.setColorSchemeColors(colors);
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
                GetMessageHelper helper = new GetMessageHelper(SystemMsgActivity.this, new GetMessageListener() {
                    @Override
                    public void onSuccess(String userinfoJson) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                mPtrFrame.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        update(false);
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
        loadingDialog.show();
        update(true);
    }

    /**
     * 清空所有对话
     */
    private void deleteConversation() {
        MessageUtils.deleteAllMsgByUserName(msgMember.getUser_id());
        update(true);
    }

    @Override
    public void onClick(View v) {
        if (CMethod.isFastDoubleClick()) {
            return;
        }

        switch (v.getId()) {
            case R.id.tv_right:
                L.e("清空被点击");
                if (mAdapter.getCount() > 0) {
                    DialogUtils.deleteConvarsationDialog(SystemMsgActivity.this, new SimpleClickListener() {
                        @Override
                        public void ok() {
                            deleteConversation();
                        }

                        @Override
                        public void cancle() {

                        }
                    });
                }

//                Message msg_sys_battery = MessageFactory.getInstance().createSystemMessage("电量低于18%，请尽快充值", WarningContentType.WARNING_BATTERY);
//                Message msg_sys_sos = MessageFactory.getInstance().createSystemMessage("宝贝1的位置:\\r\\n朝阳区北苑路", WarningContentType.SOS);
//                Message msg_sys_location = MessageFactory.getInstance().createSystemMessage("宝贝1出了安全区域!请注意宝贝安全!", WarningContentType.WARNING_LOCATION);
//                Message msg_sys_welcome = MessageFactory.getInstance().createSysWelcomeMsg();
//                MessageUtils.saveMsg2DB(msg_sys_battery);
//                MessageUtils.saveMsg2DB(msg_sys_sos);
//                MessageUtils.saveMsg2DB(msg_sys_location);
//                MessageUtils.saveMsg2DB(msg_sys_welcome);


                break;
            case R.id.iv_left:
                L.e("后退被点击");
                List<Message> src_list = MessageUtils.getAllMessageWithUserId("0");
                L.e("msg_list_size:" + src_list.size());
                Finish();
                break;


        }
    }

    private void Finish() {

        if (mAdapter.getCount() > 0) {

            Message lastMsg = mAdapter.getItem(mAdapter.getCount() - 1);
            msgMember.setUpdate_time(lastMsg.create_time);

            msgMember.setLast_msg_type(lastMsg.message_type);
//            if (lastMsg.content_type.equals(MessageContentType.VOICE+"")){
//                child.setLast_msg_desc("[语音]");
//            }else {
            msgMember.setLast_msg_desc(lastMsg.content);
//            }
            msgMember.setCount_unread(0);

            int result = MessageUtils.updateMsgMember(msgMember);
            L.e("update child index is  result is--->" + result);


        }
        finish();
    }


    @Override
    public void onBackPressed() {
        Finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private void update(final boolean isToLast) {


        ThreadUtils.getPool().execute(new Runnable() {
            @Override
            public void run() {

                final List<Message> recentlys = MessageUtils.getAllMessageWithUserId("000");

                loadingDialog.dismiss();
                SystemMsgActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mAdapter != null) {
                            if (listView.getAdapter() == null) {

                                listView.setAdapter(mAdapter);
                            }
                            mAdapter.refresh(recentlys);
                        }

                        if (isToLast)
                            listView.setSelection(mAdapter.getCount());

                    }
                });
            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(updateReceiver);

    }


}
