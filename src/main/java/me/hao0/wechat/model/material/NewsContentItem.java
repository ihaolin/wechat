package me.hao0.wechat.model.material;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 图文素材类
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 12/11/15
 */
public class NewsContentItem implements Serializable {

    private static final long serialVersionUID = 8483540691949616866L;

    /**
     * 图文消息的标题
     */
    private String title;

    /**
     * 图文消息的封面图片素材id（必须是永久mediaID）
     */
    @JsonProperty("thumb_media_id")
    private String thumbMediaId;

    /**
     * 是否显示封面，0为false，即不显示，1为true，即显示
     */
    @JsonProperty("show_cover_pic")
    private Integer showCoverPic;

    /**
     * 作者
     */
    private String author;

    /**
     * 图文消息的摘要，仅有单图文消息才有摘要，多图文此处为空
     */
    private String digest;

    /**
     * 内容
     */
    private String content;

    /**
     * URL
     */
    private String url;

    /**
     * 图文消息的原文地址，即点击“阅读原文”后的URL
     */
    @JsonProperty("content_source_url")
    private String contentSourceUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }

    public Integer getShowCoverPic() {
        return showCoverPic;
    }

    public void setShowCoverPic(Integer showCoverPic) {
        this.showCoverPic = showCoverPic;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContentSourceUrl() {
        return contentSourceUrl;
    }

    public void setContentSourceUrl(String contentSourceUrl) {
        this.contentSourceUrl = contentSourceUrl;
    }

    @Override
    public String toString() {
        return "NewsContentItem{" +
                "title='" + title + '\'' +
                ", thumbMediaId='" + thumbMediaId + '\'' +
                ", showCoverPic=" + showCoverPic +
                ", author='" + author + '\'' +
                ", digest='" + digest + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                ", contentSourceUrl='" + contentSourceUrl + '\'' +
                '}';
    }
}
