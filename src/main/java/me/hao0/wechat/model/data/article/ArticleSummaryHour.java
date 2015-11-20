package me.hao0.wechat.model.data.article;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import me.hao0.wechat.serializer.ArticleSourceDeserializer;

/**
 * 图文统计分时数据
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 20/11/15
 */
public class ArticleSummaryHour extends CommonSummary {

    private static final long serialVersionUID = -5668724641461918373L;

    /**
     * 用户渠道
     */
    @JsonProperty("user_source")
    @JsonDeserialize(using = ArticleSourceDeserializer.class)
    private ArticleSource source;

    /**
     * 小时: 包括从000到2300，分别代表的是[000,100)到[2300,2400)，即每日的第1小时和最后1小时
     */
    @JsonProperty("ref_hour")
    private Integer hour;

    public ArticleSource getSource() {
        return source;
    }

    public void setSource(ArticleSource source) {
        this.source = source;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    @Override
    public String toString() {
        return "ArticleSummaryHour{" +
                "source=" + source +
                ", hour=" + hour +
                "} " + super.toString();
    }
}
