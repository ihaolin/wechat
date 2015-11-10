package me.hao0.wechat.model.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 10/11/15
 */
public class AccessToken implements Serializable {

    private static final long serialVersionUID = 6038499458891708844L;

    /**
     * accessToken
     */
    @JsonProperty("access_token")
    private String accessToken;

    /**
     * 有效时间(秒)
     */
    @JsonProperty("expires_in")
    private Integer expire;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "accessToken='" + accessToken + '\'' +
                ", expire=" + expire +
                '}';
    }
}
