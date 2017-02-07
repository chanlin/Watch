package com.jajale.watch.activity;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jajale.watch.BaseActivity;
import com.jajale.watch.R;
import com.jajale.watch.utils.CMethod;


/**
 * 添加手表
 */
public class AddWatchActivity extends BaseActivity implements View.OnClickListener {

    private Button add_watch_addimei_btn;
    private Button add_watch_qrcode_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addwatch);

        add_watch_addimei_btn = (Button) findViewById(R.id.add_watch_addimei_btn);
        add_watch_qrcode_btn = (Button) findViewById(R.id.add_watch_qrcode_btn);
        add_watch_addimei_btn.setOnClickListener(this);
        add_watch_qrcode_btn.setOnClickListener(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==1)
            if (data!=null)
                Toast.makeText(AddWatchActivity.this, data.getExtras().getString("QR_code"), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

        if (CMethod.isFastDoubleClick()){
            return;
        }

        switch (v.getId()) {
            case R.id.add_watch_addimei_btn://通过填写IMEI添加手表


                break;
            case R.id.add_watch_qrcode_btn://通过扫描二维码添加手表
                Intent openCameraIntent = new Intent(this, ScannerActivity.class);
                startActivityForResult(openCameraIntent,1);
                break;
        }

    }
}
