package me.hao0.wechat.model.message.receive.event;

import me.hao0.wechat.exception.EventException;

import java.util.Objects;

/**
 * 接收微信服务器的消息类型
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 8/11/15
 */
public enum RecvEventType {

    SUBSCRIBE("subscribe", "关注公众号"),
    UN_SUBSCRIBE("unsubscribe", "取消关注公众号"),
    /**
     *  1. 用户未关注时，进行关注后的事件推送:
        <xml><ToUserName><![CDATA[toUser]]></ToUserName>
             <FromUserName><![CDATA[FromUser]]></FromUserName>
             <CreateTime>123456789</CreateTime>
             <MsgType><![CDATA[event]]></MsgType>
             <Event><![CDATA[subscribe]]></Event>
             <EventKey><![CDATA[qrscene_123123]]></EventKey>
             <Ticket><![CDATA[TICKET]]></Ticket>
        </xml>
        2. 用户已关注时的事件推送:
         <xml>
             <ToUserName><![CDATA[toUser]]></ToUserName>
             <FromUserName><![CDATA[FromUser]]></FromUserName>
             <CreateTime>123456789</CreateTime>
             <MsgType><![CDATA[event]]></MsgType>
             <Event><![CDATA[SCAN]]></Event>
             <EventKey><![CDATA[SCENE_VALUE]]></EventKey>
             <Ticket><![CDATA[TICKET]]></Ticket>
         </xml>
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
    MENU_VIEW("VIEW", "点击菜单跳转链接时");

    private String value;

    private String desc;

    private RecvEventType(String value, String desc){
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
        throw new EventException("unknown event type");
    }

    @Override
    public String toString() {
        return "RecvEventType{" +
                "value='" + value + '\'' +
                ", desc='" + desc + '\'' +
                "} " + super.toString();
    }
}
