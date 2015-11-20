package me.hao0.wechat.model.data.msg;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 20/11/15
 */
public class MsgSendSummaryHour extends MsgSendSummary {

    private static final long serialVersionUID = -1978665674344648838L;

    @JsonProperty("ref_hour")
    private Integer hour;

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    @Override
    public String toString() {
        return "MsgSendSummaryHour{" +
                "hour=" + hour +
                "} " + super.toString();
    }
}
