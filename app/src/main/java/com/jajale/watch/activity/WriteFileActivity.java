package com.jajale.watch.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jajale.watch.R;
import com.jajale.watch.entityno.BaseArea;
import com.jajale.watch.fragment.BaseAreaManager;

import java.io.FileOutputStream;

/**
 * Created by athena on 2015/12/16.
 * Email: lizhiqiang@bjjajale.com
 */
public class WriteFileActivity extends Activity{

    private TextView tv_right ;

    private BaseArea baseArea = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_msg);
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setText("执行写入文件");


        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                写入文件
                baseArea = BaseAreaManager.getBaseArea(WriteFileActivity.this);

                Gson gson = new Gson();
                String reuslt = gson.toJson(baseArea);


                String fileName = "CITY_JSON_TEST.txt";

                writeFileData(fileName, reuslt);



            }
        });

    }


    public void writeFileData(String fileName,String message){

        try{

            FileOutputStream fout =openFileOutput(fileName, MODE_PRIVATE);

            byte [] bytes = message.getBytes();

            fout.write(bytes);

            fout.close();

        }

        catch(Exception e){

            e.printStackTrace();

        }

    }




}
