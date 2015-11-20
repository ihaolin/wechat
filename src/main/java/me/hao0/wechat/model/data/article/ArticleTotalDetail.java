package me.hao0.wechat.model.data.article;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 20/11/15
 */
public class ArticleTotalDetail extends CommonSummary {

    private static final long serialVersionUID = 3233729188265417152L;

    /**
     * 统计的日期，ref_date指的是文章群发出日期， 而stat_date是数据统计日期
     */
    @JsonProperty("stat_date")
    private String statDate;

    /**
     * 送达人数，一般约等于总粉丝数（需排除黑名单或其他异常情况下无法收到消息的粉丝）
     */
    @JsonProperty("target_user")
    private Integer targetUser;

    public String getStatDate() {
        return statDate;
    }

    public void setStatDate(String statDate) {
        this.statDate = statDate;
    }

    public Integer getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(Integer targetUser) {
        this.targetUser = targetUser;
    }

    @Override
    public String toString() {
        return "ArticleTotalDetail{" +
                "statDate='" + statDate + '\'' +
                ", targetUser=" + targetUser +
                "} " + super.toString();
    }
}
