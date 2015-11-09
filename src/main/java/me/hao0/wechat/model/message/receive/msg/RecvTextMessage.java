package me.hao0.wechat.model.message.receive.msg;

import me.hao0.wechat.model.message.receive.RecvMessageType;

/**
 * 文本消息
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 9/11/15
 */
public class RecvTextMessage extends RecvMsg {

    private static final long serialVersionUID = -8070100690774814611L;

    /**
     * 文本内容
     */
    private String content;

    public RecvTextMessage(RecvMsg m){
        super(m);
        this.msgId = m.msgId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getMsgType() {
        return RecvMessageType.TEXT.value();
    }

    @Override
    public String toString() {
        return "RecvTextMessage{" +
                "content='" + content + '\'' +
                "} " + super.toString();
    }
}
