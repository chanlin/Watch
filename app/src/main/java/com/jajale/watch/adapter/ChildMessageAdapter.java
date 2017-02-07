package com.jajale.watch.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jajale.watch.R;
import com.jajale.watch.cviews.RoundedImageView;
import com.jajale.watch.entity.Conversation;
import com.jajale.watch.entitydb.Message;
import com.jajale.watch.entitydb.MsgMember;
import com.jajale.watch.entityno.MessageContentStatus;
import com.jajale.watch.entityno.MessageContentType;
import com.jajale.watch.helper.MediaHelper;
import com.jajale.watch.image.GetImage;
import com.jajale.watch.image.JJLImageLoader;
import com.jajale.watch.image.MyApplication;
import com.jajale.watch.listener.MessageCommandListener;
import com.jajale.watch.listener.SimpleClickListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.DialogUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.Md5Util;
import com.jajale.watch.utils.MessageUtils;
import com.jajale.watch.utils.ParentHeadViewUtils;
import com.jajale.watch.utils.T;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * Created by athena on 2015/11/27.
 * Email: lizhiqiang@bjjajale.com
 */
public class ChildMessageAdapter extends BaseAdapter {
    private Activity mActivity;
    //    private ArrayList<Message> mListContact ;
    private MsgMember msgMember;
    private LayoutInflater mInflater;
    private MessageCommandListener mListener;
    private Conversation conversation; //本次会话
    private MediaHelper helper;
    private AnimationDrawable animationDrawable;//语音播放动画
    RequestQueue queue;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    String imei, user_id;
    String URL = "http://lib.huayinghealth.com/lib-x/?";
    String service_ignore_message = "watch.ignore_message";//获取新微聊信息列表
    private final static String ALBUM_PATH = "/storage/sdcard0/Android/data/com.jajale.watch/cache/";
    private ViewHolder holder;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                if (!"".equals(msg.obj)) {
                    holder.iv_image.setImageBitmap((Bitmap) msg.obj);
                }
            }
        }
    };


    public ChildMessageAdapter(Activity mActivity, MsgMember msgMember, MediaHelper helper, MessageCommandListener listener) {
        this.mActivity = mActivity;
        this.msgMember = msgMember;
        this.helper = helper;
        this.mListener = listener;
        this.mInflater = LayoutInflater.from(mActivity);
        queue = Volley.newRequestQueue(mActivity);
        sp = mActivity.getSharedPreferences("NotDisturb", mActivity.MODE_PRIVATE);// 创建对象
        editor = sp.edit();
        imei = sp.getString("IMEI", "");
        user_id = sp.getString("Telephone_Number", "");
    }

    @Override
    public int getCount() {

        return conversation != null ? conversation.getMsgCount() : 0;
    }

    @Override
    public Message getItem(int position) {
        if (conversation.getAllMessage() != null && conversation.getAllMessage().size() > 0) {
            return conversation.getMessage(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        final Message message = getItem(position);
//        message.setUrl("http://7xosde.com2.z0.glb.qiniucdn.com/2d6f02ce-fd64-40fb-b552-af96ac77412c.png");
//        message.setContent_type("4");
        L.e("eeee:"+message.toString());
//        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            if (message != null) {
                convertView = createViewByMessage(message, position);
                holder.nriv_userhead = (RoundedImageView) convertView.findViewById(R.id.nriv_userhead);
                holder.timestamp = (TextView) convertView.findViewById(R.id.timestamp);

                if (message.getContent_type() == MessageContentType.VOICE) {
                    holder.tv_voice_length = (TextView) convertView.findViewById(R.id.tv_length);
                    holder.iv_voice = (ImageView) convertView.findViewById(R.id.iv_voice);
                    holder.voice_root = (RelativeLayout) convertView.findViewById(R.id.rl_voice_msg_root);
                } else if(message.getContent_type()==MessageContentType.IMAGE) {
                    //照片
                    L.e("int==" + "int");
                    holder.rl_image_msg_root= (RelativeLayout) convertView.findViewById(R.id.rl_image_msg_root);
                    holder.iv_image= (ImageView) convertView.findViewById(R.id.iv_image);

                }else {
                    holder.tv_chatcontent = (TextView) convertView.findViewById(R.id.tv_chatcontent);
                }
                if (message.getFrom_user().equals("")) {
                    //send
                    holder.msg_status = (ImageView) convertView.findViewById(R.id.msg_status);
                    holder.pb_sending = (ProgressBar) convertView.findViewById(R.id.pb_sending);
                    holder.iv_user_head_shadow = (ImageView) convertView.findViewById(R.id.member_space_user_head_shadow);
                } else {
                    //receive
                    holder.iv_unread_icon = (ImageView) convertView.findViewById(R.id.iv_unread_voice);
                }
            }
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //显示图片
        //
        //
        if(message.getContent_type()==MessageContentType.IMAGE) {
//            holder.iv_image.setImageResource(R.mipmap.head_image_boy);

            L.e("wwww:" + message.getUrl());
//            String urlImage = "http://core.huayinghealth.com/media/childwatch/123456789012345_1477103482.jpg";
//            String urlImage = message.getUrl();
//            JJLImageLoader.downloadRoundImage(mActivity, urlImage.toString(), holder.iv_image);
//            if (!CMethod.isEmptyOrZero(urlImage)) {
//                JJLImageLoader.download(mActivity, urlImage, holder.iv_image);
//                GetImage.download(urlImage);
//            }
            holder.iv_image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    DialogUtils.deleteMsgDialog(mActivity, new SimpleClickListener(){
                        @Override
                        public void ok() {
                            if (mListener != null) {
                                clearphoto();
                                mListener.deleteMsg(position);
                                deletemessage(position);
                            }
                        }

                        @Override
                        public void cancle() {

                        }
                    });
                    return false;
                }

                private void clearphoto() {
                    if (!CMethod.isNet(mActivity)) {
                        T.s("网络不给力");
                        return;
                    }
                    String filename = message.getContent();
                    if ("".equals(imei)||"".equals(user_id)||"".equals(filename)) {
                        return;
                    }

                    String sign = Md5Util.stringmd5(filename, imei, service_ignore_message, user_id);
                    String url = URL + "service=" + service_ignore_message + "&imei=" + imei + "&user_id=" + user_id + "&filename=" + filename + "&sign=" + sign;
                    L.e("sign===" + url);
                    StringRequest request = new StringRequest(url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject jsondata = jsonObject.getJSONObject("data");
                                int code = jsondata.getInt("code");
                                if(code == 0){
                                    L.e("处理正常");
                                }else if(code == 1){

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
                    queue.add(request);
                }
            });
        }
        //
        //
        //
        String url = msgMember.getHeader_img_url();
        L.e("img_url is :" + url + "<======>" + position);
        if (!CMethod.isEmptyOrZero(url)) {
            JJLImageLoader.download(mActivity, url, holder.nriv_userhead);
        } else {
            //处理头像
            int resid = 0;
            if (message.getFrom_user().equals("")) {
                resid = ParentHeadViewUtils.getImage(msgMember.getRelation());
//                resid = msgMember.getSex() == 1 ? R.mipmap.head_image_boy : R.mipmap.head_image_girl;
            } else {
                resid = msgMember.getSex() == 0 ? R.mipmap.head_image_girl: R.mipmap.head_image_boy;
            }


            holder.nriv_userhead.setBackgroundResource(resid);
        }

        Long timemillions;
        try {
            L.e("123===message.getCreate_time()==="+message.getCreate_time());
            timemillions = Long.parseLong(message.getCreate_time());
        } catch (Exception e) {
            timemillions = System.currentTimeMillis();
        }
        Long last_msg_time = null;
        if (position > 0) {
            Message last = getItem(position - 1);
            try {
                last_msg_time = Long.parseLong(last.getCreate_time());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        String display_result = CMethod.displayTime(timemillions, last_msg_time);
        if (display_result == null || display_result.equals("invisiable")) {
            holder.timestamp.setVisibility(View.GONE);
        } else {
            L.e("sign==" + "time");
            holder.timestamp.setText(display_result);
            holder.timestamp.setVisibility(View.VISIBLE);
        }


        if (holder.tv_chatcontent != null) {
            holder.tv_chatcontent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    DialogUtils.deleteMsgDialog(mActivity, new SimpleClickListener() {
                        @Override
                        public void ok() {
                            L.e("确定");
                            if (mListener != null) {
                                mListener.deleteMsg(position);
                                deletemessage(position);
                            }
                        }

                        @Override
                        public void cancle() {
                            L.e("取消");
                        }
                    });


                    return false;
                }
            });
        }


        switch (message.getContent_type()) {
            case MessageContentType.TEXT:
                handleTextMessage(message, holder, position);
                break;
            case MessageContentType.VOICE:
                handleVoiceMessage(message, holder, position);
                break;
            case MessageContentType.IMAGE:
//                holder.iv_image.setImageResource(R.mipmap.head_image_boy);
//                GetImage.download(message.getUrl());
                if (!CMethod.isEmptyOrZero(message.getUrl())) {
                    if (CMethod.isNet(mActivity)) {

                        File mFile=new File(ALBUM_PATH + message.content);
                        if (mFile.exists()) {//若该文件存在
                            Bitmap bitmap=BitmapFactory.decodeFile(ALBUM_PATH + message.content);
                            holder.iv_image.setImageBitmap(bitmap);
                            L.e("sign==" + "存在照片");
                        } else {
                            L.e("sign==" + "不存在照片");
                            MyApplication.imageLoader.init(ImageLoaderConfiguration.createDefault(mActivity));
                            MyApplication.imageLoader.displayImage(message.getUrl(), holder.iv_image,MyApplication.options);
                            new Thread() {//下载保存照片
                                @Override
                                public void run() {
                                    android.os.Message msg = new android.os.Message();
                                    msg.what = 1;
                                    byte[] btimg = GetImage.download(message.getUrl(), message.getContent());
//                                    Bitmap bitmap = BitmapFactory.decodeByteArray(btimg, 0, btimg.length);
//                                    msg.obj = bitmap;
//                                    mHandler.sendMessage(msg);
                                }
                            }.start();
                        }

                    } else {
                        holder.iv_image.setImageResource(R.mipmap.head_image_boy);
                    }

                }
                break;

        }

        return convertView;
    }

    private void deletemessage(int position) {
        int p = position + 1;
        L.e("sign==" + p);
        SharedPreferences sp = mActivity.getSharedPreferences("file" + p, mActivity.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putString("content", "");//消息条数
        et.commit();
    }


    public void addNewMsg(Message msg, int index) {
        L.e(index + "<<======");

        if (conversation == null) {
            conversation = new Conversation();
        }

        conversation.getAllMessage().add(msg);
        conversation.setMsgCount(conversation.getAllMessage().size());
        notifyDataSetChanged();
    }

    public void loadMoreMsg(List<Message> source) {
        for (int i = 0; i < source.size(); i++) {
            conversation.getAllMessage().add(0, source.get(i));
        }
        conversation.setMsgCount(conversation.getAllMessage().size());
        notifyDataSetChanged();

    }


    public void refresh(List<Message> temp) {
        L.d("MessageAdapter", "refresh");
//        List<Message> temp  = MessageUtils.getLastestMessageWithUserId(user_name, pageSize);

        conversation = new Conversation();
        if (temp != null) {
            if (temp.size() > 0) {
                conversation.setMessages(temp);
                conversation.setMsgCount(temp.size());
            } else {
                conversation.setMsgCount(0);
            }
        }
        notifyDataSetChanged();

    }

    static class ViewHolder {
        public RoundedImageView nriv_userhead;
        public TextView tv_chatcontent;
        public TextView timestamp;
        public ImageView iv_voice; //音频图标

        public TextView tv_voice_length;
        public ProgressBar pb_sending;
        public ImageView msg_status;  //文本发送失败时候的提示按钮
        public ImageView iv_unread_icon;

        public ImageView iv_user_head_shadow;
        public RelativeLayout voice_root;

        public RelativeLayout rl_image_msg_root;
        public ImageView iv_image;


    }


    private View createViewByMessage(Message message, int position) {

        int resId = 0;
        switch (message.getContent_type()) {
            case MessageContentType.TEXT:
                return message.getFrom_user().equals("") ? mInflater.inflate(R.layout.item_row_sent_message, null) : mInflater.inflate(R.layout.item_row_received_message, null);
            case MessageContentType.VOICE:
                return message.getFrom_user().equals("") ? mInflater.inflate(R.layout.item_row_sent_voice, null) : mInflater.inflate(R.layout.item_row_received_voice, null);
            case MessageContentType.IMAGE:
                return mInflater.inflate(R.layout.item_row_received_image,null);
        }

        return mInflater.inflate(resId, null);
    }

    public void deleteIndexMsg(int index) {
        if (conversation.getAllMessage().size() > 0) {
            conversation.getAllMessage().remove(index);
            notifyDataSetChanged();
        }


//        mListContact.remove(index);

    }

    @Override
    public int getItemViewType(int position) {
        if (conversation.getAllMessage().size() > 0) {
            Message message = conversation.getMessage(position);

            switch (message.getContent_type()) {
                case MessageContentType.TEXT:
                    return message.getFrom_user().equals("") ? 0 : 1;
                case MessageContentType.VOICE:
                    return message.getFrom_user().equals("") ? 2 : 3;
                case MessageContentType.IMAGE:
                    return 4;
            }
        }


        return -1;// invalid
    }

    @Override
    public int getViewTypeCount() {
//        return 5;
        return 7;
    }


    private void handleVoiceMessage(final Message msg, final ViewHolder holder, final int position) {
        if (!CMethod.isEmptyOrZero(msg.audio_time)) {
            holder.tv_voice_length.setText(msg.audio_time + "\"");//音频时间
        } else {
            holder.tv_voice_length.setText("");//音频时间
        }
        if (msg.getFrom_user().equals("")) {
            switch (msg.getMessage_status()) {
                case MessageContentStatus.SEND_FAIL:
                    holder.pb_sending.setVisibility(View.GONE);
                    holder.msg_status.setVisibility(View.VISIBLE);
                    break;
                case MessageContentStatus.SEND_SUCCESS:
                    holder.pb_sending.setVisibility(View.GONE);
                    holder.msg_status.setVisibility(View.GONE);
                    break;
                case MessageContentStatus.SEND_INPROFRESS:
                    holder.pb_sending.setVisibility(View.VISIBLE);
                    holder.msg_status.setVisibility(View.GONE);
                    break;
            }

            holder.msg_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CMethod.isFastDoubleClick()) {
                        return;
                    }
                    if (CMethod.isNet(mActivity)) {
                        holder.pb_sending.setVisibility(View.VISIBLE);
                        holder.msg_status.setVisibility(View.GONE);
                        mListener.resendVoice(msg);
                    } else {
                        T.s("请检查您的网络链接");
                    }
                }
            });

        } else { // 接收到的语音
            if (msg.getMessage_status() == MessageContentStatus.RECEIVE_READ) {
                holder.iv_unread_icon.setVisibility(View.GONE);
            } else if (msg.getMessage_status() == MessageContentStatus.RECEIVE_UNREAD) {
                holder.iv_unread_icon.setVisibility(View.VISIBLE);
            }

        }

        holder.voice_root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DialogUtils.deleteMsgDialog(mActivity, new SimpleClickListener() {
                    @Override
                    public void ok() {
                        L.e("确定");
                        helper.stopPlay();
                        if (mListener != null) {
                            mListener.deleteMsg(position);
                            deletemessage(position);
                        }
                    }

                    @Override
                    public void cancle() {
                        L.e("取消");
                    }
                });
                return false;


            }
        });


        //播放语音
        holder.voice_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CMethod.isFastDoubleClick()) {
                    return;
                }
                if (!CMethod.isNet(mActivity)){
                    T.s("网络不给力");
                    return;
                }
                if (holder.pb_sending != null) {
                    holder.pb_sending.setVisibility(View.VISIBLE);
                }
                if (!msg.getFrom_user().equals("")) {
                    holder.iv_unread_icon.setVisibility(View.GONE);
                }
                if (msg.content != null && !CMethod.isEmptyOrZero(msg.content)) {
                    L.e("msg.content"+msg.content);
                    playAudio(1, helper, holder, msg, position);

                } else {
                    L.e("语音地址为空");

                    if (holder != null) {
                        holder.pb_sending.setVisibility(View.GONE);
                        holder.iv_unread_icon.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

    }

    private void playAudio(int index, final MediaHelper helper,  final ViewHolder holder, final Message msg, final int position) {
        helper.playAudio(index,  msg, new MediaHelper.OnPlayStatusListener() {
            @Override
            public void onStart() {

                //开始播放
                if (holder.pb_sending != null) {
                    holder.pb_sending.setVisibility(View.GONE);
                }

                if (holder.iv_voice != null) {
                    holder.iv_voice.setBackgroundResource(R.mipmap.transparent_bg);
                    if (msg.getFrom_user().equals("")) {
                        holder.iv_voice.setImageResource(R.drawable.frame_progress_play_voice_send);
                    } else {
                        holder.iv_voice.setImageResource(R.drawable.frame_progress_play_voice_receive);
                    }
                    animationDrawable = (AnimationDrawable) holder.iv_voice.getDrawable();
                    animationDrawable.start();
                }
                //更新该条message状态
                if (!msg.getFrom_user().equals("")) {
//                                conversation.getAllMessage().get(position).setMessage_status(MessageContentStatus.RECEIVE_READ+""))
                    conversation.getAllMessage().get(position).setMessage_status(MessageContentStatus.RECEIVE_READ + "");
                    MessageUtils.updateMsgByUUID(msg.msg_id, MessageContentStatus.RECEIVE_READ + "", msg.content);
                }

            }

            @Override
            public void onEnd(String path) {
                //结束播放
                if (animationDrawable != null) {
                    if (!path.equals("stop")) {
                        if (!msg.getFrom_user().equals("")) {
                            int result = MessageUtils.updateMsgByUUID(msg.getMsg_id(), MessageContentStatus.RECEIVE_READ + "", path);
                        }
                    }
                    animationDrawable.stop();
                    holder.iv_voice.setBackgroundResource(R.mipmap.transparent_bg);
                    if (msg.getFrom_user().equals("")) {
                        holder.iv_voice.setBackgroundResource(R.mipmap.chatto_voice_playing_f3);
                    } else {
                        holder.iv_voice.setBackgroundResource(R.mipmap.receive_voice_icon_3);
                    }
                    animationDrawable = null;
                }

            }

            @Override
            public void onFail() {
//                T.s("播放失败");
//                if (try_this){
//                    playAudio(0,helper,msg.url,holder,msg,position,false);
//                }else {
                if (holder != null) {
                    if (holder.pb_sending != null) {
                        holder.pb_sending.setVisibility(View.GONE);
                    }
                    if (holder.iv_unread_icon != null) {
                        holder.iv_unread_icon.setVisibility(View.VISIBLE);
                    }
                }
                if (!msg.getFrom_user().equals("")) {
//                                conversation.getAllMessage().get(position).setMessage_status(MessageContentStatus.RECEIVE_READ+""))
                    conversation.getAllMessage().get(position).setMessage_status(MessageContentStatus.RECEIVE_READ + "");
                    MessageUtils.updateMsgByUUID(msg.msg_id, MessageContentStatus.RECEIVE_READ + "", msg.content);
                }
//                }
            }
        });


    }


    private void handleTextMessage(final Message msg, final ViewHolder holder, int position) {
        String content = "";
        if (msg.getFrom_user().equals("")) {
            L.e("content1"+msg.getDrict());
            L.e("content2"+msg.getFrom_user().equals(""));
            switch (msg.getMessage_status()) {
                case MessageContentStatus.SEND_FAIL:
                    holder.pb_sending.setVisibility(View.GONE);
                    holder.msg_status.setVisibility(View.VISIBLE);
                    break;
                case MessageContentStatus.SEND_INPROFRESS:
                    holder.pb_sending.setVisibility(View.VISIBLE);
                    holder.msg_status.setVisibility(View.GONE);
                    break;
                case MessageContentStatus.SEND_SUCCESS:
                    holder.pb_sending.setVisibility(View.GONE);
                    holder.msg_status.setVisibility(View.GONE);
                    break;
            }

            holder.msg_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CMethod.isFastDoubleClick()) {
                        return;
                    }

                    if (CMethod.isNet(mActivity)) {
                        holder.pb_sending.setVisibility(View.VISIBLE);
                        holder.msg_status.setVisibility(View.GONE);
                        mListener.resendText(msg);
                    } else {
                        T.s("请检查您的网络链接");
                    }
                }
            });
            content = msg.content;
            L.e("content"+content);
        } else {
            //可能要做解密的时候用
//            content =
        }

        holder.tv_chatcontent.setText(content);

    }


    public void sendMsgInBackground(final Message message, final ViewHolder holder) {
//        holder.msg_status.setVisibility(View.GONE);
//        holder.progressBar.setVisibility(View.VISIBLE);

        //重新发送消息
    }

}
