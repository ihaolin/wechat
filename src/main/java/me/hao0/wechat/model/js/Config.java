package me.hao0.wechat.model.js;

import java.io.Serializable;

/**
 * 调用JSSDK前需要加载的配置对象(http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html)
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 15/11/15
 */
public class Config implements Serializable {

    private static final long serialVersionUID = -8263857663686622616L;

    /**
     * 微信APP ID
     */
    private String appId;

    /**
     * 时间戳(秒)
     */
    private Long timestamp;

    /**
     * 随机字符串
     */
    private String nonStr;

    /**
     * 签名
     */
    private String signature;

    public Config(String appId, Long timestamp, String nonStr, String signature) {
        this.appId = appId;
        this.timestamp = timestamp;
        this.nonStr = nonStr;
        this.signature = signature;
    }

    public Config() {
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonStr() {
        return nonStr;
    }

    public void setNonStr(String nonStr) {
        this.nonStr = nonStr;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "Config{" +
                "appId='" + appId + '\'' +
                ", timestamp=" + timestamp +
                ", nonStr='" + nonStr + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }
}
