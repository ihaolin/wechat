package me.hao0.wechat.model.message.receive.event;

import java.util.Objects;

/**
 * 接收微信服务器的消息类型
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 8/11/15
 */
public enum RecvEventType {

    UNKNOW("unknown", "未知事件"),

    SUBSCRIBE("subscribe", "关注公众号"),
    UN_SUBSCRIBE("unsubscribe", "取消关注公众号"),
    /**
     *  1. 用户未关注时，进行关注后的事件推送:
     */
    SCAN("SCAN", "扫码"),
    LOCATION("LOCATION", "上报地理位置信息"),
    /**
     * 点击菜单拉取消息时的事件推送
     */
    MENU_CLICK("CLICK", "点击菜单拉取消息时"),
    /**
     * 点击菜单跳转链接时的事件推送
     */
    MENU_VIEW("VIEW", "点击菜单跳转链接时"),

    /**
     * 模版消息发送结果通知事件
     */
    TEMPLATE_SEND_JOB_FINISH("TEMPLATESENDJOBFINISH", "模版消息发送任务完成后, 微信服务器会将是否送达成功作为通知");

    private String value;

    private String desc;

    RecvEventType(String value, String desc){
        this.value = value;
        this.desc = desc;
    }

    public String value(){
        return value;
    }

    public static RecvEventType from(String type){

        for (RecvEventType t : RecvEventType.values()){
            if (Objects.equals(t.value(), type)){
                return t;
            }
        }

        return UNKNOW;
    }

    @Override
    public String toString() {
        return "RecvEventType{" +
                "value='" + value + '\'' +
                ", desc='" + desc + '\'' +
                "} " + super.toString();
    }
}
