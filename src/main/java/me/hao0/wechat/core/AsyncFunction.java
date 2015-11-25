package me.hao0.wechat.core;

/**
 * 异步执行函数
 * @param <T> 范型结果
 * @since 1.4.0
 */
abstract class AsyncFunction<T> {

    Callback<T> cb;

    AsyncFunction(Callback<T> cb){
        this.cb = cb;
    }

    public abstract T execute() throws Exception;
}