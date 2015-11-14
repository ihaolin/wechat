package me.hao0.wechat.model.material;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 素材上传类型
 */
public enum MaterialUploadType {

    IMAGE("image"),

    VIDEO("video"),

    VOICE("voice"),

    THUMB("thumb");

    private String value;

    private MaterialUploadType(String value){
        this.value = value;
    }

    @JsonValue
    public String value(){
        return value;
    }
}