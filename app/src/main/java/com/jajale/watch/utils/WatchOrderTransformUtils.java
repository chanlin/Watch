package com.jajale.watch.utils;

/**
 * Created by lilonghui on 2015/11/26.
 * Email:lilonghui@bjjajale.com
 */
public class WatchOrderTransformUtils {


    public   static String  TransForm(String  order)
    {
        String  back_string="";

        if (order.contains("*39*0"))
        {
            back_string="手表不在线";
        }else if(order.contains("*31"))
        {
            back_string="找手表指令发送成功";
        }else if(order.contains("*41*")){
            back_string="指令发送成功";
        }else if(order.contains("*40"))
        {
            back_string="回复出厂设置成功";
        }else if(order.contains("*25"))
        {
            back_string="远程关机成功";
        }else if(order.contains("*34*"))
        {
            back_string="闹钟设置成功";
        }else if(order.contains("*1*"))
        {
            back_string="指令发送成功";

        }else if(order.contains("*28*1")){
            back_string = "收到新语音";
        }

        return back_string;
    }



    private void setPedometrOnOff()
    {



    }







}
