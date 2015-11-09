package me.hao0.wechat.model.message;

import java.io.Serializable;
import java.util.List;

/**
 * 发送消息
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 8/11/15
 */
public class SendMessage implements Serializable {

    private static final long serialVersionUID = 8803986913007718602L;

    /**
     * 消息类型:
     *  @see SendMessageType
     */
    private SendMessageType type;

    /**
     * 发送范围
     *  @see SendMessageScope
     */
    private SendMessageScope scope;

    /**
     * 分组群发消息时
     * 分组ID
     */
    private Integer groupId;

    /**
     * 分组群发消息时
     * 用于设定是否向全部用户发送，值为true或false，选择true该消息群发给所有用户，选择false可根据group_id发送给指定群组的用户
     */
    private Boolean isToAll = Boolean.FALSE;

    /**
     * OpenID列表群发
     * 用户openId列表，与groupId和isToAll排斥
     */
    private List<String> openIds;

    /**
     * 文本消息内容，当type为文本时
     */
    private String content;

    /**
     * 用于群发的消息的media_id，当type为语音，图文，视频，图片时
     */
    private String mediaId;

    /**
     * 卡券ID，当type为卡券时
     */
    private String cardId;

    /**
     * 视频缩略图的媒体ID
     */
    private String thumbMediaId;

    /**
     * 消息的标题
     */
    private String title;

    /**
     * 消息的描述
     */
    private String description;

    public SendMessageType getType() {
        return type;
    }

    public void setType(SendMessageType type) {
        this.type = type;
    }

    public SendMessageScope getScope() {
        return scope;
    }

    public void setScope(SendMessageScope scope) {
        this.scope = scope;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Boolean getIsToAll() {
        return isToAll;
    }

    public void setIsToAll(Boolean isToAll) {
        this.isToAll = isToAll;
    }

    public List<String> getOpenIds() {
        return openIds;
    }

    public void setOpenIds(List<String> openIds) {
        this.openIds = openIds;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "SendMessage{" +
                "type=" + type +
                ", groupId=" + groupId +
                ", isToAll=" + isToAll +
                ", content='" + content + '\'' +
                ", mediaId='" + mediaId + '\'' +
                ", cardId='" + cardId + '\'' +
                ", thumbMediaId='" + thumbMediaId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
