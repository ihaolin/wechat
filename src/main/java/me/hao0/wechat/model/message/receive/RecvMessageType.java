package me.hao0.wechat.model.message.receive;

import me.hao0.wechat.exception.EventException;

import java.util.Objects;

/**
 * 接收微信服务器的消息类型
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 8/11/15
 */
public enum RecvMessageType {

    TEXT("text", "文本消息"),
    IMAGE("image", "图片消息"),
    VOICE("voice", "语音消息"),
    VIDEO("video", "视频消息"),
    SHORT_VIDEO("shortvideo", "小视频消息"),
    LOCATION("location", "地理位置信息"),
    LINK("link", "链接信息"),
    /**
     * 接收到微信服务器的事件消息:
     *  @see me.hao0.wechat.model.message.receive.event.RecvEventType
     */
    EVENT("event", "事件消息");

    private String value;

    private String desc;

    private RecvMessageType(String value, String desc){
        this.value = value;
        this.desc = desc;
    }

    public String value(){
        return value;
    }

    public String desc(){
        return desc;
    }

    public static RecvMessageType from(String type){
        for (RecvMessageType t : RecvMessageType.values()){
            if (Objects.equals(t.value(), type)){
                return t;
            }
        }
        throw new EventException("unknown message type");
    }

    @Override
    public String toString() {
        return "RecvMessageType{" +
                "value='" + value + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
