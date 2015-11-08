package org.haolin.wechat.model.message;

/**
 * 发送消息的类型
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 8/11/15
 */
public enum SendMessageType {

    TEXT("text", "文本消息"),
    IMAGE("image", "图片消息"),
    VOICE("voice", "语音消息"),
    VIDEO("mpvideo", "视频消息"),
    NEWS("mpnews", "图文消息"),
    CARD("wxcard", "卡券消息");

    private String value;

    private String desc;

    private SendMessageType(String value, String desc){
        this.value = value;
        this.desc = desc;
    }

    public String value(){
        return value;
    }

    public String desc(){
        return desc;
    }
}
