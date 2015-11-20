package me.hao0.wechat.model.material;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * 上传永久素材后的返回对象
 * Author: haolin
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 13/11/15
 */
public class PermMaterial implements Serializable {

    private static final long serialVersionUID = 4907386605074554874L;

    @JsonProperty("media_id")
    private String mediaId;

    private String url;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "PermMaterial{" +
                "mediaId='" + mediaId + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
