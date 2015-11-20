package me.hao0.wechat.model.data.article;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import me.hao0.wechat.serializer.ArticleShareSceneDeserializer;
import java.io.Serializable;

/**
 * 图文分享转发数据
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 20/11/15
 */
public class ArticleShare implements Serializable {

    private static final long serialVersionUID = -2590968439236572137L;

    /**
     * 日期
     */
    @JsonProperty("ref_date")
    private String date;

    /**
     * 分享场景
     */
    @JsonProperty("share_scene")
    @JsonDeserialize(using = ArticleShareSceneDeserializer.class)
    private ArticleShareScene scene;

    /**
     * 分享次数
     */
    @JsonProperty("share_count")
    private Integer count;

    /**
     * 分享人数
     */
    @JsonProperty("share_user")
    private Integer user;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArticleShareScene getScene() {
        return scene;
    }

    public void setScene(ArticleShareScene scene) {
        this.scene = scene;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "ArticleShare{" +
                "date='" + date + '\'' +
                ", scene=" + scene +
                ", count=" + count +
                ", user=" + user +
                '}';
    }
}

