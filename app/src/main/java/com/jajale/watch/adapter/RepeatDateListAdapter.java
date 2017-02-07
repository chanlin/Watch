package com.jajale.watch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.R;


public class RepeatDateListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	public String [] list;
	public boolean [] booleans;

	private Context mcontext;



	public RepeatDateListAdapter(Context context, String[] list,boolean [] booleans) {
		this.mInflater = LayoutInflater.from(context);
		this.list = list;
		this.booleans = booleans;
		this.mcontext = context;


	}

	public int getCount() {
		// TODO Auto-generated method stub
		return list.length;
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list[arg0];
	}

	@SuppressWarnings("deprecation")
	public View getView(final int position, View convertView, ViewGroup parent) {

		View view = convertView;
		ViewHolder holder = null;
		if (view == null) {
			holder = new ViewHolder();
			view = mInflater.inflate(R.layout.listview_item_repeat_date, null);
			holder.repeat_tv_week = (TextView) view.findViewById(R.id.repeat_tv_week);
			holder.repeat_checkbox = (ImageView) view.findViewById(R.id.repeat_checkbox);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}


		holder.repeat_tv_week.setText(list[position]);
		if (booleans[position])
		{
			holder.repeat_checkbox.setImageResource(R.mipmap.radio_button_on);
		}else{
			holder.repeat_checkbox.setImageResource(R.mipmap.radio_button_off);
		}

		return view;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public final class ViewHolder {
		public TextView repeat_tv_week;
		public ImageView repeat_checkbox;

	}
}
