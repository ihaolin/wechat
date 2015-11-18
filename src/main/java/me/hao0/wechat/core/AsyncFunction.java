package me.hao0.wechat.core;

abstract class AsyncFunction<T> {

    Callback<T> cb;

    AsyncFunction(Callback<T> cb){
        this.cb = cb;
    }

    public abstract T execute() throws Exception;
}