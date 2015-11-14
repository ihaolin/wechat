package me.hao0.wechat.model.material;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

/**
 * 素材总数统计
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 12/11/15
 */
public class MaterialCount implements Serializable {

    private static final long serialVersionUID = 8269265985082026069L;

    /**
     * 图片数
     */
    @JsonProperty("image_count")
    private Integer image;

    /**
     * 图文数
     */
    @JsonProperty("news_count")
    private Integer news;

    /**
     * 语音数
     */
    @JsonProperty("voice_count")
    private Integer voice;

    /**
     * 视频数
     */
    @JsonProperty("video_count")
    private Integer video;

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public Integer getNews() {
        return news;
    }

    public void setNews(Integer news) {
        this.news = news;
    }

    public Integer getVoice() {
        return voice;
    }

    public void setVoice(Integer voice) {
        this.voice = voice;
    }

    public Integer getVideo() {
        return video;
    }

    public void setVideo(Integer video) {
        this.video = video;
    }

    @Override
    public String toString() {
        return "Count{" +
                "image=" + image +
                ", news=" + news +
                ", voice=" + voice +
                ", video=" + video +
                '}';
    }
}

