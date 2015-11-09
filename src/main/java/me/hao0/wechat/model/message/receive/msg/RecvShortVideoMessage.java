package me.hao0.wechat.model.message.receive.msg;

import me.hao0.wechat.model.message.receive.RecvMessageType;

/**
 * 小视频消息
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 9/11/15
 */
public class RecvShortVideoMessage extends RecvVideoMessage {

    private static final long serialVersionUID = 4589295453710532536L;

    public RecvShortVideoMessage(RecvMsg m){
        super(m);
        this.msgId = m.msgId;
    }

    @Override
    public String getMsgType() {
        return RecvMessageType.SHORT_VIDEO.value();
    }
}
