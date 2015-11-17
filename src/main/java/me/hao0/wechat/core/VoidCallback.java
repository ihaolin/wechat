package me.hao0.wechat.core;

/**
 * 无结果回调，仅处理失败
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 17/11/15
 */
public interface VoidCallback {

    /**
     * 失败时回调
     * @param e Exception
     */
    void onFailure(Exception e);
}
