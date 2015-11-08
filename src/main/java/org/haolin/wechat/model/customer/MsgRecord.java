package org.haolin.wechat.model.customer;

import java.io.Serializable;
import java.util.Date;

/**
 * 客服聊天记录
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 7/11/15
 */
public class MsgRecord implements Serializable {

    private static final long serialVersionUID = -4343547752472563821L;

    /**
     * 客服帐号
     */
    private String worker;

    /**
     * 用户openid
     */
    private String openid;

    /**
     * 客服操作码:
         1000	创建未接入会话
         1001	接入会话
         1002	主动发起会话
         1003	转接会话
         1004	关闭会话
         1005	抢接会话
         2001	公众号收到消息
         2002	客服发送消息
         2003	客服收到消息
     */
    private String opercode;

    /**
     * 操作时间
     */
    private Date time;

    /**
     * 聊天记录
     */
    private String text;

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getOpercode() {
        return opercode;
    }

    public void setOpercode(String opercode) {
        this.opercode = opercode;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "CsMsgRecord{" +
                "worker='" + worker + '\'' +
                ", openid='" + openid + '\'' +
                ", opercode='" + opercode + '\'' +
                ", time=" + time +
                ", text='" + text + '\'' +
                '}';
    }
}
