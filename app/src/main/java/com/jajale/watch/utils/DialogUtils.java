package com.jajale.watch.utils;

import android.app.Activity;
import android.content.Context;
import android.text.Html;

import com.jajale.watch.R;
import com.jajale.watch.cviews.UploadPicDialog;
import com.jajale.watch.dialog.AreaDialog;
import com.jajale.watch.dialog.BirthdayDialog;
import com.jajale.watch.dialog.ChangeNickDialog;
import com.jajale.watch.dialog.ChangePhoneDialog;
import com.jajale.watch.dialog.FamilySettingDialog;
import com.jajale.watch.dialog.MontitorDialog;
import com.jajale.watch.dialog.MoveDataDialog;
import com.jajale.watch.dialog.SelectTimeDialog;
import com.jajale.watch.dialog.SimpleBaseDialog;
import com.jajale.watch.dialog.SimpleNoTitleBaseDialog;
import com.jajale.watch.dialog.SimpleSingleMsgDialog;
import com.jajale.watch.dialog.SimpleVersionDialog;
import com.jajale.watch.dialog.SingleChoiceDialog;
import com.jajale.watch.dialog.StringDialog;
import com.jajale.watch.listener.AreaListener;
import com.jajale.watch.dialog.VaccineSelectDialog;
import com.jajale.watch.listener.FamilyDialogListener;
import com.jajale.watch.listener.SimpleClickListener;
import com.jajale.watch.listener.SingleStringListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by athena on 2015/11/19.
 * Email: lizhiqiang@bjjajale.com
 */
public class DialogUtils {
    /**
     * 家庭成员管理弹出框
     *
     * @param context
     * @param listener
     */
    public static void familyMemberSetting(Context context, int type, FamilyDialogListener listener) {
        new FamilySettingDialog(context, type, listener).show();
    }
    /**
     * 地区选框
     *
     * @param context
     */
    public static void monitorDialog(Context context) {
        new MontitorDialog(context).show();
    }
    /**
     * 修改昵称
     *
     * @param context
     * @param listener
     */
    public static void changeNickname(Context context, SingleStringListener listener) {
        new ChangeNickDialog(context, listener, context.getResources().getString(R.string.nick_name), context.getResources().getString(R.string.nick_name_), context.getResources().getString(R.string.nick_name_hint)).show();
    }

    /**
     * 家庭成员关系
     *
     * @param context
     * @param listener
     */
    public static void editRelation(Context context, SingleStringListener listener) {
        new ChangeNickDialog(context, listener,context.getResources().getString(R.string.relation_baby),context.getResources().getString(R.string.input_relation),context.getResources().getString(R.string.relation_words)).show();
    }
    /**
     * 修改手机号
     *
     * @param context
     * @param listener
     */
    public static void changePhone(Context context, SingleStringListener listener) {
        new ChangePhoneDialog(context, listener, context.getResources().getString(R.string.phone_number), context.getResources().getString(R.string.phone_number_hint), "").show();
    }

    /**
     * 生日选框
     *
     * @param context
     */
    public static void birthdayDialog(Context context, String title,String birthday, int differ,SingleStringListener listener) {
        if (CMethod.isEmpty(birthday)){
            birthday = CMethod.getFullDay();
        }
        new BirthdayDialog(context, title,birthday, differ,listener).show();

    }

    /**
     * 选择时段
     *
     * @param context
     */
    public static void selectTimeDialog(Context context, String title,String  time,SingleStringListener listener) {
        new SelectTimeDialog(context, title,time,listener).show();
    }

    public static void switchAccountDialog(Context context, SimpleClickListener listener) {
        new SimpleBaseDialog(context, "解除绑定", "当前帐号退出后", "是否确定切换帐号?", "取消", "确定", listener).show();
    }
    /**
     * 解除绑定
     *
     * @param context
     */
    public static void removeBindDialog(Context context, SimpleClickListener listener) {
        new SimpleBaseDialog(context, context.getResources().getString(R.string.remove_binding), context.getResources().getString(R.string.remove_binding_), "", context.getResources().getString(R.string.quite), context.getResources().getString(R.string.ok), listener).show();
    }
    /**
     * 数据迁移失败弹出框
     *
     * @param context
     */
    public static void moveDataFailDialog(Context context, SimpleClickListener listener) {
        new MoveDataDialog(context, "提示", "数据更新失败，请重试", "", "重试", "请联系客服", listener).show();
    }

    /**
     * 数据迁移失败弹出框
     *
     * @param context
     */
    public static void resetPasswordFailDialog(Context context, SimpleClickListener listener) {
        new MoveDataDialog(context, "提示", "数据尚未迁移", "", "取消", "请联系客服", listener).show();
    }

    /**
     * 下载提示
     *
     * @param context
     */
    public static void loadingBookDialog(Context context, SimpleClickListener listener) {
        new SimpleBaseDialog(context, "温馨提示", "当前正在使用数据流量，下载图书可能会产生流量费用，是否继续？", "", "稍后阅读", "继续阅读", listener).show();
    }
    /**
     * 远程关机
     * @param context
     * @param listener
     */
    public static void remoteShutdownDialog(Context context, SimpleClickListener listener) {
        new SimpleBaseDialog(context, context.getResources().getString(R.string.Remote_Shutdown), context.getString(R.string.close_watch), "", context.getResources().getString(R.string.quite), context.getResources().getString(R.string.ok), listener).show();
    }
    public static void WIFISelectedDialog(Context context, SimpleClickListener listener) {
        new SimpleBaseDialog(context, "未打开WIFI", "当前WIFI未连接，观看需消耗数据流量?", "确定使用数据流量观看？", "取消", "继续", listener).show();
    }

    //奖励小红花
    public static void add_honorDialog(Context context, SimpleClickListener listener) {
        new SimpleBaseDialog(context, context.getResources().getString(R.string.award_flower_), context.getResources().getString(R.string.award_flower), "", context.getResources().getString(R.string.quite), context.getResources().getString(R.string.award), listener).show();
    }

    //奖励小红花
    public static void clear_honorDialog(Context context, SimpleClickListener listener) {
        new SimpleBaseDialog(context, context.getResources().getString(R.string.flower_clear), context.getResources().getString(R.string.flower_clear_), "", context.getResources().getString(R.string.quite), context.getResources().getString(R.string.reset_flower), listener).show();
    }

    /**
     * 退出登录
     *
     * @param context
     */
    public static void logOffDialog(Context context, SimpleClickListener listener) {
        new SimpleBaseDialog(context, context.getResources().getString(R.string.exit_account), context.getString(R.string.exit_account_), "", context.getResources().getString(R.string.quite), context.getResources().getString(R.string.ok), listener).show();
    }
    /**
     *疫苗接种选择
     *
     * @param context
     */
    public static void vaccineSelectDialog(Context context, String  title,SimpleClickListener listener) {
        new VaccineSelectDialog(context,title, listener).show();
    }
    /**
     * 版本更新
     *
     * @param context
     */
    public static void versionUpdateDialog(Context context, String version,String message,SimpleClickListener listener) {
        new SimpleVersionDialog(context ,"发现新版本"+version, Html.fromHtml(message), "", "取消", "马上更新",0 ,listener).show();
    }

    public static void versionForceUpdateDialog(Context context, String version,String message,SimpleClickListener listener){
        new SimpleVersionDialog(context ,"发现新版本"+version, Html.fromHtml(message), "","", "马上更新",1,listener).show();

    }

//    SingleChoiceDialog

    public static void showManagePriorityDialog(Context context, SimpleClickListener listener){
        new SingleChoiceDialog(context,"","投保请联系手表管理员","","确定",listener).show();
    }

    public static void NOInsuranceDialog(Context context, SimpleClickListener listener){
        new SingleChoiceDialog(context,"","您购买的套餐无保险服务,如需开通请拨打","010-84922888","确定",listener).show();
    }


    public static void deleteMsgDialog(Context context, SimpleClickListener listener) {
        new SimpleSingleMsgDialog(context, context.getResources().getString(R.string.delete_message), "", context.getResources().getString(R.string.quite), context.getResources().getString(R.string.ok), listener).show();
    }

    public static void deleteConvarsationDialog(Context context, SimpleClickListener listener) {
        new SimpleSingleMsgDialog(context, context.getResources().getString(R.string.delete_all_message), "", context.getResources().getString(R.string.quite), context.getResources().getString(R.string.ok), listener).show();
    }

    public static void editGrowRecordDialog(Context context, SimpleClickListener listener) {
        new SimpleSingleMsgDialog(context, "您确定要更新该条记录吗？", "", "取消", "确定", listener).show();
    }
    public static void deleteGrowRecordDialog(Context context, SimpleClickListener listener) {
        new SimpleSingleMsgDialog(context, "您确定要删除该条记录吗？", "", "取消", "确定", listener).show();
    }
    public static void deleteSaveFenceDialog(Context context, SimpleClickListener listener) {
        new SimpleSingleMsgDialog(context, context.getResources().getString(R.string.delete_zone), "", context.getResources().getString(R.string.quite), context.getResources().getString(R.string.ok), listener).show();
    }

    /**
     * 地区选框
     *
     * @param context
     */
    public static void areaDialog(Context context, AreaListener listener) {
        new AreaDialog(context, listener).show();
    }

    /**
     * 证件选择
     * @param context
     * @param type
     * @param listener
     */
    public static void idTypeDialog(Context context,int type,SingleStringListener listener ){
        List<String> types = new ArrayList<String>();
        if (type == 1){
            types.add("军官证");
        }
        types.add("护照");
        types.add("身份证");
        types.add("港澳通行证");
        types.add("台湾通行证");
        new StringDialog(context, types, "","证件类型", "", listener).show();

    }

    /**
     * 解除绑定
     *
     * @param context
     */
    public static void drawInsuranceDialog(Context context, SimpleClickListener listener) {
        new SimpleNoTitleBaseDialog(context, "", "是否免费领取儿童意外重疾", "综合保险", "否", "是", listener).show();
    }

    /**
     * 选取图片
     * @param activity
     */
    public static void uploadPicDialog(Activity activity) {
        new UploadPicDialog(activity).show();
    }
/*
    /**
     * 选取图片（不裁剪）
     * @param activity
     * @param b
     *//*
    public static void uploadPicDialog(Activity activity,boolean b) {
        new UploadPicDialog(activity,b).show();
    }*/



}
