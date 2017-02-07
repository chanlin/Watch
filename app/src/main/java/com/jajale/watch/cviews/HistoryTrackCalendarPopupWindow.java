package com.jajale.watch.cviews;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.jajale.watch.R;
import com.jajale.watch.adapter.CalenderGirdViewAdapter;
import com.jajale.watch.utils.CalendarUtil;

import java.util.List;


public class HistoryTrackCalendarPopupWindow extends PopupWindow {

    private View mMenuView;
    private final GridView calendar_gv;
    private final CalenderGirdViewAdapter adapter;


    @SuppressWarnings({"deprecation", "deprecation"})
    public HistoryTrackCalendarPopupWindow(Activity context, List<CalendarUtil.CalendarData> calendarLists) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.layout_calendar, null);

        calendar_gv = (GridView) mMenuView.findViewById(R.id.calendar_gv);
        adapter = new CalenderGirdViewAdapter(context,calendarLists);
        calendar_gv.setAdapter(adapter);
        calendar_gv.setOnItemClickListener((AdapterView.OnItemClickListener) context);


        this.setContentView(mMenuView);
        this.setWidth(LayoutParams.FILL_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.PopupWindowAnimation);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);
        mMenuView.setFocusableInTouchMode(true);
        mMenuView.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_MENU) {
                    dismiss();
                }
                return false;
            }
        });

    }

    public void notifyAdapter(int selectPosition){
        if (adapter==null||adapter.mCalendarLists==null||adapter.mCalendarLists.size()==0)
            return;

        for (int i = 0; i < adapter.mCalendarLists.size(); i++) {
                if (selectPosition==i){
                    adapter.mCalendarLists.get(i).setIsSelect(true);
                }else{
                    adapter.mCalendarLists.get(i).setIsSelect(false);
                }
        }
        adapter.notifyDataSetChanged();

    }

}
