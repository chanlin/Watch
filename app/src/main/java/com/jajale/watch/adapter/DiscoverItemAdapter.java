package com.jajale.watch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.R;

/**
 * Created by athena on 2016/2/17.
 * Email: lizhiqiang@bjjajale.com
 */
public class DiscoverItemAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    public  String[] mName;
    public  int [] mHeadView;

    public DiscoverItemAdapter(String [] des,int [] icons,Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mName = des;
        this.mHeadView = icons;


    }

    @Override
    public int getCount() {
        return mName.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.layout_item_discover, null);
            holder.iv_icon = (ImageView) view.findViewById(R.id.iv_item_icon);
            holder.tv_desc = (TextView) view.findViewById(R.id.tv_item_desc);
            view.setTag(holder);
        }

        holder.iv_icon.setImageResource(mHeadView[position]);
        holder.tv_desc.setText(mName[position]);



        return view;
    }



    public class ViewHolder {
        public ImageView iv_icon;
        public TextView tv_desc;
    }
}
