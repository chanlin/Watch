package com.jajale.watch.adapter;

import android.app.Activity;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import com.jajale.watch.R;
import com.jajale.watch.entity.BaseEntity;

import java.util.List;

/**
 * Created by athena on 2015/12/5.
 * Email: lizhiqiang@bjjajale.com
 */
public class InsuranceInfoAdapter extends BaseAdapter{

    private Activity mActivity;
    private List<BaseEntity> mListContact;
    private LayoutInflater mInflater;


    public InsuranceInfoAdapter(Activity mActivity, List<BaseEntity> mListContact) {
        this.mActivity = mActivity;
        this.mListContact = mListContact;
        this.mInflater = LayoutInflater.from(mActivity);
    }

    @Override
    public int getCount() {
        return mListContact.size();
    }

    @Override
    public Object getItem(int position) {
        return mListContact.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final BaseEntity entity = (BaseEntity) getItem(position);
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = createViewByPosition(position);

            holder.tv_insurance_title_left = (TextView) convertView.findViewById(R.id.tv_insurance_title_left);
            holder.tv_insurance_title_right = (TextView) convertView.findViewById(R.id.tv_insurance_title_right);

            holder.radio_father = (RadioButton) convertView.findViewById(R.id.radio_father);
            holder.radio_mother = (RadioButton) convertView.findViewById(R.id.radio_mother);

            holder.et_insurance_right_value = (EditText) convertView.findViewById(R.id.et_insurance_right_value);

            holder.btn_insurance_confirm = (Button) convertView.findViewById(R.id.btn_insurance_confirm);

            holder.tv_insurance_area_value = (TextView) convertView.findViewById(R.id.tv_insurance_area_value);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (holder.tv_insurance_title_left!= null){
            holder.tv_insurance_title_left.setText(mListContact.get(position).getKey());
        }
        if (holder.tv_insurance_title_right!= null){
            holder.tv_insurance_title_right.setText(mListContact.get(position).getValue());
        }
        if (holder.et_insurance_right_value !=null){
            SpannableString ss = new SpannableString(mListContact.get(position).getValue());
            holder.et_insurance_right_value.setHint(new SpannableString(ss));
        }

        if (holder.btn_insurance_confirm != null){
            holder.btn_insurance_confirm.setText(mListContact.get(position).getKey());
        }

        if (holder.tv_insurance_area_value != null){
            holder.tv_insurance_area_value.setText(mListContact.get(position).getValue());
        }



        return convertView;
    }


    private View createViewByPosition( int position) {
        int resId = 0;
        switch (position){
            case 0:
            case 10:
                resId = R.layout.item_insurance_title;
                break;
            case 1:
                resId = R.layout.item_insurance_choice;
                break;
            case 7:
            case 13:
                resId = R.layout.item_insurance_area;
                break;
            case 14:
                resId = R.layout.item_insurance_button;
                break;
            default:
                resId = R.layout.item_insurance_edit;
                break;

        }
//        resId = R.layout.item_insurance_title;


        return mInflater.inflate(resId,null);
    }

    static class ViewHolder {

        public TextView tv_insurance_title_left;
        public TextView tv_insurance_title_right ;

        public RadioButton radio_father ;
        public RadioButton radio_mother ;

        public EditText et_insurance_right_value;

        public Button btn_insurance_confirm ;

        public TextView tv_insurance_area_value ;

    }


    @Override
    public int getItemViewType(int position) {
       int type = -1 ;
        switch (position){
          case 0: //title
          case 10:
              type = 0 ;
            break;
          case 1:// choice
              type = 1;
              break;
          case 7:// area
          case 13:
              type = 2;
              break;
          case 14:
              type = 3;
              break;
          default:
              type = 4;
              break;
       }
        return type;// invalid
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }


}
