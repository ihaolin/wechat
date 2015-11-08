package org.haolin.wechat.model.message;

/**
 * 发送消息的范围
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 8/11/15
 */
public enum SendMessageScope {

    /**
     * 分组群发:【订阅号与服务号认证后均可用】
     */
    GROUP(1, "分组群发"),

    /**
     * 按OpenId列表发: 订阅号不可用，服务号认证后可用
     */
    OPEN_ID(2, "按OpenId列表发");

    private Integer value;

    private String desc;

    private SendMessageScope(Integer value, String desc){
        this.value = value;
        this.desc = desc;
    }

}
