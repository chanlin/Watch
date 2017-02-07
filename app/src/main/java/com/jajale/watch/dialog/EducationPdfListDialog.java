package com.jajale.watch.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jajale.watch.R;
import com.jajale.watch.adapter.PDfListAdapter;

/**
 * 教育，阅读pdf弹出框
 */
public class EducationPdfListDialog extends Dialog {

    private Context mContext;
    String[] names = new String[]{"语文（一）", "语文（二）", "语文（三）", "语文（四）"};
    private AdapterView.OnItemClickListener mListener;
    private String  book_url ;
    private int  book_id ;
    private String  book_name ;

    public EducationPdfListDialog(Context context,String book_name,String url,int book_id,AdapterView.OnItemClickListener listener) {
        super(context);
        this.mContext = context;
        this.book_url = url;
        this.book_name = book_name;
        this.book_id = book_id;
        this.mListener = listener;


        setContentView(R.layout.dialog_education_pdf_list);
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

        ListView listView = (ListView) findViewById(R.id.listView);
        PDfListAdapter adapter = new PDfListAdapter(mContext, getNames(),book_id+"");
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(mListener);

    }

    private int getNumber(){
        try {
            String string = book_url.substring(book_url.lastIndexOf("_") + 1);
            String str_number=string.split("\\.")[0];
            int number=Integer.parseInt(str_number);
            return number;
        }catch (Exception e){

        }

        return 0;
    }
    private String[] getNames(){

        String[] names=new String[getNumber()];
        for (int i = 0; i < getNumber(); i++) {
            names[i]=book_name+"（"+(i+1)+"）";
        }
        return names;
    }

}
