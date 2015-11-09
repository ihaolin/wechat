package me.hao0.wechat.model.message.resp;

import java.io.Serializable;

/**
 * 图文消息对象
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 8/11/15
 */
public class Article implements Serializable {

    private static final long serialVersionUID = -5038606046133238683L;

    private String title;

    private String desc;

    /**
     * 图片链接，支持JPG、PNG格式，较好的效果为大图360*200，小图200*200
     */
    private String picUrl;

    /**
     * 点击图文消息跳转链接
     */
    private String url;

    public Article(){}

    public Article(String title, String desc, String picUrl, String url) {
        this.title = title;
        this.desc = desc;
        this.picUrl = picUrl;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ArticleItem{" +
                "title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", picUrl='" + picUrl + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
