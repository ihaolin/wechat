package me.hao0.wechat.model.js;

/**
 * 临时凭证类型
 */
public enum TicketType {

    /**
     * 用于调用微信JSSDK的临时票据
     */
    JSAPI("jsapi"),

    /**
     * 用于调用卡券相关接口的临时票据
     */
    CARD("wx_card");

    private String type;

    private TicketType(String type){
        this.type = type;
    }

    public String type(){
        return type;
    }
}