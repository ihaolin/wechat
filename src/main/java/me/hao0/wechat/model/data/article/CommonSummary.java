package me.hao0.wechat.model.data.article;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 * 基本统计
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 20/11/15
 */
public abstract class CommonSummary implements Serializable {

    /**
     * 图文页（点击群发图文卡片进入的页面）的阅读人数
     */
    @JsonProperty("int_page_read_user")
    private Integer readUser;

    /**
     * 图文页的阅读次数
     */
    @JsonProperty("int_page_read_count")
    private Integer readCount;

    /**
     * 原文页（点击图文页“阅读原文”进入的页面）的阅读人数，无原文页时此处数据为0
     */
    @JsonProperty("ori_page_read_user")
    private Integer originReadUser;

    /**
     * 原文页的阅读次数
     */
    @JsonProperty("ori_page_read_count")
    private Integer originReadCount;

    /**
     * 分享的人数
     */
    @JsonProperty("share_user")
    private Integer shareUser;

    /**
     * 分享的次数
     */
    @JsonProperty("share_count")
    private Integer shareCount;

    /**
     * 收藏人数
     */
    @JsonProperty("add_to_fav_user")
    private Integer favUser;

    /**
     * 收藏次数
     */
    @JsonProperty("add_to_fav_count")
    private Integer favCount;

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public Integer getOriginReadUser() {
        return originReadUser;
    }

    public void setOriginReadUser(Integer originReadUser) {
        this.originReadUser = originReadUser;
    }

    public Integer getOriginReadCount() {
        return originReadCount;
    }

    public void setOriginReadCount(Integer originReadCount) {
        this.originReadCount = originReadCount;
    }

    public Integer getShareUser() {
        return shareUser;
    }

    public void setShareUser(Integer shareUser) {
        this.shareUser = shareUser;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    public Integer getFavUser() {
        return favUser;
    }

    public void setFavUser(Integer favUser) {
        this.favUser = favUser;
    }

    public Integer getFavCount() {
        return favCount;
    }

    public void setFavCount(Integer favCount) {
        this.favCount = favCount;
    }

    @Override
    public String toString() {
        return "CommonSummary{" +
                "readUser=" + readUser +
                ", readCount=" + readCount +
                ", originReadUser=" + originReadUser +
                ", originReadCount=" + originReadCount +
                ", shareUser=" + shareUser +
                ", shareCount=" + shareCount +
                ", favUser=" + favUser +
                ", favCount=" + favCount +
                '}';
    }
}
