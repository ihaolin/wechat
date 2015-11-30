package me.hao0.wechat.core;

import me.hao0.wechat.loader.AccessTokenLoader;
import me.hao0.wechat.loader.TicketLoader;
import java.util.concurrent.ExecutorService;
import static me.hao0.common.util.Preconditions.*;

/**
 * 微信组件库配置构建器
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 14/11/15
 * @since 1.4.0
 */
public final class WechatBuilder {

    private Wechat wechat;

    /**
     * 创建一个WechatBuilder
     * @param appId 微信appId
     * @param appSecret 微信appSecret
     * @return a builder
     */
    public static WechatBuilder newBuilder(String appId, String appSecret){
        checkNotNullAndEmpty(appId, "appId");
        checkNotNullAndEmpty(appSecret, "appSecret");

        WechatBuilder builder = new WechatBuilder();
        builder.wechat = new Wechat(appId, appSecret);
        return builder;
    }

    /**
     * 配置微信APP令牌(Token)
     * @param token 微信APP令牌(Token)
     * @return this
     */
    public WechatBuilder token(String token){
        checkNotNullAndEmpty(token, "token");
        wechat.appToken = token;
        return this;
    }

    /**
     * 配置加密消息的Key
     * @param msgKey 加密消息的Key
     * @return this
     */
    public WechatBuilder msgKey(String msgKey){
        checkNotNullAndEmpty(msgKey, "msgKey");
        wechat.msgKey = msgKey;
        return this;
    }

    /**
     * 配置accessToken加载器
     * @param accessTokenLoader accessToken加载器
     * @return return this
     */
    public WechatBuilder accessTokenLoader(AccessTokenLoader accessTokenLoader){
        checkNotNull(accessTokenLoader, "accessTokenLoader can't be null");
        wechat.tokenLoader = accessTokenLoader;
        return this;
    }

    /**
     * 配置ticket加载器
     * @param ticketLoader ticket加载器
     * @return this
     */
    public WechatBuilder ticketLoader(TicketLoader ticketLoader){
        checkNotNull(ticketLoader, "ticketLoader can't be null");
        wechat.ticketLoader = ticketLoader;
        return this;
    }

    /**
     * 设置ExecutorService，用于异步调用
     * @param executor 异步执行器
     * @return this
     */
    public WechatBuilder executor(ExecutorService executor){
        checkNotNull(executor, "executor can't be null");
        wechat.executor = executor;
        return this;
    }

    /**
     * 返回最终配置好的Wechat对象
     * @return Wechat对象
     */
    public Wechat build(){
        return wechat;
    }
}
