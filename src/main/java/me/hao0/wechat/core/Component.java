package me.hao0.wechat.core;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 18/11/15
 */
public class Component {

    protected Wechat wechat;

    protected String loadAccessToken(){
        return wechat.loadAccessToken();
    }

    protected Map<String, Object> doPost(String url, Map<String, Object> params){
        return wechat.doPost(url, params);
    }

    protected Map<String, Object> doPost(String url, String body){
        return wechat.doPost(url, body);
    }

    protected Map<String, Object> doGet(String url){
        return wechat.doGet(url);
    }

    protected Map<String, Object> doUpload(String url, String fieldName, String fileName, InputStream input){
        return wechat.doUpload(url, fieldName, fileName, input, Collections.<String, String>emptyMap());
    }

    protected Map<String, Object> doUpload(String url, String fieldName, String fileName, InputStream input, Map<String, String> params){
        return wechat.doUpload(url, fieldName, fileName, input, params);
    }

    public <T> void doAsync(AsyncFunction<T> af){
        wechat.doAsync(af);
    }
}
