package com.jajale.watch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.utils.ParentHeadViewUtils;

public class FamilyMemberHeadGirdViewAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private Context mContext;
	public  String[] mName;
	public  int [] mHeadView;


	public FamilyMemberHeadGirdViewAdapter(Context context) {
		this.mContext = context;
		this.mName = context.getResources().getStringArray(R.array.family_member_relation);
		this.mHeadView = ParentHeadViewUtils.mHeadView;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mName.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mName[position];
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
			view = mInflater.inflate(R.layout.girdview_item_family_head, null);
			holder.family_member_iv_head = (ImageView) view.findViewById(R.id.family_member_iv_head);
			holder.family_member_tv_head = (TextView) view.findViewById(R.id.family_member_tv_head);
			view.setTag(holder);
		}


		holder.family_member_iv_head.setImageResource(mHeadView[position]);
		holder.family_member_tv_head.setText(mName[position]);



		return view;
	}

	public class ViewHolder {
		public ImageView family_member_iv_head;
		public TextView family_member_tv_head;
	}
}
