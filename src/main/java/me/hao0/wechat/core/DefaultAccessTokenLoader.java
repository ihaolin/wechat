package me.hao0.wechat.core;

import me.hao0.wechat.model.base.AccessToken;

/**
 * 一个内存式AccessToken加载器
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 10/11/15
 */
public class DefaultAccessTokenLoader implements AccessTokenLoader {

    private Long expiredAt;

    private AccessToken validToken;

    @Override
    public String get() {
        if (expiredAt == null
                || System.currentTimeMillis() > expiredAt
                || validToken == null){
            return null;
        }
        return validToken.getAccessToken();
    }

    @Override
    public void refresh(AccessToken token) {
        validToken = token;
        expiredAt = System.currentTimeMillis() + (token.getExpire() * 1000L);
    }
}
