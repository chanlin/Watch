package com.jajale.watch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.activity.GrowActivity;
import com.jajale.watch.entity.GrowRecordData;
import com.jajale.watch.entitydb.GrowRecord;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.GrowRecordUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 健康记录
 * Created by chunlongyuan on 11/24/15.
 */
public class HealthyRecordAdapter extends BaseAdapter {

    private Context context;
    final List<GrowRecord> records = new ArrayList<GrowRecord>();
    private final LayoutInflater layoutInflater;

    public HealthyRecordAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public GrowRecord getItem(int position) {
        return records.get(position);
    }

    public List<GrowRecord> getRecords() {
        return records;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        GrowRecord record = getItem(position);

        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = layoutInflater.inflate(R.layout.item_health_record, null);
            holder.tv_height = (TextView) convertView.findViewById(R.id.tv_height);
            holder.tv_weight = (TextView) convertView.findViewById(R.id.tv_weight);
            holder.tv_desc = (TextView) convertView.findViewById(R.id.tv_disc);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_date_label = (TextView) convertView.findViewById(R.id.tv_date_label);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.tv_height.setText("" + record.height);
        holder.tv_weight.setText("" + record.weight);

        if (!CMethod.isEmpty(record.create_time) && record.create_time.length() > 9) {
            holder.tv_date.setText(record.create_time.substring(0, 10));
        }
        if (!CMethod.isEmpty(record.content)) {
            holder.tv_desc.setText(record.content);
            holder.tv_desc.setVisibility(View.VISIBLE);
        } else {
            holder.tv_desc.setVisibility(View.INVISIBLE);
        }

        if (GrowActivity.currentWatch != null) {
            String age = GrowRecordUtils.getAgeByBirthday(record.create_time,GrowActivity.currentWatch.getBirthday());
            holder.tv_date_label.setText(age);
        }
        return convertView;
    }

    public void add(GrowRecord record) {
        records.add(record);
        notifyDataSetChanged();
    }

    public void updata(GrowRecord record) {
        for (GrowRecord growRecord : records) {
            if (record.growth_id.equals(growRecord.growth_id)) {
                growRecord.height = record.height;
                growRecord.weight = record.weight;
                growRecord.create_time = record.create_time;
                growRecord.content = record.content;
                notifyDataSetChanged();
                break;
            }
        }
    }

    public void remove(int position) {
        records.remove(position);
        notifyDataSetChanged();
    }


    public void clear() {
        this.records.clear();
        notifyDataSetChanged();
    }


    static class Holder {
        public TextView tv_height;
        public TextView tv_weight;
        public TextView tv_desc;
        public TextView tv_date;
        public TextView tv_date_label;
    }

    public void refresh(GrowRecordData growRecordData) {
        if (growRecordData != null && growRecordData.growthList != null && growRecordData.growthList.size() > 0) {
            this.records.clear();
            this.records.addAll(growRecordData.growthList);
            this.notifyDataSetChanged();
        }
    }

    public void loadNextPage(GrowRecordData growRecordData) {
        if (growRecordData != null && growRecordData.growthList != null && growRecordData.growthList.size() > 0) {
            this.records.addAll(growRecordData.growthList);
            this.notifyDataSetChanged();
        }
    }






}
