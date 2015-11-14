package me.hao0.wechat.model.base;

/**
 * 授权类型
 */
public enum AuthType{

    BASE("snsapi_base"),

    USER_INFO("snsapi_userinfo");

    private String scope;

    private AuthType(String scope){
        this.scope = scope;
    }

    public String scope(){
        return scope;
    }
}