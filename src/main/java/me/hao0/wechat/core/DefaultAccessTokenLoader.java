package me.hao0.wechat.core;

import me.hao0.wechat.model.base.AccessToken;
import me.hao0.wechat.utils.Strings;

/**
 * 一个内存式AccessToken加载器
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 10/11/15
 */
public class DefaultAccessTokenLoader implements AccessTokenLoader {

    private volatile AccessToken validToken;

    @Override
    public String get() {
        return (validToken == null
                || Strings.isNullOrEmpty(validToken.getAccessToken())
                || System.currentTimeMillis() > validToken.getExpiredAt()) ? null : validToken.getAccessToken();
    }

    @Override
    public void refresh(AccessToken token) {
        validToken = token;
    }
}
