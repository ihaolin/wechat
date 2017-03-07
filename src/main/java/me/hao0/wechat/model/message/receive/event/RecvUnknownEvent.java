package me.hao0.wechat.model.message.receive.event;

/**
 * 未知事件(当解析到微信的新事件时)
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * @since 1.9.2
 */
public class RecvUnknownEvent extends RecvEvent {

    public RecvUnknownEvent(RecvEvent e){
        super(e);
        this.eventType = e.eventType;
    }

    @Override
    public String getEventType() {
        return RecvEventType.UNKNOW.value();
    }
}
