package com.jajale.watch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.entitydb.SmartWatch;

import java.util.List;

public class BabyChoosePopupWindowGirdViewAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<SmartWatch> mWatches;
    private int present_position;

    public BabyChoosePopupWindowGirdViewAdapter(Context context, int present_position, List<SmartWatch> watches) {
        this.mContext = context;
        this.mWatches = watches;
        this.present_position = present_position;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mWatches.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = convertView;
        final ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.girdview_item_dear_detail_head, null);
            holder.family_member_iv_head = (ImageView) view.findViewById(R.id.family_member_iv_head);
            holder.family_member_tv_head = (TextView) view.findViewById(R.id.family_member_tv_head);
            view.setTag(holder);
        }

        SmartWatch watch = mWatches.get(position);
        if (watch.getSex()==1) {
            holder.family_member_iv_head.setImageResource(R.drawable.dear_deatil_boy_head_view_selector);
            if (present_position == position) {
                holder.family_member_iv_head.setImageResource(R.mipmap.dear_detail_boy_head_view_press);
            }
        } else {
            holder.family_member_iv_head.setImageResource(R.drawable.dear_deatil_girl_head_view_selector);
            if (present_position == position) {
                holder.family_member_iv_head.setImageResource(R.mipmap.dear_detail_girl_head_view_press);
            }
        }


        holder.family_member_tv_head.setText(watch.getNick_name());


        return view;
    }

    public class ViewHolder {
        public ImageView family_member_iv_head;
        public TextView family_member_tv_head;
    }
}
