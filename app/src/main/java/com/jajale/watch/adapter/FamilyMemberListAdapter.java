package com.jajale.watch.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.BaseApplication;
import com.jajale.watch.R;
import com.jajale.watch.entitydb.FamilyMember;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.ParentHeadViewUtils;

import java.util.List;


public class FamilyMemberListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    public List<FamilyMember> mFamilyList;

    private Context mcontext;
    private String userId;

    SharedPreferences sp;
    SharedPreferences.Editor editor;


    public FamilyMemberListAdapter(Context context, List<FamilyMember> familyList) {
        this.mInflater = LayoutInflater.from(context);
        this.mFamilyList = familyList;
        this.mcontext = context;
//        userId = BaseApplication.getUserInfo().userID;
        sp = mcontext.getSharedPreferences("NotDisturb", mcontext.MODE_PRIVATE);// 创建对象
        editor = sp.edit();
        userId = sp.getString("Telephone_Number", "");

    }

    public int getCount() {
        // TODO Auto-generated method stub
        return mFamilyList.size();
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return mFamilyList.get(arg0);
    }

    @SuppressWarnings("deprecation")
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.listview_item_family_member, null);
            holder.family_member_iv_name = (TextView) view.findViewById(R.id.family_member_iv_name);
            holder.family_member_iv_phone = (TextView) view.findViewById(R.id.family_member_iv_phone);
            holder.family_member_iv_head = (ImageView) view.findViewById(R.id.family_member_iv_head);
            holder.line = view.findViewById(R.id.line);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (position == 0) {
            holder.line.setVisibility(View.INVISIBLE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }


        String relation = mFamilyList.get(position).getRelation();
        //如果是本人
        if (mFamilyList.get(position).getUser_id().equals(userId)) {
            relation= CMethod.isEmpty(relation)? mcontext.getResources().getString(R.string.relationship):relation;
            relation = mcontext.getResources().getString(R.string.me) + "(" + relation + ")";
        }
        //如果是管理员
        if (mFamilyList.get(position).getIsManage() == 1) {
            relation = relation + "&nbsp&nbsp<font color=#2fbbef>" + mcontext.getResources().getString(R.string.administrator) + "</font>";
        }

        if (relation != null)
            holder.family_member_iv_name.setText(Html.fromHtml(relation));


        holder.family_member_iv_phone.setText(mFamilyList.get(position).getPhone());
        int headView = ParentHeadViewUtils.getImage(mFamilyList.get(position).getRelation());
        holder.family_member_iv_head.setImageResource(headView == 0 ? R.mipmap.head_image_father : headView);


        return view;
    }

    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    public final class ViewHolder {
        public TextView family_member_iv_name;
        public TextView family_member_iv_phone;
        public ImageView family_member_iv_head;
        public View line;

    }
}
