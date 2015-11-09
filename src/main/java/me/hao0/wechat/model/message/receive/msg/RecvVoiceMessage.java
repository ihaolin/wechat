package me.hao0.wechat.model.message.receive.msg;

import me.hao0.wechat.model.message.receive.RecvMessageType;

/**
 * 语音消息
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 9/11/15
 */
public class RecvVoiceMessage extends RecvMsg {

    private static final long serialVersionUID = 4578361001225765322L;

    /**
     * 语音消息媒体id，可以调用多媒体文件下载接口拉取数据。
     */
    private String mediaId;

    /**
     * 语音格式，如amr，speex等
     */
    private String format;

    /**
     * 语音识别结果，使用UTF8编码
     */
    private String recognition;

    public RecvVoiceMessage(RecvMsg m){
        super(m);
        this.msgId = m.msgId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getRecognition() {
        return recognition;
    }

    public void setRecognition(String recognition) {
        this.recognition = recognition;
    }

    @Override
    public String getMsgType() {
        return RecvMessageType.VOICE.value();
    }

    @Override
    public String toString() {
        return "RecvVoiceMessage{" +
                "mediaId='" + mediaId + '\'' +
                ", format='" + format + '\'' +
                ", recognition='" + recognition + '\'' +
                "} " + super.toString();
    }
}
