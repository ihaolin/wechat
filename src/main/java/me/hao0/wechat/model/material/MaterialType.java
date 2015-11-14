package me.hao0.wechat.model.material;

/**
 * 素材类型
 */
public enum MaterialType {

    IMAGE("image"),

    VIDEO("video"),

    VOICE("voice"),

    NEWS("news");

    private String value;

    private MaterialType(String value){
        this.value = value;
    }

    public String value(){
        return value;
    }
}