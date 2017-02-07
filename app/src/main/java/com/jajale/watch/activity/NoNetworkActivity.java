package com.jajale.watch.activity;

import android.view.View;
import android.widget.Button;

import com.jajale.watch.BaseActivity;
import com.jajale.watch.R;
import com.jajale.watch.utils.CMethod;

/**
 * Created by lilonghui on 2016/2/1.
 * Email:lilonghui@bjjajale.com
 */
public class NoNetworkActivity extends BaseActivity {
    public void showNotworkView() {
        try {
            View view = findViewById(R.id.layout_no_network);
            if (view != null) {
                if (CMethod.isNet(this)) {
                    view.setVisibility(View.GONE);
                } else {
                    view.setVisibility(View.VISIBLE);
                }

                Button button = (Button) view.findViewById(R.id.refresh_btn);
                if (button != null)
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showNotworkView();
                            refreshView();
                        }
                    });
            }
        } catch (ClassCastException e) {

        }
    }

    public void refreshView() {

    }
}
