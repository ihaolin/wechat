package me.hao0.wechat.exception;

import java.util.Map;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 5/11/15
 */
public class WechatException extends RuntimeException {

    public WechatException(Map<String, ?> errMap) {
        super("[" + errMap.get("errcode") + "]" + errMap.get("errmsg"));
    }
}
