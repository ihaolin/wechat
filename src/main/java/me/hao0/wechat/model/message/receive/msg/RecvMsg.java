package me.hao0.wechat.model.message.receive.msg;

import me.hao0.wechat.model.message.receive.RecvMessage;

/**
 * 接收微信服务器的普通消息
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 9/11/15
 */
public class RecvMsg extends RecvMessage {

    private static final long serialVersionUID = 8863935279441026878L;

    /**
     * 消息ID
     */
    protected Long msgId;

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public RecvMsg(){}

    public RecvMsg(RecvMessage e){
        super(e);
    }

    @Override
    public String toString() {
        return "RecvMsg{" +
                "msgId=" + msgId +
                "} " + super.toString();
    }
}
