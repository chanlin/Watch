package com.jajale.watch.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.cviews.RoundedImageView;
import com.jajale.watch.entitydb.MsgMember;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by athena on 2015/11/25.
 * Email: lizhiqiang@bjjajale.com
 */
public class ContactAdapter extends BaseAdapter {
    private Activity mActivity;
    private List<MsgMember> mListContact;

    public ContactAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        this.mListContact = new ArrayList<MsgMember>();
    }


    @Override
    public int getCount() {
        return mListContact.size();
    }

    @Override
    public Object getItem(int position) {
        return mListContact.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MsgMember relation = mListContact.get(position);


//        int ResID = relation.getUser_identity() == 1 ? R.layout.listview_item_message_center_system : R.layout.listview_item_message_center_watch;



        L.e("getView is ==>" + position + "<======>" + relation.getUser_identity());

        ViewHolder holder = null;
        if (convertView == null) {

            convertView = LayoutInflater.from(mActivity).inflate(R.layout.listview_item_message_center_watch, null);



            holder = new ViewHolder();
            holder.iv_letter_contact_item_headphoto = (RoundedImageView) convertView.findViewById(R.id.iv_etter_self_person_item_headphoto);
            holder.tv_letter_contact_item_nickname = (TextView) convertView.findViewById(R.id.tv_letter_self_person_item_nickname);
            holder.tv_letter_contact_item_desc_text = (TextView) convertView.findViewById(R.id.tv_letter_self_person_item_desc_text);
            holder.tv_letter_self_person_item_time = (TextView) convertView.findViewById(R.id.tv_letter_self_person_item_time);
            holder.tv_letter_unReadCount = (TextView) convertView.findViewById(R.id.tv_msg_count_unread);



            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        android.widget.AbsListView.LayoutParams params = new android.widget.AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,Dp2Px(70));
        convertView.setLayoutParams(params);

        int resID = 0 ;
        if (relation.getUser_identity() == 1){
            resID =R.mipmap.icon_system_child;
        }else {
            resID =relation.getSex()== 0?R.mipmap.head_image_girl:R.mipmap.head_image_boy;

        }
        holder.iv_letter_contact_item_headphoto.setBackgroundResource(resID);

        if (relation.getUser_identity()==1){
            holder.tv_letter_contact_item_desc_text.setTextColor(mActivity.getResources().getColor(R.color.list_item_system_content_color));
        }else{
            holder.tv_letter_contact_item_desc_text.setTextColor(mActivity.getResources().getColor(R.color.gray_text_color));

        }

        String nickname = relation.getNick_name();
        L.e("nickname is :" + position + "<======>" + nickname);
        if (!CMethod.isEmpty(nickname)) {
            holder.tv_letter_contact_item_nickname.setText(nickname);
        }else{
            holder.tv_letter_contact_item_nickname.setText(R.string.family_member_baby_text);
        }

        String content = relation.getLast_msg_desc();
        L.e("content"+content);
        L.e("content is :" + position + "<======>" + content);
        if("system_clean".equals(content)){
            holder.tv_letter_contact_item_desc_text.setText("");
        }else {

            holder.tv_letter_contact_item_desc_text.setText(content);
        }

        String time = relation.getUpdate_time() + "";
        if (!CMethod.isEmptyOrZero(time)){
            holder.tv_letter_self_person_item_time.setText(CMethod.displayTime(relation.getUpdate_time(),null));
        }else {
            holder.tv_letter_self_person_item_time.setText("");
        }
        L.e("msg=====relation.getCount_unread()==" + relation.getCount_unread() + "");
        if (relation.getCount_unread() >0){
            holder.tv_letter_unReadCount.setVisibility(View.VISIBLE);
            holder.tv_letter_unReadCount.setText(relation.getCount_unread()+"");
        }else {
            holder.tv_letter_unReadCount.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    static class ViewHolder {
        public RoundedImageView iv_letter_contact_item_headphoto;
        public TextView tv_letter_contact_item_nickname;
        public TextView tv_letter_contact_item_desc_text;
        public TextView tv_letter_self_person_item_time;
        public TextView tv_letter_unReadCount ;


    }

    public void refresh(List<MsgMember> mListContacts) {
        if (mListContacts!=null && mListContact != null) {
            this.mListContact.clear();
            this.mListContact.addAll(mListContacts);
            this.notifyDataSetChanged();
        }

    }

    @Override
    public int getItemViewType(int position) {
//        if (mListContact.getAllMessage().size()>0){
//            Message message = conversation.getMessage(position);
//
//            switch (message.getContent_type()){
//                case MessageContentType.TEXT:
//                    return message.getDrict() ? 0 : 1;
//                case MessageContentType.VOICE:
//                    return message.getDrict() ? 2 : 3;
//            }
//        }
//        relation.getUser_identity() == 1
//        return mListContact.get(position).getUser_identity() == 1? 0 : 1;
        return  1;
//        return -1;// invalid
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public int Dp2Px(float dp) {
        final float scale = mActivity.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}
