package me.hao0.wechat.model.data.msg;

import com.google.common.base.Objects;

/**
 * 分析消息类型
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 20/11/15
 */
public enum MsgType {

    /**
     * 未知
     */
    UNKNOWN(0),

    /**
     * 文字
     */
    TEXT(1),

    /**
     * 图片
     */
    IMAGE(2),

    /**
     * 语音
     */
    VOICE(3),

    /**
     * 视频
     */
    VIDEO(4),

    /**
     * 第三方应用消息（链接消息）
     */
    THIRD(6);

    private Integer value;

    private MsgType(Integer scope){
        this.value = scope;
    }

    public Integer value(){
        return value;
    }

    public static MsgType from(Integer s){
        for (MsgType source : MsgType.values()){
            if (Objects.equal(source.value(), s)){
                return source;
            }
        }
        return UNKNOWN;
    }
}
