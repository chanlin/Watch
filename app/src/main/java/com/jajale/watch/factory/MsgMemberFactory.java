package com.jajale.watch.factory;

import com.jajale.watch.AppConstants;
import com.jajale.watch.entity.ReturnMessageData;
import com.jajale.watch.entitydb.MsgMember;
import com.jajale.watch.entitydb.SmartWatch;
import com.jajale.watch.entityno.MessageContentType;
import com.jajale.watch.utils.L;
import com.jajale.watch.utils.MessageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lilonghui on 2016/3/1.
 * Email:lilonghui@bjjajale.com
 */
public class MsgMemberFactory {

    public static MsgMemberFactory getInstance() {
        return new MsgMemberFactory();
    }

    public MsgMember createMsgMemberBySmartWatch(SmartWatch watch) {
        MsgMember msgMember_ = MessageUtils.getMsgMemberByUserId(watch.getUser_id());
        if (msgMember_ == null) {
            MsgMember msgMember = new MsgMember();
            msgMember.setUser_id(watch.getUser_id());
            msgMember.setBirthday(watch.getBirthday());
            msgMember.setElectricities(watch.getElectricities());
            msgMember.setGps_lat(watch.getGps_lat());
            msgMember.setGps_lon(watch.getGps_lon());
            msgMember.setHeader_img_url(watch.getHeader_img_url());
            msgMember.setIs_manage(watch.getIs_manage());
            msgMember.setNick_name(watch.getNick_name());
            msgMember.setPhone_num_binded(watch.getPhone_num_binded());
            msgMember.setRelation(watch.getRelation());
            msgMember.setRole_type(watch.getRole_type());
            msgMember.setSex(watch.getSex());
            msgMember.setWatch_imei_binded(watch.getWatch_imei_binded());
            msgMember.setUser_identity(2);
            msgMember.setCreate_time(System.currentTimeMillis());
            msgMember.setUpdate_time("");
            msgMember.setCount_unread(0);
            msgMember.setCount_msg(0);
            return msgMember;
        }
        msgMember_.setNick_name(watch.getNick_name());
        msgMember_.setPhone_num_binded(watch.getPhone_num_binded());
        msgMember_.setGps_lat(watch.getGps_lat());
        msgMember_.setGps_lon(watch.getGps_lon());
        msgMember_.setRelation(watch.getRelation());
        msgMember_.setIs_manage(watch.getIs_manage());
        msgMember_.setSex(watch.getSex());
        msgMember_.setBirthday(watch.getBirthday());
        return msgMember_;
    }


    public List<MsgMember> createMsgMemberListByReturnMsgList(List<ReturnMessageData> returnMessageDataList) {
        List<MsgMember> list = MessageUtils.getMsgMemberList();
        L.e("123=======list.size()=11=" + list.size());
//        int msg_about_system = 0;
        //手表消息
        for (MsgMember mesMember : list) {
            int msg_about_watch = 0;
            int msg_about_system = 0;


            for (ReturnMessageData returnMessageData : returnMessageDataList) {

                int msg_type = returnMessageData.getMsg_type();
                if (msg_type == 1 && mesMember.getUser_id().equals(returnMessageData.getSend_user())) {//聊天消息
                    msg_about_watch++;
                    long updateTime = returnMessageData.getTime_stamp();
                    String lastMsgDesc = returnMessageData.getContent_type() == MessageContentType.VOICE ? "[语音]" : returnMessageData.getContent();
                    mesMember.setUpdate_time(updateTime);
                    mesMember.setLast_msg_desc(lastMsgDesc);
                }
                if (mesMember.getUser_id().equals(AppConstants.SYSTEM_WATCH_ID) && msg_type != 1) {
                    msg_about_system++;

                    long updateTime = returnMessageData.getTime_stamp();
                    String lastMsgDesc = returnMessageData.getContent();
                    mesMember.setUpdate_time(updateTime);
                    mesMember.setLast_msg_desc(lastMsgDesc);
                }
            }


            if (msg_about_watch > 0) {
                int watchMessageCount = mesMember.getCount_msg() + msg_about_watch;
                mesMember.setCount_msg(watchMessageCount);
                mesMember.setCount_unread(mesMember.getCount_unread() + msg_about_watch);
            }


            if (mesMember.getUser_id().equals(AppConstants.SYSTEM_WATCH_ID) && msg_about_system > 0) {
                int systemMessageCount = mesMember.getCount_msg() + msg_about_system;
                mesMember.setCount_msg(systemMessageCount);
                mesMember.setCount_unread(mesMember.getCount_unread() + msg_about_system);
            }
        }

        L.e("123=======list.size()=22=" + list.size());
        return list;

    }


    public List<MsgMember> createMsgMemberListBySmartWatchList(List<SmartWatch> smartWatchList) {

        List<MsgMember> list = new ArrayList<MsgMember>();
        for (SmartWatch smartWatch : smartWatchList) {
            MsgMember msgMember = createMsgMemberBySmartWatch(smartWatch);
            list.add(msgMember);
        }
        list.add(createSystemRelation());
        return list;
    }

    public MsgMember createSystemRelation() {
        MsgMember msgMember_ = MessageUtils.getMsgMemberByUserId(AppConstants.SYSTEM_WATCH_ID);
        if (msgMember_ == null) {
            MsgMember msgMember = new MsgMember();
            msgMember.setUser_id(AppConstants.SYSTEM_WATCH_ID);
            msgMember.setNick_name("系统消息");
            msgMember.setUser_identity(1);

            msgMember.setCreate_time(System.currentTimeMillis());//创建时间
            msgMember.setUpdate_time("");//更新时间
            msgMember.setCount_unread(0);//未读消息数
            msgMember.setCount_msg(0);//消息总数
            msgMember.setLast_msg_desc("");//最后一封消息的描述
            msgMember.setLast_msg_type(MessageContentType.TEXT + "");//最后一封消息的类型
            return msgMember;
        }
        return msgMember_;

    }

}
