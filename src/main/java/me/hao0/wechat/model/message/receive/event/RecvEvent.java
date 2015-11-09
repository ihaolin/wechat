package me.hao0.wechat.model.message.receive.event;

import me.hao0.wechat.model.message.receive.RecvMessage;
import me.hao0.wechat.model.message.receive.RecvMessageType;

/**
 * 接收微信服务器的事件消息
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 9/11/15
 */
public class RecvEvent extends RecvMessage {

    /**
     * 事件类型:
     * @see RecvEvent
     */
    protected String eventType;



    public RecvEvent(){}

    public RecvEvent(RecvMessage e){
        super(e);
    }

    public void setEventType(String eventType){
        this.eventType = eventType;
    }

    public String getEventType(){
        return this.eventType;
    }

    @Override
    public String getMsgType() {
        return RecvMessageType.EVENT.value();
    }

    @Override
    public String toString() {
        return "RecvEvent{" +
                "eventType='" + eventType + '\'' +
                "} " + super.toString();
    }
}
