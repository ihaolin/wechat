package me.hao0.wechat.model.qrcode;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 9/11/15
 */
public class Qrcode implements Serializable {

    private static final long serialVersionUID = -8864173402832984445L;

    /**
     * 获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码。
     */
    private String ticket;

    /**
     * 有效时间(秒)
     */
    @JsonProperty("expire_seconds")
    private String expireSeconds;

    /**
     * 二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
     */
    private String url;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(String expireSeconds) {
        this.expireSeconds = expireSeconds;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Qrcode{" +
                "ticket='" + ticket + '\'' +
                ", expireSeconds='" + expireSeconds + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
