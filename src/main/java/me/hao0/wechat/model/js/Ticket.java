package me.hao0.wechat.model.js;

import java.io.Serializable;

/**
 * 临时凭证
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 14/11/15
 * @see TicketType
 */
public class Ticket implements Serializable {

    private static final long serialVersionUID = 978451551258121101L;

    /**
     * 凭证字符串
     */
    private String ticket;

    /**
     * 凭证类型
     */
    private TicketType type;

    /**
     * 有效时间(s)
     */
    private Integer expire;

    /**
     * 过期时刻(ms)
     */
    private Long expireAt;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }

    public Long getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Long expireAt) {
        this.expireAt = expireAt;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticket='" + ticket + '\'' +
                ", type=" + type +
                ", expire=" + expire +
                ", expireAt=" + expireAt +
                '}';
    }
}
