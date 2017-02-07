package com.jajale.watch.cviews;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.jajale.watch.R;
import com.jajale.watch.utils.PhotoUtils;


/**
 * Created by pig on 3/6/15.
 * 上传头像dialog
 */
public class UploadPicDialog extends Dialog implements View.OnClickListener {

    private Activity mActivity;

    public UploadPicDialog(Activity activity) {
        super(activity);

        mActivity = activity;

        setContentView(R.layout.insert_picture_dialog_layout);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(params);
        setCanceledOnTouchOutside(true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.take_picture).setOnClickListener(this);
        findViewById(R.id.local_picture).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_picture:
                dismiss();
                PhotoUtils.toCamera(mActivity);
                break;
            case R.id.local_picture:
                dismiss();
                PhotoUtils.toPhoto(mActivity);
                break;
        }
    }


}
