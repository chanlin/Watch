package com.jajale.watch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.utils.CalendarUtil;

import java.util.List;

public class CalenderGirdViewAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    public List<CalendarUtil.CalendarData> mCalendarLists;


    public CalenderGirdViewAdapter(Context context, List<CalendarUtil.CalendarData> calendarLists) {
        this.mContext = context;
        this.mCalendarLists = calendarLists;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mCalendarLists.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mCalendarLists.get(position);
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
            view = mInflater.inflate(R.layout.girdview_item_calendar, null);
            holder.calendar_iv_bg = (ImageView) view.findViewById(R.id.calendar_iv_bg);
            holder.calendar_iv_select = (ImageView) view.findViewById(R.id.calendar_iv_select);
            holder.calendar_tv_hint = (TextView) view.findViewById(R.id.calendar_tv_hint);
            holder.calendar_tv_day = (TextView) view.findViewById(R.id.calendar_tv_day);
            view.setTag(holder);
        }
        CalendarUtil.CalendarData data = mCalendarLists.get(position);
        holder.calendar_tv_day.setText(data.getDay());
        holder.calendar_tv_hint.setText(data.getMonth());
        if (data.isToday()) {
            holder.calendar_iv_select.setVisibility(View.VISIBLE);
        } else {
            holder.calendar_iv_select.setVisibility(View.GONE);
        }
        if (data.isSelect()) {
            holder.calendar_iv_bg.setVisibility(View.VISIBLE);
        } else {
            holder.calendar_iv_bg.setVisibility(View.GONE);
        }
        if (data.isCanSelect()) {
            holder.calendar_tv_day.setTextColor(mContext.getResources().getColor(R.color.black_text_color));
        } else {
            holder.calendar_tv_day.setTextColor(mContext.getResources().getColor(R.color.gray_text_color));
        }




        return view;
    }

    public class ViewHolder {
        public ImageView calendar_iv_select;
        public ImageView calendar_iv_bg;
        public TextView calendar_tv_hint;
        public TextView calendar_tv_day;
    }
}
