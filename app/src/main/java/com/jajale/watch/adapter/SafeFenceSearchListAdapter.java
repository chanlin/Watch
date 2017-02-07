package com.jajale.watch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.help.Tip;
import com.jajale.watch.R;

import java.util.List;


public class SafeFenceSearchListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;


    private Context mcontext;
    private List<Tip> mList;


    public SafeFenceSearchListAdapter(Context context,List<Tip> list) {
        this.mInflater = LayoutInflater.from(context);
        this.mList = list;
        this.mcontext = context;


    }

    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    public Tip getItem(int arg0) {
        // TODO Auto-generated method stub
        return mList.get(arg0);
    }

    @SuppressWarnings("deprecation")
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.listview_item_safe_fence_search, null);

            holder.tv_1 = (TextView) view.findViewById(R.id.tv_1);
            holder.tv_2 = (TextView) view.findViewById(R.id.tv_2);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        Tip tip=mList.get(position);
        holder.tv_1.setText(tip.getName());
        holder.tv_2.setText(tip.getDistrict());

        return view;
    }

    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    public final class ViewHolder {
        public TextView tv_1;
        public TextView tv_2;
    }
}
