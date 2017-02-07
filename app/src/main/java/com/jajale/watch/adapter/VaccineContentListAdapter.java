package com.jajale.watch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jajale.watch.R;
import com.jajale.watch.entity.VaccineContentData;

import java.util.List;


public class VaccineContentListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    public List<VaccineContentData.VaccineListEntity> mVaccineList;

    private Context mcontext;

    private String null_date="0000-00-00";


    public VaccineContentListAdapter(Context context, List<VaccineContentData.VaccineListEntity> vaccineList) {
        this.mInflater = LayoutInflater.from(context);
        this.mVaccineList = vaccineList;

        this.mcontext = context;


    }

    public int getCount() {
        // TODO Auto-generated method stub
        return mVaccineList.size();
    }

    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return mVaccineList.get(arg0);
    }

    @SuppressWarnings("deprecation")
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = mInflater.inflate(R.layout.listview_item_vaccine_content, null);
            holder.vaccine_tv_vaccine_name = (TextView) view.findViewById(R.id.vaccine_tv_vaccine_name);
            holder.vaccine_tv_vaccine_state = (TextView) view.findViewById(R.id.vaccine_tv_vaccine_state);
            holder.vaccine_tv_vaccine_info = (TextView) view.findViewById(R.id.vaccine_tv_vaccine_info);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        holder.vaccine_tv_vaccine_name.setText(mVaccineList.get(position).getVaccineName());

        if (mVaccineList.get(position).getState() == 0) {

            holder.vaccine_tv_vaccine_state.setText("未接种");
            holder.vaccine_tv_vaccine_state.setTextColor(mcontext.getResources().getColor(R.color.app_color));
            holder.vaccine_tv_vaccine_state.setBackgroundResource(R.drawable.my_round_text_blue);

            if (mVaccineList.get(position).getTime().equals(null_date)){
                holder.vaccine_tv_vaccine_info.setVisibility(View.GONE);
            }else{
                holder.vaccine_tv_vaccine_info.setVisibility(View.VISIBLE);
                holder.vaccine_tv_vaccine_info.setText("记得在" + mVaccineList.get(position).getTime() + "接种哦");
            }

        } else {
            holder.vaccine_tv_vaccine_state.setText("已接种");
            holder.vaccine_tv_vaccine_state.setTextColor(mcontext.getResources().getColor(R.color.vaccine_content_state_off));
            holder.vaccine_tv_vaccine_state.setBackgroundResource(R.drawable.my_round_text_gray);
            if (mVaccineList.get(position).getTime().equals(null_date)){
                holder.vaccine_tv_vaccine_info.setVisibility(View.GONE);
            }else{
                holder.vaccine_tv_vaccine_info.setVisibility(View.VISIBLE);
                holder.vaccine_tv_vaccine_info.setText(mVaccineList.get(position).getTime());
            }

        }

//		if (position%2==0)
//		{
//			holder.vaccine_tv_vaccine_state.setText("已接种");
//			holder.vaccine_tv_vaccine_state.setTextColor(mcontext.getResources().getColor(R.color.vaccine_content_state_off));
//			holder.vaccine_tv_vaccine_state.setBackgroundResource(R.drawable.my_round_text_gray);
//			holder.vaccine_tv_vaccine_info.setVisibility(View.GONE);
//		}else {
//			holder.vaccine_tv_vaccine_state.setText("未接种");
//			holder.vaccine_tv_vaccine_state.setTextColor(mcontext.getResources().getColor(R.color.app_color));
//			holder.vaccine_tv_vaccine_state.setBackgroundResource(R.drawable.my_round_text_blue);
//			holder.vaccine_tv_vaccine_info.setVisibility(View.VISIBLE);
//		}

        return view;
    }

    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    public final class ViewHolder {
        public TextView vaccine_tv_vaccine_name;
        public TextView vaccine_tv_vaccine_info;
        public TextView vaccine_tv_vaccine_state;

    }
}
