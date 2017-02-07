package com.jajale.watch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jajale.watch.R;
import com.jajale.watch.entity.LocationUserInfoEntity;

import java.util.ArrayList;
import java.util.List;

public class MapChildsHeadGirdViewAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    public List<LocationUserInfoEntity> mList;
    public List<Boolean> mImage_bs = new ArrayList<Boolean>();

    public MapChildsHeadGirdViewAdapter(Context context, List<LocationUserInfoEntity> list) {
        this.mContext = context;
        this.mList = list;
        this.mInflater = LayoutInflater.from(context);
        for (int i = 0; i < list.size(); i++)
            mImage_bs.add(false);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
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
            view = mInflater.inflate(R.layout.girdview_item_map_child, null);
            holder.imageView = (ImageView) view.findViewById(R.id.image_head);
            view.setTag(holder);
        }
        int resId;
        if (position < mImage_bs.size()) {
            resId = mImage_bs.get(position) ? mList.get(position).getHeadView_press()
                    : mList.get(position).getHeadView();
        } else {
            resId = mList.get(position).getHeadView();
        }

        holder.imageView.setImageResource(resId);


        return view;
    }

    public void notifyData(List<LocationUserInfoEntity> list, int position) {
        if (list != null && list.size() != 0) {
            this.mList = list;
            this.mImage_bs.clear();
            for (int i = 0; i < list.size(); i++) {
                if (i == position) {
                    this.mImage_bs.add(true);
                } else {
                    this.mImage_bs.add(false);
                }
            }

            this.notifyDataSetChanged();
        }
    }


    public class ViewHolder {
        public ImageView imageView;
    }
}
