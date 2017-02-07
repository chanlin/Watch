package com.jajale.watch.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.jajale.watch.AppConstants;
import com.jajale.watch.BaseActivity;
import com.jajale.watch.BaseApplication;
import com.jajale.watch.BroadcastConstants;
import com.jajale.watch.R;
import com.jajale.watch.adapter.ChildMessageAdapter;
import com.jajale.watch.entity.HYReturnMessageListData;
import com.jajale.watch.entitydb.Message;
import com.jajale.watch.entitydb.MsgMember;
import com.jajale.watch.entityno.MessageContentStatus;
import com.jajale.watch.entityno.MessageContentType;
import com.jajale.watch.entityno.UMeventId;
import com.jajale.watch.factory.MessageFactory;
import com.jajale.watch.helper.MediaHelper;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.listener.MessageCommandListener;
import com.jajale.watch.listener.SimpleClickListener;
import com.jajale.watch.message.MySocketManager;
import com.jajale.watch.utils.Base64Tools;
import com.jajale.watch.utils.ByteUtil;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.DateUtils;
import com.jajale.watch.utils.DialogUtils;
import com.jajale.watch.utils.FileUtils;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.Md5Util;
import com.jajale.watch.utils.MessageUtils;
import com.jajale.watch.utils.NotificationUtils;
import com.jajale.watch.utils.T;
import com.jajale.watch.utils.ThreadUtils;
import com.jajale.watch.utils.UrlAddressUrils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by athena on 2015/11/26.
 * Email: lizhiqiang@bjjajale.com
 */
public class ChildMsgActivity extends BaseActivity implements View.OnClickListener {

    public static final int REQUEST_CODE_CHANEG_DRAWABLE = 12;
    public static final int REFRESH_ADAPTER = 14;


    @InjectView(R.id.iv_left)
    ImageView iv_left;
    @InjectView(R.id.tv_middle)
    TextView midTitle;
    @InjectView(R.id.tv_right)
    TextView tv_right;
    @InjectView(R.id.btn_set_model_voice)
    ImageView btnSetModelVoice;
    @InjectView(R.id.btn_set_mode_keyboard)
    ImageView btnSetModeKeyboard;
    @InjectView(R.id.tv_voice_btn_intro)
    TextView tv_voice_btn_intro;
    @InjectView(R.id.btn_press_to_speak)
    LinearLayout btnPressToSpeak;
    @InjectView(R.id.et_sendmessage)
    EditText edittext_layout;
    @InjectView(R.id.edittext_layout)
    LinearLayout edittextLayout;
    @InjectView(R.id.btn_send)
    Button btnSend;
    @InjectView(R.id.ll_control_bottom)
    LinearLayout llControlBottom;
    @InjectView(R.id.bar_bottom)
    LinearLayout barBottom;
    @InjectView(R.id.pb_load_more)
    ProgressBar pbLoadMore;
    @InjectView(R.id.lv_msg)
    ListView listView;

    @InjectView(R.id.mic_image)
    ImageView micImage;
    @InjectView(R.id.recording_hint)
    TextView recordingHint;
    @InjectView(R.id.recording_container)
    RelativeLayout recordingContainer;


    private String fromTag = "";
    private MsgMember msgMember;
    public LoadingDialog loadingDialog;
    private ChildMessageAdapter mAdapter;
    private int voice_time = 0;
    private Drawable[] micImages;
    private MediaHelper mediaHelper;
    private PowerManager.WakeLock wakeLock;
    private boolean isloading;
    private boolean haveMoreData = true;
    private InputMethodManager manager;
    private boolean iskeyboardShow = false;
    private Gson gson = new Gson();
    private int pageSize = 20;
    private boolean voice_send_lock = false;

    RequestQueue queue;
    String service_message_list = "watch.get_new_message_list";//获取新微聊信息列表
    String service_send_msg = "watch.send_msg";//发送信息列表
    String user_id;
    String imei;
    String URL = "http://lib.huayinghealth.com/lib-x/?";
    SharedPreferences sp;
    SharedPreferences.Editor editor;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REQUEST_CODE_CHANEG_DRAWABLE:
                    chageMicDrawbale(msg.arg1);
                    break;
                case REFRESH_ADAPTER:

                    break;
            }

        }
    };
//    private MySocketUtils socketUtils;

    private BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {//手表发送语音后接收的广播
            String action = intent.getAction();
            if (action.equals(BroadcastConstants.SEND_VOICE_SUCCESS)) {
                T.s(getResources().getString(R.string.not_online));
            }
            if (action.equals(BroadcastConstants.ACTION_MESSAGE_UPDATA)) {
//                firstmsglist();
                msgList = getDataFromFile();//list值
                //去重
                for(int i = 0; i < msgList.size(); i++) {
                    for (int  j= i + 1; j <msgList.size(); j++)
                    {
                        if (msgList.get(i).getContent().toString().trim().equals(msgList.get(j).getContent().toString().trim()))
                        {
                            msgList.remove(j);
                            j--;
                        }
                    }

                }
                L.e("sign==" + "广播更新录音信息");
                String s = "";
                for (int i = 0; i < msgList.size(); i++) {
                    s = msgList.get(i).getContent().toString().trim() + "--";
                    L.e("sign==" + s.toString());
                }
                editor.putString("new_message_notice", "0");
                editor.commit();
                refreshListView(false);
                savemsglist();//保存msglist数据
            }
        }
    };

    private void savemsglist() {
        for (int i = 1; i < msgList.size() + 1; i++) {
            SharedPreferences sp = getSharedPreferences("file"+ i, MODE_PRIVATE);
            SharedPreferences.Editor et = sp.edit();
            et.putString("num", msgList.size()+"");//消息条数
            et.putString("msg_id", msgList.get(i-1).getMsg_id().toString());//用户id
            et.putString("from_user", msgList.get(i-1).getFrom_user().toString());//发送方userid
            et.putString("to_user", msgList.get(i-1).getTo_user().toString());//接收方userid
            et.putString("content", msgList.get(i-1).getContent().toString());// 消息内容、语音名称
            et.putString("content_type", msgList.get(i-1).getContent_type()+"");//消息内容的类型 （IMAGE/VOICE/TEXT）
            et.putString("message_status", msgList.get(i-1).getMessage_status()+"");//消息的状态 (1是发送失败 2是发送成功 3是发送中 4是接收的已读 2是接收的消息未读)
            et.putString("create_time", msgList.get(i-1).getCreate_time().toString());//每条消息的发送时间戳
            et.putString("url", msgList.get(i-1).getUrl().toString());//语音下载路径url
            et.commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_msg);
        queue = Volley.newRequestQueue(getApplicationContext());
        sp = getApplicationContext().getSharedPreferences("NotDisturb", getApplicationContext().MODE_PRIVATE);// 创建对象
        editor = sp.edit();
        imei = sp.getString("IMEI", "");
        user_id = sp.getString("Telephone_Number", "");

        ButterKnife.inject(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastConstants.ACTION_MESSAGE_UPDATA);//接收更新语音数据
        registerReceiver(updateReceiver, intentFilter);

        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(BroadcastConstants.SEND_VOICE_SUCCESS);//接收手表不在线提示
        registerReceiver(updateReceiver, intentFilter2);

        msgMember = getIntent().getParcelableExtra(MsgMember.KEY);

        if (msgMember == null) {
            finish();
            return;
        }

        msgMember.setCount_unread(0);
        MessageUtils.updateMsgMember(msgMember);
        NotificationUtils.cancel(this, CMethod.parseInt(msgMember.getUser_id()));
        fromTag = getIntent().getStringExtra("from_tag");

        ButterKnife.inject(this);
        initTitle();
        initMediaHelper();
        initView();
        bindListener();
        MessageUtils.updateUnReadStatusByUserName(msgMember.getUser_id());


    }

    private void initTitle() {
        midTitle.setText(msgMember.getNick_name());
        tv_right.setText(getResources().getString(R.string.clear_message));
        tv_right.setTextColor(Color.WHITE);
        iv_left.setImageResource(R.drawable.title_goback_selector);
    }

    private void initView() {
        loadingDialog = new LoadingDialog(ChildMsgActivity.this);
        initMicImgRes();
        wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "jjl");
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mAdapter = new ChildMessageAdapter(ChildMsgActivity.this, msgMember, mediaHelper, new MessageCommandListener() {
            @Override
            public void deleteMsg(int index) {
                L.e("1111111111111111111111");
                loadingDialog.show();

                final Message msg = mAdapter.getItem(index);
                msgList.remove(index);
                refreshListView(false);

                loadingDialog.dismiss();
            }

            @Override
            public void resendText(Message resend_msg) {
                sendTextMsg(resend_msg);
            }

            @Override
            public void resendVoice(Message resend_voice) {
                reSendVoice(resend_voice);
            }
        });
        loadingDialog.show();
        firstmsglist();
        if ("1".equals(sp.getString("new_message_notice",""))) {
            firstmsglist2();
            editor.putString("new_message_notice", "0");
            editor.commit();
        }

    }

    private void firstmsglist() {
        if (!CMethod.isNet(getApplicationContext())) {
            T.s(getResources().getString(R.string.no_network));
            return;
        }
        msgList.clear();
        SharedPreferences sp = getSharedPreferences("file1", MODE_PRIVATE);
        if (!sp.getString("num","").equals("")) {
            int num = Integer.parseInt(sp.getString("num", ""));
            for (int i = 1; i < num + 1; i++) {
                Message message = new Message();

                SharedPreferences sp1 = getSharedPreferences("file"+ i, MODE_PRIVATE);
                if (!"".equals(sp1.getString("content",""))) {
                    L.e("sign==" + sp1.getString("msg_id","") + "=" + i);
                    message.setMsg_id(sp1.getString("msg_id",""));
                    message.setFrom_user(sp1.getString("from_user",""));
                    message.setTo_user(sp1.getString("to_user",""));
                    message.setContent(sp1.getString("content",""));
                    message.setContent_type(sp1.getString("content_type", ""));
                    message.setMessage_status(sp1.getString("message_status", ""));
                    message.setCreate_time(sp1.getString("create_time",""));
                    message.setUrl(sp1.getString("url",""));
                    msgList.add(message);
                }
            }
        }
        savemsglist();//保存msglist数据
        refreshListView(true);
    }

    private void firstmsglist2() {
        if (!CMethod.isNet(getApplicationContext())) {
            T.s(getResources().getString(R.string.no_network));
            return;
        }
        String sign = Md5Util.stringmd5(imei, service_message_list, user_id);
        String url = URL + "service=" + service_message_list + "&imei=" + imei + "&user_id=" +user_id + "&sign=" + sign;
        L.e("sign===" + url);
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                L.e("response==" + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject json = jsonObject.getJSONObject("data");
                    int code = json.getInt("code");
                    if(code == 0){
                        msgList = getDataFromFile();//list值
                        //去重
                        for (int i = 0; i < msgList.size(); i++) {
                            for (int j = i + 1; j < msgList.size(); j++) {
                                if (msgList.get(i).getContent().toString().trim().equals(msgList.get(j).getContent().toString().trim())) {
                                    msgList.remove(j);
                                    j--;
                                }
                            }
                        }
                        savemsglist();//保存msglist数据
                        refreshListView(false);
                    }else if(code == 1){
                        L.e("无新消息");
                    }
//                    JSONArray array = json.getJSONArray("message");
//                    for (int i = 0; i < array.length(); i++) {
//                        Message message = new Message();
//                        JSONObject object = array.getJSONObject(i);
//                        String filename = object.getString("file");
//                        message.setContent(filename);
//                        message.setMsg_id(user_id);
//                        message.setUrl("http://core.huayinghealth.com/media/childwatch/"+filename);
//                        message.setContent_type(""+MessageContentType.VOICE);//消息内容的类型、录音2、照片4、文本3
//                        message.setMessage_status("2");//消息的状态 (1是发送失败 2是发送成功 3是发送中 4是接收的已读 2是接收的消息未读)
//                        message.setCreate_time("11:30:20");//每条消息的发送时间戳
////                      message.setAudio_time("8");//音频时间
//                        message.setFrom_user("watch");
//                        message.setTo_user("phone");
//                        msgList.add(message);
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                refreshListView(true);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismissAndStopTimer();
            }
        });
        queue.add(request);
    }

    private void initMediaHelper() {
        mediaHelper = new MediaHelper(ChildMsgActivity.this);
        mediaHelper.setRecorderListener(new MediaHelper.MediaRecorderListener() {
            @Override
            public void update(int second) {
                L.e("media_update", second + "");
                voice_time = second;
                if (second >= 15) {
                    //停止录音发送
                    T.s(getResources().getString(R.string.record_time));
                    closeVoiceBtn();
                }


            }
        });
        mediaHelper.registerMicCallBack(new MediaHelper.MicDBCallback() {
            @Override
            public void getDb(int result) {
                L.e("ChatActivity", result + "");
                android.os.Message msg = new android.os.Message();
                msg.arg1 = result;
                msg.what = REQUEST_CODE_CHANEG_DRAWABLE;
                mHandler.sendMessage(msg);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (CMethod.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_right:
                if (mAdapter.getCount() > 0) {
                    DialogUtils.deleteConvarsationDialog(ChildMsgActivity.this, new SimpleClickListener() {
                        @Override
                        public void ok() {
                            mediaHelper.stopPlay();
                            deleteConversation();
                        }

                        @Override
                        public void cancle() {

                        }
                    });
                }
                break;

            case R.id.iv_left:
                Finish();
                break;
            case R.id.btn_set_model_voice:
                L.e("model_voice");
                setModeVoice();
                break;
            case R.id.btn_set_mode_keyboard:
                L.e("model_keyboard");
                setModeKeyBoard();
                break;

            case R.id.btn_send:
                L.e("发送消息");
                if (!CMethod.isFastDoubleClick()) {
                    String result = edittext_layout.getText().toString().trim();
                    if (!CMethod.isEmpty(result)) {
                        sendNewTextMsg(result);
                    }
                }

                break;
        }
    }
    List<Message> msgList = new ArrayList<Message>();

    private List<Message> getDataFromFile(){
        try {
            String file= FileUtils.readLine("/mnt/sdcard/huaying.dat");
            try {
                JSONObject jsonObject = new JSONObject(file.toString());
                JSONObject json = jsonObject.getJSONObject("data");
                JSONArray array = json.getJSONArray("message");
                for (int i = 0; i < array.length(); i++) {
                    Message message = new Message();
                    JSONObject object = array.getJSONObject(i);
                    String filename = object.getString("file");
                    message.setMsg_id(user_id);
                    message.setContent(filename);//
                    message.setUrl("http://core.huayinghealth.com/media/childwatch/"+filename);
                    if (filename.endsWith(".amr")) {
                        message.setContent_type("" + MessageContentType.VOICE);//消息内容的类型、录音2、照片4、文本3
                    } else if (filename.endsWith(".jpg")) {
                        message.setContent_type("" + MessageContentType.IMAGE);//消息内容的类型、录音2、照片4、文本3
                    }
                    message.setMessage_status("2");//消息的状态 (1是发送失败 2是发送成功 3是发送中 4是接收的已读)
                    message.setFrom_user("watch");
                    message.setTo_user("phone");
                    Long time = System.currentTimeMillis();
                    message.setCreate_time(time.toString());//每条消息的发送时间戳
                    msgList.add(message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            msgList = null;
        }
        return msgList;
    }
    //刷新 聊天界面消息
    private void refreshListView(final boolean isSelection) {


        ThreadUtils.getPool().execute(new Runnable() {
            @Override
            public void run() {
                if (msgList == null ){
                    loadingDialog.dismiss();
                    return;
                }

                ChildMsgActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mAdapter != null) {
                            if (listView.getAdapter() == null) {
                                listView.setAdapter(mAdapter);
                            }
                            mAdapter.refresh(msgList);
                        }
                        loadingDialog.dismiss();
                        if (isSelection) {
                            listView.setSelection(mAdapter.getCount());
                        }
                    }
                });
            }
        });
    }

    private void chageMicDrawbale(int db) {
        L.e("CHAT_ACTIVITY", db + "");
        if (db >= 65 && db < 75) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                micImage.setBackground(micImages[1]);
            } else {
                micImage.setBackgroundDrawable(micImages[1]);
            }
        } else if (db >= 75 & db < 85) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                micImage.setBackground(micImages[2]);
            } else {
                micImage.setBackgroundDrawable(micImages[2]);
            }
        } else if (db >= 85) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                micImage.setBackground(micImages[3]);
            } else {
                micImage.setBackgroundDrawable(micImages[3]);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                micImage.setBackground(micImages[0]);
            } else {
                micImage.setBackgroundDrawable(micImages[0]);
            }
        }
    }

    private void bindListener() {
        tv_right.setOnClickListener(this);
        iv_left.setOnClickListener(this);
        btnSetModeKeyboard.setOnClickListener(this);
        btnSetModelVoice.setOnClickListener(this);
        btnSend.setOnClickListener(this);

        btnPressToSpeak.setOnTouchListener(new PressToSpeakListen());
        listView.setOnScrollListener(new ListScrollListener());

        edittext_layout.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int maxLen = 30;
                Editable editable = edittext_layout.getText();
                int len = editable.toString().getBytes().length;

                if (len > maxLen) {
                    T.s(getResources().getString(R.string.show_character));
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString();
                    //截取新字符串
                    String newStr = null;
                    try {
                        newStr = CMethod.substringByByte(str, 30, "");
                        int len_new = newStr.getBytes().length;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    edittext_layout.setText(newStr);
                    editable = edittext_layout.getText();
//                    //新字符串的长度
                    if (editable != null) {
                        int newLen = editable.length();
//                    //旧光标位置超过字符串长度
                        if (selEndIndex > newLen) {
                            selEndIndex = editable.length();
                        }
//                      设置新光标所在的位置
                        Selection.setSelection(editable, selEndIndex);
                    }

                }

            }
        });

    }


    private void closeVoiceBtn() {
        btnPressToSpeak.setPressed(false);
        recordingContainer.setVisibility(View.GONE);
        recordingHint.setVisibility(View.GONE);
        tv_voice_btn_intro.setTextColor(Color.rgb(136, 136, 136));
        tv_voice_btn_intro.setText(getResources().getString(R.string.hold_to_talk));
        prepare2SendVoice();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (btnPressToSpeak.isPressed()) {
            closeVoiceBtn();
        }
    }

    @Override
    public void onBackPressed() {
        Finish();
    }

    public void Finish() {
        mediaHelper.stopPlay();
        //返回更新msgfragment页面的消息展示内容
        if (mAdapter.getCount() > 0) {
            Message lastMsg = mAdapter.getItem(mAdapter.getCount() - 1);
            msgMember.setUpdate_time(lastMsg.create_time);

            msgMember.setLast_msg_type(lastMsg.message_type);
            if (lastMsg.content_type.equals(MessageContentType.VOICE + "")) {
                msgMember.setLast_msg_desc("[语音]");
            } else if (lastMsg.content_type.equals(MessageContentType.IMAGE + "")) {
                msgMember.setLast_msg_desc("[照片]");
            } else {
                msgMember.setLast_msg_desc(lastMsg.content);
            }
            int result = MessageUtils.updateMsgMember(msgMember);
            msgMember.setCount_unread(0);
        }
        finish();
    }

    private void initMicImgRes() {
        // 动画资源文件,用于录制语音时 不通音频音量高度显示的图片资源  record_animate_01
        micImages = new Drawable[]{getResources().getDrawable(R.mipmap.record_animate_01), getResources().getDrawable(R.mipmap.record_animate_03), getResources().getDrawable(R.mipmap.record_animate_05), getResources().getDrawable(R.mipmap.record_animate_07)};//
    }

    /**
     * 按住说话listener
     */
    class PressToSpeakListen implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    if (!CMethod.isExitsSdcard()) {
                        Toast.makeText(ChildMsgActivity.this, getResources().getString(R.string.sdcard_support), Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    try {
                        v.setPressed(true);
                        wakeLock.acquire();
                        recordingContainer.setVisibility(View.VISIBLE);
                        recordingHint.setVisibility(View.VISIBLE);
                        tv_voice_btn_intro.setTextColor(Color.WHITE);
                        tv_voice_btn_intro.setText(getResources().getString(R.string.loosen_the_end));
                        recordingHint.setText(getString(R.string.move_up_to_cancel));
                        recordingHint.setBackgroundColor(Color.TRANSPARENT);
                        voice_time = 0;
                        mediaHelper.startRecord();

                    } catch (Exception e) {
                        e.printStackTrace();
                        v.setPressed(false);
                        if (wakeLock.isHeld())
                            wakeLock.release();
                        recordingContainer.setVisibility(View.GONE);
                        recordingHint.setVisibility(View.GONE);
                        Toast.makeText(ChildMsgActivity.this, R.string.recoding_fail, Toast.LENGTH_SHORT).show();
                        mediaHelper.stopRecord();
                        return false;
                    }

                    return true;
                case MotionEvent.ACTION_MOVE: {
                    if (event.getY() < 0) {
                        recordingHint.setText(getString(R.string.release_to_cancel));
                        recordingHint.setBackgroundResource(R.drawable.recording_text_hint_bg);
                    } else {
                        recordingHint.setText(getString(R.string.move_up_to_cancel));
                        recordingHint.setBackgroundColor(Color.TRANSPARENT);
                    }
                    return true;
                }
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    L.e("press btn aciton up");
                    if (CMethod.isFastDoubleClick() || !v.isPressed()) {
                        return true;
                    }

                    v.setPressed(false);
                    recordingContainer.setVisibility(View.GONE);
                    recordingHint.setVisibility(View.GONE);
                    tv_voice_btn_intro.setTextColor(Color.rgb(136, 136, 136));
                    tv_voice_btn_intro.setText(getResources().getString(R.string.hold_to_talk));

                    if (wakeLock.isHeld())
                        wakeLock.release();
                    if (event.getY() < 0) {
                        mediaHelper.discardRecording();
                    } else {
                        prepare2SendVoice();

                    }
                    return true;
                default:
                    return false;
            }
        }
    }


    /**
     * 点击判断发送语音的状态
     */
    private void prepare2SendVoice() {
        if (voice_send_lock && btnPressToSpeak.isPressed()) {
            return;
        }
        voice_send_lock = true;
        try {
            MediaHelper.Entity result = mediaHelper.stopRecord();

            if (voice_time > 0) {
                if (voice_time < 15) {
                    sendVoice(result);//条件下发送语音
                }
            } else {
                voice_send_lock = false;
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.record_short), Toast.LENGTH_LONG).show();
                voice_time = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            voice_send_lock = false;
            Toast.makeText(ChildMsgActivity.this, getResources().getString(R.string.send_failed), Toast.LENGTH_SHORT).show();
            voice_time = 0;
        }

    }

    /**
     * 发送语音
     *
     * @param entity
     */
    private void sendVoice(MediaHelper.Entity entity) {
        if (!(new File(entity.filename).exists())) {
            voice_time = 0;
            voice_send_lock = false;
            return;
        }
        try {

            String content = Base64Tools.base64Encode(ByteUtil.getBytes(entity.filename));
            L.e("voice1" + entity.filename.toString());
            sendVoiceToServer(content, entity.filename);//发送语音

        } catch (Exception e) {
            voice_send_lock = false;
            e.printStackTrace();
        }
    }

    public String toHex(String info){
        String len1 = Integer.toHexString(info.length());
        for (int i = len1.length(); i < 4; i++) {
            len1 = "0"+len1;
        }
        String b = len1 + info;
         return b;
    }
    private String suffixes = ".amr";

    /**
     * 向服务器发送语音消息
     *
     */
    private void sendVoiceToServer(final String content, final String fileName) {

        if (CMethod.isEmpty(content) && "2321414d520a".equals(content)) {
            T.s(getResources().getString(R.string.record_permission));
            return;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            //{"id":"13112345679","cmd":"tk","imei":"123456789012349","info":"123"}
            jsonObject.put("id", user_id);
            jsonObject.put("cmd", "tk");
            jsonObject.put("imei", imei);
            jsonObject.put("info", content);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String inf = toHex(jsonObject.toString());

        Intent intent = new Intent();
        intent.putExtra("data", inf);
        intent.setAction(BroadcastConstants.ACTION);
        sendBroadcast(intent);
        Message msg = new Message();
        msg.setContent_type(""+MessageContentType.VOICE);//消息内容的类型、录音2、照片4、文本3
        msg.setMessage_status("2");//消息的状态 (1是发送失败 2是发送成功 3是发送中 4是接收的已读)
        msg.setContent(fileName);
        Long time = System.currentTimeMillis();
        msg.setCreate_time(time.toString());
        msg.setFrom_user("");
        msg.setMsg_id(user_id);
        msg.setTo_user("phone");
        msg.setUrl("");
        msgList.add(msg);
        refreshListView(false);
        savemsglist();//保存msglist数据
    }


    /**
     * listview滑动监听listener
     */
    private class ListScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                    if (view.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
                        pbLoadMore.setVisibility(View.VISIBLE);
                        try {
                            loadMoreMsg(msgMember.getUser_id(), mAdapter.getItem(0).create_time, pageSize + "");
                        } catch (Exception e1) {
                            pbLoadMore.setVisibility(View.GONE);
                            return;
                        }
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                        }
                        pbLoadMore.setVisibility(View.GONE);
                        isloading = false;

                    }
                    break;
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }
    }


    public void setModeVoice() {

        L.e("Chat", "modelVoice");
        hideKeyboard();
        edittextLayout.setVisibility(View.GONE);
        btnSetModelVoice.setVisibility(View.GONE);

        btnSetModeKeyboard.setVisibility(View.VISIBLE);
        btnSend.setVisibility(View.GONE);
        btnPressToSpeak.setVisibility(View.VISIBLE);
    }

    private void setModeKeyBoard() {

        hideKeyboard();
        edittext_layout.setFocusable(true);
        edittext_layout.requestFocus();
        edittextLayout.setVisibility(View.VISIBLE);
        btnSetModeKeyboard.setVisibility(View.GONE);
        btnSetModelVoice.setVisibility(View.VISIBLE);
        btnPressToSpeak.setVisibility(View.GONE);
        btnSend.setVisibility(View.VISIBLE);


    }

    private void sendNewTextMsg(String content) {

        Message msg = new Message();
        msg.setContent_type(""+MessageContentType.TEXT);//消息内容的类型、录音2、照片4、文本3
        msg.setMessage_status("2");//消息的状态 (1是发送失败 2是发送成功 3是发送中 4是接收的已读 2是接收的消息未读)
        msg.setContent(edittext_layout.getText().toString().trim());
        Long time = System.currentTimeMillis();
        msg.setCreate_time(time.toString());
        msg.setFrom_user("");
        msg.setMsg_id(user_id);
        msg.setTo_user("phone");
        msg.setUrl("");

        msgList.add(msg);
        sendTextMsg(msg);
        refreshListView(false);
        savemsglist();//保存msglist数据
    }


    /**
     * 隐藏软键盘
     */
    private void hideKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            iskeyboardShow = false;
        }
    }


    /**
     * 发送文字消息
     *
     * @param msg
     */
    private void sendTextMsg(final Message msg) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id",user_id);
            jsonObject.put("cmd","tk");
            jsonObject.put("imei",imei);
            jsonObject.put("info","123");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String result = edittext_layout.getText().toString().trim();
        edittext_layout.setText("");
        if (!CMethod.isEmpty(result)) {
            String message = "";
            char[] utfBytes = result.toCharArray();
            for (int j = 0; j < result.length(); j++) {
                String name = Integer.toHexString(utfBytes[j]);
                if (name.length() <= 2) {
                    name = "00" + name;
                }
                message = message + name;
            }
            String sign = Md5Util.stringmd5(imei, message, service_send_msg);
            String url = URL + "service=" + service_send_msg + "&imei=" + imei + "&message=" + message + "&sign=" + sign;
            L.e("sign===" + url);
            StringRequest request = new StringRequest(url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    loadingDialog.dismissAndStopTimer();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject jsondata = jsonObject.getJSONObject("data");
                        int code = jsondata.getInt("code");
                        if(code == 0){
                            Long time = System.currentTimeMillis();
//                            MessageUtils.updateMsgByUUID(msg.msg_id, MessageContentStatus.SEND_SUCCESS + "", null, time);
                            msg.create_time = time + "";
//                            refreshListView(false);
//                            savemsglist();//保存msglist数据
                            L.e("msglistsend"+msgList.toString());
                        }else if(code == 1){
                            T.s(getResources().getString(R.string.not_online));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    loadingDialog.dismissAndStopTimer();
                }
            });
            queue.add(request);
        }


    }


    private void reSendVoice(Message msg) {
        if (msg.url != null)
            if (!(new File(msg.url).exists())) {
                T.s(getResources().getString(R.string.record_delete));
                MessageUtils.updateMsgByUUID(msg.msg_id, MessageContentStatus.SEND_FAIL + "", null);
                refreshListView(false);
                return;
            }
//        sendVoiceToServer(msg, msg.url);
    }


    /**
     * 清空所有对话
     */
    private void deleteConversation() {
//        MessageUtils.deleteAllMsgByUserName(msgMember.getUser_id());
        msgList.removeAll(msgList);
        loadingDialog.dismiss();
        refreshListView(false);
        //清空消息列表
        SharedPreferences sp = getSharedPreferences("file1", MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putString("num", "");//消息条数
        et.commit();
    }


    private void loadMoreMsg(String userID, String createTime, String pageSize) {
        List<Message> temp = MessageUtils.loadMoreMsgWithLastMsgTime(userID, createTime, pageSize);
        if (temp.size() > 0) {
            mAdapter.loadMoreMsg(temp);
            listView.setSelection(temp.size() - 1);
        } else {
            T.s(getResources().getString(R.string.no_more_data));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (fromTag.equals("msgcenter")) {
            listView.setSelection(mAdapter.getCount());
            fromTag = "";
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaHelper.stopPlay();
        unregisterReceiver(updateReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaHelper.stopPlay();

    }
}
