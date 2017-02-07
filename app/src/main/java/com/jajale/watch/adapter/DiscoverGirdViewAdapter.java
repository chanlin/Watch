package com.jajale.watch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.entity.DiscoverData;
import com.jajale.watch.image.JJLImageLoader;

/**
 * Created by athena on 2016/2/17.
 * Email: lizhiqiang@bjjajale.com
 */
public class DiscoverGirdViewAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context mContext;
    public boolean[] isShoDot;
    public DiscoverData discoverList;

    public DiscoverGirdViewAdapter(DiscoverData discoverList, Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.discoverList =discoverList;
        isShoDot=new boolean[discoverList.getModeList().size()];
        for (int i = 0; i < discoverList.getModeList().size(); i++) {
            isShoDot[i]=false;
        }
    }

    @Override
    public int getCount() {
        return discoverList.getModeList().size();
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
            holder.iv_dot = (ImageView) view.findViewById(R.id.iv_item_dot);
            view.setTag(holder);
        }

        DiscoverData.ModeListEntity entity=discoverList.getModeList().get(position);

        if (isShoDot[position]){
            holder.iv_dot.setVisibility(View.VISIBLE);
        }else{
            holder.iv_dot.setVisibility(View.GONE);
        }

        JJLImageLoader.download(mContext, entity.getMo_icon(), holder.iv_icon);
//        holder.iv_icon.setImageResource(mHeadView[position]);
        holder.tv_desc.setText(entity.getMo_name());



        return view;
    }


//    public void showDot(int positon){
//        isShoDot[positon]=true;
//            notifyDataSetChanged();
//    }
//    public void disDot(int positon){
//        isShoDot[positon]=false;
//        notifyDataSetChanged();
//    }



    public class ViewHolder {
        public ImageView iv_icon;
        public ImageView iv_dot;
        public TextView tv_desc;
    }
}
