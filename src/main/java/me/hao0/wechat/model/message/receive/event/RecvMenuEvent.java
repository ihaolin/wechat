package me.hao0.wechat.model.message.receive.event;

/**
 * 菜单事件
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 9/11/15
 */
public class RecvMenuEvent extends RecvEvent {

    /**
     * eventType为CLICK时: 与自定义菜单接口中KEY值对应
     * eventType为VIEW时: 设置的跳转URL
     */
    private String eventKey;

    public RecvMenuEvent(RecvEvent e){
        super(e);
        this.eventType = e.eventType;
    }

    @Override
    public String getEventType() {
        if ("CLICK".equalsIgnoreCase(eventType)){
            return RecvEventType.MENU_CLICK.value();
        }
        return RecvEventType.MENU_VIEW.value();
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    @Override
    public String toString() {
        return "RecvMenuEvent{" +
                ", eventKey='" + eventKey + '\'' +
                "} " + super.toString();
    }
}
