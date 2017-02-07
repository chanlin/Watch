package com.jajale.watch.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.jajale.watch.BaseActivity;
import com.jajale.watch.IntentAction;
import com.jajale.watch.R;
import com.jajale.watch.utils.CMethod;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 添加家庭成员
 * <p/>
 * Created by lilonghui on 2015/11/27.
 * Email:lilonghui@bjjajale.com
 */
public class AddFamilyMemberActivity extends BaseActivity implements OnClickListener {

    //测试生成二维码
    String codeContent = "";
    @InjectView(R.id.iv_left)
    ImageView ivLeft;
    @InjectView(R.id.tv_left_2)
    TextView tvLeft2;
    @InjectView(R.id.tv_middle)
    TextView tvMiddle;
    @InjectView(R.id.tv_right)
    TextView tvRight;
    @InjectView(R.id.iv_right)
    ImageView ivRight;
    @InjectView(R.id.code_image)
    ImageView codeImage;
    @InjectView(R.id.text_imei)
    TextView textImei;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_family_member);
        ButterKnife.inject(this);
        codeContent = getIntent().getStringExtra(IntentAction.KEY_IMEI_CODE);
        tvMiddle.setText(getResources().getString(R.string.add_family_member_title_text));
        ivLeft.setImageResource(R.drawable.title_goback_selector);
        ivLeft.setOnClickListener(this);
        Bitmap qrCode = generateQRCode(codeContent);
        codeImage.setImageBitmap(qrCode);
        textImei.setText("IMEI:"+codeContent);
    }

    @Override
    public void onClick(View v) {
        if (CMethod.isFastDoubleClick()){
            return;
        }
        switch (v.getId()) {
            case R.id.iv_left:
                finish();
                break;
        }

    }

    private Bitmap bitMatrix2Bitmap(BitMatrix matrix) {
        int w = matrix.getWidth();
        int h = matrix.getHeight();
        int[] rawData = new int[w * h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int color = Color.WHITE;

                if (matrix.get(i, j)) {
                    color = Color.BLACK;
                }
                rawData[i + (j * w)] = color;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        bitmap.setPixels(rawData, 0, w, 0, 0, w, h);
        return bitmap;
    }

    private Bitmap generateQRCode(String content) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            // MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, 700, 700);
            return bitMatrix2Bitmap(matrix);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

}
