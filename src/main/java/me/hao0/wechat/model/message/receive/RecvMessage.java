package me.hao0.wechat.model.message.receive;

import java.io.Serializable;

/**
 * 接收微信服务器的消息
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 9/11/15
 */
public class RecvMessage implements Serializable {

    /**
     * 开发者微信号
     */
    protected String toUserName;

    /**
     * 用户openId
     */
    protected String fromUserName;

    /**
     * 消息创建时间
     */
    protected Integer createTime;

    /**
     * 消息类型:
     * @see me.hao0.wechat.model.message.resp.RespMessageType
     */
    protected String msgType;

    public RecvMessage(){}

    public RecvMessage(RecvMessage m){
        this.toUserName = m.toUserName;
        this.fromUserName = m.fromUserName;
        this.createTime = m.createTime;
        this.msgType = m.msgType;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgType(){
        return this.msgType;
    }

    @Override
    public String toString() {
        return "RecvMessage{" +
                "toUserName='" + toUserName + '\'' +
                ", fromUserName='" + fromUserName + '\'' +
                ", createTime=" + createTime +
                ", msgType='" + msgType + '\'' +
                '}';
    }
}
