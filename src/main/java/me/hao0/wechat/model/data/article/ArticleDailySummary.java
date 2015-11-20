package me.hao0.wechat.model.data.article;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 图文群发每日数据
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 20/11/15
 */
public class ArticleDailySummary extends CommonSummary {

    private static final long serialVersionUID = -7195185645683267419L;

    /**
     * 日期: yyyy-MM-dd
     */
    @JsonProperty("ref_date")
    private String date;

    /**
     * 群发消息ID:
        这里的msgid实际上是由msgid
        （图文消息id，这也就是群发接口调用后返回的msg_data_id）和index（消息次序索引）组成，例如12003_3，
        其中12003是msgid，即一次群发的消息的id；
        3为index，假设该次群发的图文消息共5个文章（因为可能为多图文），3表示5个中的第3个
     */
    @JsonProperty("msgid")
    private String msgId;

    /**
     * 图文标题
     */
    private String title;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ArticleSummary{" +
                "date='" + date + '\'' +
                ", msgId='" + msgId + '\'' +
                ", title='" + title + '\'' +
                "} " + super.toString();
    }
}
