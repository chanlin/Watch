package com.jajale.watch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.entity.VaccineAgeData;

import java.util.List;


public class VaccineAgeListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	public List<VaccineAgeData.VaccineAgeListEntity> mVaccineAgeList;
	public Boolean [] booleans;
	private Context mcontext;



	public VaccineAgeListAdapter(Context context, List<VaccineAgeData.VaccineAgeListEntity> vaccineAgeList,Boolean[] booleans) {
		this.mInflater = LayoutInflater.from(context);
		this.mVaccineAgeList = vaccineAgeList;
		this.booleans = booleans;
		this.mcontext = context;


	}

	public int getCount() {
		// TODO Auto-generated method stub
		return mVaccineAgeList.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mVaccineAgeList.get(arg0);
	}

	@SuppressWarnings("deprecation")
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		ViewHolder holder = null;
		if (view == null) {
			holder = new ViewHolder();
			view = mInflater.inflate(R.layout.listview_item_vaccine_age, null);
			holder.age = (TextView) view.findViewById(R.id.vaccine_tv_age);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		if (booleans[position]){
			holder.age.setBackgroundColor(mcontext.getResources().getColor(R.color.vaccine_age_list_press_color));
		}
		else{
			holder.age.setBackgroundColor(mcontext.getResources().getColor(R.color.vaccine_age_list_normal_color));

		}
		holder.age.setText(mVaccineAgeList.get(position).getAge());
		return view;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public final class ViewHolder {
		public TextView age;

	}
}
