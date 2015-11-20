package me.hao0.wechat.model.data.interfaces;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 * 接口分析数据
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 20/11/15
 */
public class InterfaceSummary implements Serializable {

    private static final long serialVersionUID = 6383545997391007323L;

    @JsonProperty("ref_date")
    private String date;

    /**
     * 通过服务器配置地址获得消息后，被动回复用户消息的次数
     */
    @JsonProperty("callback_count")
    private Integer callbackCount;

    /**
     * 上述动作的失败次数
     */
    @JsonProperty("fail_count")
    private Integer failCount;

    /**
     * 总耗时，除以callbackCount即为平均耗时
     */
    @JsonProperty("total_time_cost")
    private Integer totalTime;

    /**
     * 最大耗时
     */
    @JsonProperty("max_time_cost")
    private Integer maxTime;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getCallbackCount() {
        return callbackCount;
    }

    public void setCallbackCount(Integer callbackCount) {
        this.callbackCount = callbackCount;
    }

    public Integer getFailCount() {
        return failCount;
    }

    public void setFailCount(Integer failCount) {
        this.failCount = failCount;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public Integer getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Integer maxTime) {
        this.maxTime = maxTime;
    }

    @Override
    public String toString() {
        return "InterfaceSummary{" +
                "date='" + date + '\'' +
                ", callbackCount=" + callbackCount +
                ", failCount=" + failCount +
                ", totalTime=" + totalTime +
                ", maxTime=" + maxTime +
                '}';
    }
}
