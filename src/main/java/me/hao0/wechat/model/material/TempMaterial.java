package me.hao0.wechat.model.material;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import me.hao0.wechat.serializer.DateDeserializer;
import java.io.Serializable;
import java.util.Date;

/**
 * 上传临时素材后的返回对象
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 13/11/15
 */
public class TempMaterial implements Serializable {

    private static final long serialVersionUID = -824128825701922924L;

    private MaterialUploadType type;

    @JsonProperty("media_id")
    private String mediaId;

    @JsonProperty("created_at")
    @JsonDeserialize(using = DateDeserializer.class)
    private Date createdAt;

    public MaterialUploadType getType() {
        return type;
    }

    public void setType(MaterialUploadType type) {
        this.type = type;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Media{" +
                "type=" + type +
                ", mediaId='" + mediaId + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
