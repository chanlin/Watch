package com.jajale.watch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.entity.SafeFenceData;

import java.util.List;


public class SafeFenceListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;


    private Context mcontext;
    public List<SafeFenceData.SafeListEntity> mSafeListEntities;


    public SafeFenceListAdapter(Context context, List<SafeFenceData.SafeListEntity> safeListEntities) {
        this.mInflater = LayoutInflater.from(context);
        this.mSafeListEntities = safeListEntities;
        this.mcontext = context;


    }

    public int getCount() {
        // TODO Auto-generated method stub
        return mSafeListEntities.size();
    }

    public SafeFenceData.SafeListEntity getItem(int arg0) {
        // TODO Auto-generated method stub
        return mSafeListEntities.get(arg0);
    }

    @SuppressWarnings("deprecation")
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.listview_item_safe_fence, null);
            holder.safe_fence_tv_address = (TextView) view.findViewById(R.id.safe_fence_tv_address);
            holder.safe_fence_tv_range = (TextView) view.findViewById(R.id.safe_fence_tv_range);
            holder.safe_fence_tv_address_name = (TextView) view.findViewById(R.id.safe_fence_tv_address_name);
            holder.safe_fence_iv_address = (ImageView) view.findViewById(R.id.safe_fence_iv_address);
//            holder.line = view.findViewById(R.id.line);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

//        if (position == 0) {
//            holder.line.setVisibility(View.INVISIBLE);
//        } else {
//            holder.line.setVisibility(View.VISIBLE);
//        }

        SafeFenceData.SafeListEntity safeListEntity=mSafeListEntities.get(position);
        if ((mcontext.getResources().getString(R.string.home)).equals(safeListEntity.getSafe_title())){
            holder.safe_fence_iv_address.setImageResource(R.mipmap.safe_fence_home);
        }else if((mcontext.getResources().getString(R.string.school)).equals(safeListEntity.getSafe_title())){
            holder.safe_fence_iv_address.setImageResource(R.mipmap.safe_fence_school);
        }else{
            holder.safe_fence_iv_address.setImageResource(R.mipmap.safe_fence_other);
        }

        holder.safe_fence_tv_address.setText(safeListEntity.getAddress());
        holder.safe_fence_tv_address_name.setText(safeListEntity.getSafe_title());
        holder.safe_fence_tv_range.setText(safeListEntity.getRadius()+"m");


        return view;
    }

    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    public  void deleteItem(int arg0){
        mSafeListEntities.remove(arg0);
        this.notifyDataSetChanged();
    }


    public final class ViewHolder {
        public TextView safe_fence_tv_address;
        public TextView safe_fence_tv_address_name;
        public TextView safe_fence_tv_range;
        public ImageView safe_fence_iv_address;
//        public View line;
    }
}
