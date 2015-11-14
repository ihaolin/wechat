package me.hao0.wechat.model.material;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import me.hao0.wechat.utils.DateDeserializer;
import java.io.Serializable;
import java.util.Date;

/**
 * 素材基类
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 12/11/15
 */
public abstract class Material implements Serializable {

    @JsonProperty("media_id")
    protected String mediaId;

    @JsonProperty("update_time")
    @JsonDeserialize(using = DateDeserializer.class)
    protected Date updateTime;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Material{" +
                "mediaId='" + mediaId + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
