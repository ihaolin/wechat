package me.hao0.wechat.model.base;

import java.io.Serializable;

/**
 * 访问Token
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 10/11/15
 * @since 1.0.0
 */
public class AccessToken implements Serializable {

    private static final long serialVersionUID = 6038499458891708844L;

    /**
     * accessToken
     */
    private String accessToken;

    /**
     * 有效时间(s)
     */
    private Integer expire;

    /**
     * 过期时刻(ms)
     */
    private Long expiredAt;

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

    public Long getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Long expiredAt) {
        this.expiredAt = expiredAt;
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "accessToken='" + accessToken + '\'' +
                ", expire=" + expire +
                ", expiredAt=" + expiredAt +
                '}';
    }
}
