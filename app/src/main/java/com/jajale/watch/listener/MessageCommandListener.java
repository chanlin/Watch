package com.jajale.watch.listener;

import com.jajale.watch.entitydb.Message;

/**
 * Created by athena on 2015/11/27.
 * Email: lizhiqiang@bjjajale.com
 */
public interface MessageCommandListener {
    void deleteMsg(int index);
    void resendText(Message resend_msg);
    void resendVoice(Message resend_voice);
}
