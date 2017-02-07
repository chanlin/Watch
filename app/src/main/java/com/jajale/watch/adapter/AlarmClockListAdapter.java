package com.jajale.watch.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.entitydb.Clock;
import com.jajale.watch.listener.SettingSwitchListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.L;

import java.util.List;
import java.util.Map;


public class AlarmClockListAdapter extends BaseAdapter {
    public static String weeks[] = new String[7];
    private LayoutInflater mInflater;
    public List<Clock> clocks;

    private Context mcontext;
    private SettingSwitchListener mListener;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    public AlarmClockListAdapter(Context context, List<Clock> clocks, SettingSwitchListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.clocks = clocks;
        this.mListener = listener;
        this.mcontext = context;
        sp = context.getSharedPreferences("NotDisturb", context.MODE_PRIVATE);// 创建对象
        editor = sp.edit();
        weeks = new String[]{mcontext.getString(R.string.sunday), mcontext.getString(R.string.monday),
                mcontext.getString(R.string.tuesday), mcontext.getString(R.string.wednesday),
                mcontext.getString(R.string.thursday), mcontext.getString(R.string.friday),
                mcontext.getString(R.string.saturday)};
    }


    public int getCount() {
        // TODO Auto-generated method stub
        return clocks.size();
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return clocks.get(arg0);
    }

    @SuppressWarnings("deprecation")
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.listview_item_alarm_clock, null);
            holder.alarm_tv_time = (TextView) view.findViewById(R.id.alarm_tv_time);
            holder.alarm_tv_repeat = (TextView) view.findViewById(R.id.alarm_tv_repeat);
            holder.alarm_checkbox = (ImageView) view.findViewById(R.id.alarm_checkbox);
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

        holder.alarm_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CMethod.isFastDoubleClick()) {
                    return;
                }
                mListener.setSwitch(position, clocks.get(position).getOnOFF() == 1 ? 0 : 1);
            }
        });


        //以下计算为将boolean数组转化为字符串，全0为不重复，全1为每天，其余显示周一，三。。
        String values = clocks.get(position).getSetValues();
        String[] strings = values.split("");

        if (strings.length == 8) {
            boolean isCheckAll = true;
            String back_string = " ";
            for (int i = 1; i < strings.length; i++) {
                if (strings[i].equals("1")) {
                    back_string = back_string + weeks[i - 1].replace("周", "") + "、";
                } else {
                    isCheckAll = false;
                }
            }
            back_string = back_string.substring(0, back_string.length() - 1);
            if (isCheckAll) {
                holder.alarm_tv_repeat.setText(mcontext.getString(R.string.everyday));
            } else if ("".equals(back_string)){
                holder.alarm_tv_repeat.setText(mcontext.getString(R.string.no_repeat));
            } else {
                holder.alarm_tv_repeat.setText(back_string);
            }
//            back_string = isCheckAll ? "每天" : back_string.equals("") ? "不重复" : back_string;
//            holder.alarm_tv_repeat.setText(back_string);
        }

        holder.alarm_tv_time.setText(clocks.get(position).getTime());
        if (clocks.get(position).getOnOFF() == 1)
            holder.alarm_checkbox.setImageResource(R.mipmap.switch_on);
        else
            holder.alarm_checkbox.setImageResource(R.mipmap.switch_off_);

        return view;
    }

    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    public final class ViewHolder {
        public TextView alarm_tv_repeat;
        public TextView alarm_tv_time;
        public ImageView alarm_checkbox;
        public View line;

    }
}
