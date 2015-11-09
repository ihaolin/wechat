package me.hao0.wechat.model.message;

/**
 * 响应微信服务器的消息类型
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 8/11/15
 */
public enum RespMessageType {

    TEXT("text", "文本消息"),
    IMAGE("image", "图片消息"),
    VOICE("voice", "语音消息"),
    VIDEO("video", "视频消息"),
    MUSIC("music", "音乐消息"),
    NEWS("news", "图文消息");

    private String value;

    private String desc;

    private RespMessageType(String value, String desc){
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
