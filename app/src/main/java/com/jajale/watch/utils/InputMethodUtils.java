package com.jajale.watch.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lilonghui on 2015/12/9.
 * Email:lilonghui@bjjajale.com
 */
public class InputMethodUtils {


    /***
     *
     * 打开软键盘
     * @param context
     * @param editText
     */
    public static void  openInputMethod(final Context context, final EditText editText)
    {
        Timer timer = new Timer(); //设置定时器
        timer.schedule(new TimerTask() {
            @Override
            public void run() { //弹出软键盘的代码
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        }, 50); //设置50毫秒的时长
    }


    /**
     * 关闭软键盘
     * @param context
     * @param editText
     */
    public static void  closeInputMethod(Context context,EditText editText)
    {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }
}
