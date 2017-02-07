package com.jajale.watch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.entity.GrowPageEntity;
import com.jajale.watch.entitydb.SmartWatch;

/**
 * 健康选择宝贝adapter
 * Created by chunlongyuan on 11/26/15.
 */
public class GrowChoiceBabyAdapter extends BaseAdapter {

    private Context context;
    //    private List<Watch> entities;
    private GrowPageEntity pageEntity = new GrowPageEntity();
    private final LayoutInflater layoutInflater;
//    private int selectedPosition = 0;

    public GrowChoiceBabyAdapter(Context context, GrowPageEntity entities) {
        this.context = context;
        this.pageEntity = entities;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
//        return entities/..size();
        return pageEntity.getWatches().size();
    }

    @Override
    public Object getItem(int position) {
        return pageEntity.getWatches().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        SmartWatch entity = pageEntity.getWatches().get(position);


        if (convertView == null) {

            holder = new ViewHolder();

            convertView = layoutInflater.inflate(R.layout.item_choice_baby, null);
            holder.iv_head = (ImageView) convertView.findViewById(R.id.iv_head);
            holder.tv_label = (TextView) convertView.findViewById(R.id.tv_label);
            holder.iv_selected = (ImageView) convertView.findViewById(R.id.iv_selected);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_label.setText(entity.getNick_name());
        holder.iv_selected.setBackgroundResource(R.mipmap.transparent_bg);
        if ("1".equals(entity.getSex())) {
            holder.iv_head.setBackgroundResource(R.mipmap.head_image_boy);
        } else {
            holder.iv_head.setBackgroundResource(R.mipmap.head_image_girl);
        }
//        selectedPosition = (CMethod.isEmpty(entity.extra)) ? 0 : Integer.parseInt(entity.extra);

        if (pageEntity.getCurrent_index() == position) {
            holder.iv_selected.setBackgroundResource(R.mipmap.item_selected);
            entity.extra = position + "";
        }


        return convertView;
    }

    public boolean isPositionSelected(int position) {
        return position == pageEntity.getCurrent_index();
    }

    public void cancelOtherSelected() {
        pageEntity.getWatches().get(pageEntity.getCurrent_index()).extra = null;
    }

    public void selectItem(int position) {
        pageEntity.getWatches().get(position).extra = "selected";
    }


    static class ViewHolder {
        public ImageView iv_head;
        public TextView tv_label;
        public ImageView iv_selected;
    }
}
