package com.jajale.watch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.entitydb.NotDisturb;
import com.jajale.watch.listener.SettingSwitchListener;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.T;

import java.util.List;
import java.util.Map;


public class NotDisturbListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    public List<NotDisturb> notDisturbList;

    private SettingSwitchListener mListener;

    private Context mcontext;


    public NotDisturbListAdapter(Context context, List<NotDisturb> notDisturbList, SettingSwitchListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.notDisturbList = notDisturbList;
        this.mListener = listener;
        this.mcontext = context;


    }

    public int getCount() {
        // TODO Auto-generated method stub
        return notDisturbList.size();
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return notDisturbList.get(arg0);
    }

    @SuppressWarnings("deprecation")
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.listview_item_not_disturb, null);
            holder.not_disturb_iv_name = (TextView) view.findViewById(R.id.not_disturb_iv_name);
            holder.not_disturb_checkbox = (ImageView) view.findViewById(R.id.not_disturb_checkbox);
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
        final NotDisturb notDisturb = notDisturbList.get(position);
        holder.not_disturb_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CMethod.isFastDoubleClick()) {
                    return;
                }
                mListener.setSwitch(position, notDisturb.getOnOFF() == 1 ? 0 : 1);
            }
        });


        holder.not_disturb_iv_name.setText(notDisturb.getBeginTime() + "-" + notDisturb.getEndTime());
        if (notDisturb.getOnOFF() == 1)
            holder.not_disturb_checkbox.setImageResource(R.mipmap.switch_on);
        else
            holder.not_disturb_checkbox.setImageResource(R.mipmap.switch_off_);

        return view;
    }

    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    public final class ViewHolder {
        public TextView not_disturb_iv_name;
        public ImageView not_disturb_checkbox;
        public View line;

    }
}
