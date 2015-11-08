package org.haolin.wechat.model.customer;

import java.io.Serializable;
import java.util.Date;

/**
 * 客服的会话状态
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 7/11/15
 */
public class CsSession implements Serializable {

    private static final long serialVersionUID = -8380650779042961587L;

    /**
     * 用户openId
     */
    private String openId;

    /**
     * 会话接入时间
     */
    private Date createTime;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "CsSessionStatus{" +
                "openId='" + openId + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
