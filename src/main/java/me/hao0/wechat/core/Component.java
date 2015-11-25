package me.hao0.wechat.core;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

/**
 * 微信组件需要继承该类
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 18/11/15
 * @since 1.4.0
 */
public abstract class Component {

    protected Wechat wechat;

    /**
     * 加载accessToken
     * @return accessToken
     */
    protected String loadAccessToken(){
        return wechat.loadAccessToken();
    }

    /**
     * POST请求
     * @param url URL
     * @param params 请求参数
     * @return Map结果集
     */
    protected Map<String, Object> doPost(String url, Map<String, Object> params){
        return wechat.doPost(url, params);
    }

    /**
     * POST请求
     * @param url URL
     * @param body 请求body
     * @return Map结果集
     */
    protected Map<String, Object> doPost(String url, String body){
        return wechat.doPost(url, body);
    }

    /**
     * GET请求
     * @param url URL
     * @return Map结果集
     */
    protected Map<String, Object> doGet(String url){
        return wechat.doGet(url);
    }

    /**
     * 上传请求
     * @param url URL
     * @param fieldName 上传字段名
     * @param fileName 上传文件名
     * @param input 输入流
     * @return Map结果集
     */
    protected Map<String, Object> doUpload(String url, String fieldName, String fileName, InputStream input){
        return wechat.doUpload(url, fieldName, fileName, input, Collections.<String, String>emptyMap());
    }

    /**
     * 上传请求
     * @param url URL
     * @param fieldName 上传字段名
     * @param fileName 上传文件名
     * @param input 输入流
     * @param params 其他参数
     * @return Map结果集
     */
    protected Map<String, Object> doUpload(String url, String fieldName, String fileName, InputStream input, Map<String, String> params){
        return wechat.doUpload(url, fieldName, fileName, input, params);
    }

    /**
     * 异步执行
     * @param af 执行函数
     * @param <T> 范型结果
     */
    protected <T> void doAsync(AsyncFunction<T> af){
        wechat.doAsync(af);
    }
}
