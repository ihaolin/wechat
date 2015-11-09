package me.hao0.wechat.model.qrcode;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 9/11/15
 */
public enum QrcodeType {

    /**
     * 临时二维码，有过期时间，最长7天，604800s
     */
    QR_SCENE("QR_SCENE"),

    /**
     * 永久二维码，最多100000个
     */
    QR_LIMIT_SCENE("QR_LIMIT_SCENE");

    private String value;

    private QrcodeType(String value){
        this.value = value;
    }

    public String value(){
        return value;
    }
}
