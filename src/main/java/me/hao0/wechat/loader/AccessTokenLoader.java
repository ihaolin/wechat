package me.hao0.wechat.loader;

import me.hao0.wechat.model.base.AccessToken;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 10/11/15
 */
public interface AccessTokenLoader {

    /**
     * 获取accessToken
     * @return accessToken，""或NULL会重新从微信服务器获取，并进行refresh
     */
    String get();

    /**
     * 刷新accessToken，实现时需要保存一段时间，以免频繁从微信服务器获取
     * @param token 从微信服务器获取AccessToken
     */
    void refresh(AccessToken token);
}
