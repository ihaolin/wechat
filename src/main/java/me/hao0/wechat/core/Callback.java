package me.hao0.wechat.core;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 17/11/15
 */
public interface Callback<T> {

    /**
     * 成功时回调
     * @param t 结果类型
     */
    void onSuccess(T t);

    /**
     * 失败时回调
     * @param e Exception
     */
    void onFailure(Exception e);
}
