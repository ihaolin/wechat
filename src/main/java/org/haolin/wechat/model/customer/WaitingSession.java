package org.haolin.wechat.model.customer;

import java.io.Serializable;
import java.util.Date;

/**
 * 未接入会话
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 7/11/15
 */
public class WaitingSession implements Serializable {

    private static final long serialVersionUID = 2699187929516108564L;

    /**
     * 用户openId
     */
    private String openId;

    /**
     * 客服帐号(包含域名)
     */
    private String kfAccount;

    /**
     * 会话结束时间
     */
    private Date createTime;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getKfAccount() {
        return kfAccount;
    }

    public void setKfAccount(String kfAccount) {
        this.kfAccount = kfAccount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "WaitingSession{" +
                "openId='" + openId + '\'' +
                ", kfAccount='" + kfAccount + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
