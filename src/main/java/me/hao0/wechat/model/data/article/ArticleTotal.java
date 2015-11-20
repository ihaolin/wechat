package me.hao0.wechat.model.data.article;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

/**
 * 图文群发总数据
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 20/11/15
 */
public class ArticleTotal implements Serializable {

    private static final long serialVersionUID = -4800022767823366024L;

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

    /**
     * 每天对应的数值为该文章到该日为止的总量（而不是当日的量）
     */
    private List<ArticleTotalDetail> details;

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

    public List<ArticleTotalDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ArticleTotalDetail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "ArticleTotal{" +
                "date='" + date + '\'' +
                ", msgId='" + msgId + '\'' +
                ", title='" + title + '\'' +
                ", details=" + details +
                '}';
    }
}
