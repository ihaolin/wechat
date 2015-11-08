package org.haolin.wechat.model.message;

import java.io.Serializable;

/**
 * 发送预览消息
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 8/11/15
 */
public class SendPreviewMessage implements Serializable {

    private static final long serialVersionUID = -5227718099852463553L;

    /**
     * 消息类型:
     *  @see org.haolin.wechat.model.message.SendMessageType
     */
    private SendMessageType type;

    /**
     * 用户openId
     */
    private String openId;

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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public SendMessageType getType() {
        return type;
    }

    public void setType(SendMessageType type) {
        this.type = type;
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

    @Override
    public String toString() {
        return "SendPreviewMessage{" +
                "type=" + type +
                ", openId='" + openId + '\'' +
                ", content='" + content + '\'' +
                ", mediaId='" + mediaId + '\'' +
                ", cardId='" + cardId + '\'' +
                '}';
    }
}
