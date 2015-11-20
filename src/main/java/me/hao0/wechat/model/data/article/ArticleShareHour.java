package me.hao0.wechat.model.data.article;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 图文分享转发每日分时数据
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 20/11/15
 */
public class ArticleShareHour extends ArticleShare {

    private static final long serialVersionUID = 8015340884882046L;

    /**
     * 小时: 包括从000到2300，分别代表的是[000,100)到[2300,2400)，即每日的第1小时和最后1小时
     */
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
        return "ArticleShareHour{" +
                "hour=" + hour +
                "} " + super.toString();
    }
}
