package org.haolin.wechat.model.base;

/**
 * 授权类型
 */
public enum AuthType{

    BASE("snsapi_base"),

    USER_INFO("snsapi_userinfo");

    public String scope;

    private AuthType(String scope){
        this.scope = scope;
    }
}