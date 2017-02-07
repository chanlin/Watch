package com.jajale.watch.cviews;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jajale.watch.R;
import com.jajale.watch.entity.BookListData;
import com.jajale.watch.entity.SPKeyConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ReadView extends View {
    private Bitmap mCurrentPageBitmap;
    private Canvas mCurrentPageCanvas;
    private PageFactory pagefactory;
    private int font_size = 50;
    private SharedPreferences sp;
    private int[] position = new int[]{0, 0};
    private int width;
    private int height;
    private OnPopupWindowListener mListener;
    private String mPath;
    private List<BookListData> mBooklist;
    private String jsonString;
    private int list_length = 0;

    public interface OnPopupWindowListener {
        public void onShowPopupWindow();
    }

    public ReadView(Context context, String path) {
        super(context);
        this.mListener = (OnPopupWindowListener) context;
        this.mPath = path;
        setBookList();
        sp = context.getSharedPreferences(SPKeyConstants.BOOK_SP_NAME, Context.MODE_PRIVATE);
//		font_size = sp.getInt("font_size", 50);
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        width = manager.getDefaultDisplay().getWidth();
        height = manager.getDefaultDisplay().getHeight();

        mCurrentPageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCurrentPageCanvas = new Canvas(mCurrentPageBitmap);
        pagefactory = new PageFactory(context, jsonString, width, height);
        try {
            //首次进入，将初始位置设置为增加前段目录json数据偏移量
            int mPosition=mBooklist == null ? 0 : mBooklist.get(0).getOffset_b() + list_length;
            position[0] = sp.getInt(path + "begin", mPosition);
            position[1] = sp.getInt(path + "end", 0);
            pagefactory.setBgBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.reader_bg));
            pagefactory.openBook(path, position);
            pagefactory.onDraw(mCurrentPageCanvas,position[0]== mPosition);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.drawBitmap(mCurrentPageBitmap, 0, 0, null);
        canvas.restore();
    }

    public void setBackGround(Bitmap bitmap) {
        pagefactory.setBgBitmap(bitmap);
    }

    public void setDrawBitMap(Bitmap bitmap) {
        mCurrentPageBitmap = bitmap;
    }

    /**
     * 获取书籍目录及解析成list数据
     */
    public void setBookList() {
        try {
            File file = new File(mPath);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            jsonString = br.readLine();


            Matcher m = Pattern.compile("<#JSON#>(.*?)<#JSON#>").matcher(jsonString);
            while (m.find())
                jsonString = m.group(1);
            list_length = jsonString.getBytes().length + "<#JSON#>".getBytes().length * 2;
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonArray jsonArray = parser.parse(jsonString).getAsJsonArray();
            mBooklist = new ArrayList<BookListData>();
            for (JsonElement obj : jsonArray) {
                BookListData cse = gson.fromJson(obj, BookListData.class);
                mBooklist.add(cse);
            }
            jsonString = jsonString.replace("<#JSON#>", "");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<BookListData> getBookList() {
        return mBooklist;
    }

    public int getListLength() {
        return list_length;
    }



    public void setOnPause() {
        position = pagefactory.getPosition();
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(mPath + "begin", position[0]);
        editor.putInt(mPath + "end", position[1]);
        editor.apply();
        int fontSize = pagefactory.getTextFont();
        SharedPreferences.Editor editor2 = sp.edit();
        editor2.putInt("font_size", fontSize);
        editor2.apply();
    }

    public void setFont_size(int font_size) {
        pagefactory.setTextFont(font_size);
    }

    public void setPresent(int present) {
        pagefactory.setPercent(present);
    }

    public void refresh() {
        pagefactory.onDraw(mCurrentPageCanvas,false);
        setDrawBitMap(mCurrentPageBitmap);
        invalidate();
    }

    public void setToPage(int offset) {

    }

    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    /**
     * 手指触摸监听
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候
            x1 = event.getX();
            y1 = event.getY();
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            x2 = event.getX();
            y2 = event.getY();
            if (x1 - x2 > 50) {
                pagefactory.nextPage();
                pagefactory.onDraw(mCurrentPageCanvas,false);
                setDrawBitMap(mCurrentPageBitmap);
            } else if (x2 - x1 > 50) {
                pagefactory.prePage();
                pagefactory.onDraw(mCurrentPageCanvas,false);
                setDrawBitMap(mCurrentPageBitmap);
            } else {
                if (event.getX() > width / 3 * 2) {
                    pagefactory.nextPage();
                    pagefactory.onDraw(mCurrentPageCanvas,false);
                    setDrawBitMap(mCurrentPageBitmap);
                } else if (event.getX() < width / 3) {
                    pagefactory.prePage();
                    pagefactory.onDraw(mCurrentPageCanvas,false);
                    setDrawBitMap(mCurrentPageBitmap);
                } else {
                    mListener.onShowPopupWindow();
                }
            }

            invalidate();

        }
        return false;
    }


}
