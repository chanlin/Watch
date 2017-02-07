package com.jajale.watch.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jajale.watch.AppConstants;
import com.jajale.watch.CodeConstants;
import com.jajale.watch.R;
import com.jajale.watch.activity.NormalWebViewActivity;
import com.jajale.watch.cviews.RoundedImageView;
import com.jajale.watch.entity.InstructionData;
import com.jajale.watch.entitydb.Message;
import com.jajale.watch.entitydb.MsgMember;
import com.jajale.watch.entityno.MessageContentType;
import com.jajale.watch.factory.GsonFactory;
import com.jajale.watch.listener.HttpClientListener;
import com.jajale.watch.listener.MessageCommandListener;
import com.jajale.watch.listener.SimpleClickListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.DialogUtils;
import com.jajale.watch.utils.HttpUtils;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.LoadingDialog;
import com.jajale.watch.utils.T;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by athena on 2015/11/26.
 * Email: lizhiqiang@bjjajale.com
 */
public class SystemMessageAdapter extends BaseAdapter {
    private Activity mActivity;
    private ArrayList<Message> mListContact;
    private MsgMember msgMember;
    private LayoutInflater mInflater;
    private MessageCommandListener mListener;
    private LoadingDialog loadingDialog;


    public SystemMessageAdapter(Activity mActivity, MsgMember msgMember, MessageCommandListener listener) {
        this.mActivity = mActivity;
        this.mListContact = new ArrayList<Message>();
        this.msgMember = msgMember;
        this.mListener = listener;
        this.mInflater = LayoutInflater.from(mActivity);
        loadingDialog = new LoadingDialog(mActivity);
    }

    @Override
    public int getCount() {
        return mListContact.size();
    }

    @Override
    public Message getItem(int position) {
        return mListContact.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Message msg = mListContact.get(position);
//        L.e("getView is ==>" + position + "<======>" + relation.getUser_identity());
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mActivity).inflate(R.layout.listview_item_row_sys_msg, null);

            holder.nriv_userhead = (RoundedImageView) convertView.findViewById(R.id.nriv_userhead);
            holder.tv_chatcontent = (TextView) convertView.findViewById(R.id.tv_chatcontent);
            holder.timestamp = (TextView) convertView.findViewById(R.id.timestamp);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.nriv_userhead.setBackgroundResource(R.mipmap.icon_system_child);
        holder.timestamp.setText(CMethod.displayTime(Long.parseLong(msg.getCreate_time()), null));


        //1聊天，2：安全区域报警，3：低电量报警，4：SOS报警
        switch (msg.getMessage_type()) {
            case 2:
                holder.tv_chatcontent.setText(msg.content);
                Drawable drawable_location_enter = mActivity.getResources().getDrawable(R.mipmap.icon_location_warning);
                drawable_location_enter.setBounds(0, 0, drawable_location_enter.getMinimumWidth(), drawable_location_enter.getMinimumHeight());
                holder.tv_chatcontent.setCompoundDrawables(drawable_location_enter, null, null, null);
                break;
            case 3:
                holder.tv_chatcontent.setText(msg.content);
                Drawable drawable_bettraty = mActivity.getResources().getDrawable(R.mipmap.icon_battery_warning);
                drawable_bettraty.setBounds(0, 0, drawable_bettraty.getMinimumWidth(), drawable_bettraty.getMinimumHeight());
                holder.tv_chatcontent.setCompoundDrawables(drawable_bettraty, null, null, null);
                break;
            case 4:
                holder.tv_chatcontent.setText(msg.content);
                Drawable drawable_sos = mActivity.getResources().getDrawable(R.mipmap.icon_sos_warning);
                drawable_sos.setBounds(0, 0, drawable_sos.getMinimumWidth(), drawable_sos.getMinimumHeight());
                holder.tv_chatcontent.setCompoundDrawables(drawable_sos, null, null, null);

                break;

            case 0:
                String html = "您在使用中遇到的任何问题都可在"+"<\n";
                html+="<a href='http://xxxxx'>使用说明</a>"+">中进行查询";//注意这里必须加上协议号，即http://

                CharSequence charSequence = Html.fromHtml(html);
                holder.tv_chatcontent.setText(charSequence);
                holder.tv_chatcontent.setCompoundDrawables(null, null, null, null);
                break;


        }
        final ViewHolder finalHolder = holder;
        holder.tv_chatcontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(finalHolder.tv_chatcontent.getText().toString().contains("使用说明"))){
                    return;
                }
                getInstructionFormNetwork();
            }
        });

        holder.tv_chatcontent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DialogUtils.deleteMsgDialog(mActivity, new SimpleClickListener() {
                    @Override
                    public void ok() {
                        L.e("确定");
                        if (mListener != null) {
                            mListener.deleteMsg(position);
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
        return convertView;
    }


    /**
     * 获取问题列表并保存
     */
    private void getInstructionFormNetwork() {


        loadingDialog.show();

        HttpUtils.PostDataToWeb(AppConstants.JAVA_GET_ENTRANCE_URL, CodeConstants.GET_INSTRUCTIONS_CODE, null, new HttpClientListener() {
            @Override
            public void onSuccess(String result) {
                loadingDialog.dismiss();
                InstructionData data = GsonFactory.newInstance().fromJson(result, InstructionData.class);
                openNewsActivityV2(data.getReq_url(), data.getMo_name());
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

    private void openNewsActivityV2(String url, String title) {
        Intent i = new Intent();
        i.setClass(mActivity, NormalWebViewActivity.class);
        i.putExtra("info_url", url);
        i.putExtra("info_title", title);
        mActivity.startActivity(i);
    }

    public void refresh(List<Message> mListContacts) {
        if (mListContacts != null)
            if (mListContacts.size() > mListContact.size()) {
                this.mListContact.clear();
                this.mListContact.addAll(mListContacts);
                this.notifyDataSetChanged();
            }

    }

    static class ViewHolder {
        public RoundedImageView nriv_userhead;
        public TextView tv_chatcontent;
        public TextView timestamp;
    }

    //
    private View createViewByMessage(Message message, int position) {
        switch (message.getContent_type()) {
            case MessageContentType.SYS_SOS:
                return mInflater.inflate(R.layout.listview_item_row_sys_msg_receive_sos, null);
            case MessageContentType.SYS_BETTRATY:
                return mInflater.inflate(R.layout.listview_item_row_sys_msg_receive_battery, null);
            case MessageContentType.SYS_LOCATION:
                return mInflater.inflate(R.layout.listview_item_row_sys_msg_receive_location, null);
            case MessageContentType.SYS_RELEASE:
                return mInflater.inflate(R.layout.listview_item_row_sys_msg_welcome, null);
            case MessageContentType.SYS_LOCATION_ENTER:
                return mInflater.inflate(R.layout.listview_item_row_sys_msg_receive_location_enter, null);
            case MessageContentType.SYS_WELCOME:
                return mInflater.inflate(R.layout.listview_item_row_sys_msg_welcome, null);

        }
        return mInflater.inflate(R.layout.listview_item_row_sys_msg_receive_battery, null);
    }

    public void deleteIndexMsg(int index) {
        mListContact.remove(index);
        notifyDataSetChanged();
    }

}
