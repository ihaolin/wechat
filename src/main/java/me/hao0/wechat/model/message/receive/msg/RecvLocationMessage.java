package me.hao0.wechat.model.message.receive.msg;

import me.hao0.wechat.model.message.receive.RecvMessageType;

/**
 * 地理位置消息
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 9/11/15
 */
public class RecvLocationMessage extends RecvMsg {

    private static final long serialVersionUID = 2468731105380952027L;

    /**
     * 纬度
     */
    private String locationX;

    /**
     * 经度
     */
    private String locationY;

    /**
     * 缩放大小
     */
    private Integer scale;

    /**
     * 位置信息
     */
    private String label;

    public RecvLocationMessage(RecvMsg m){
        super(m);
        this.msgId = m.msgId;
    }

    @Override
    public String getMsgType() {
        return RecvMessageType.LOCATION.value();
    }

    public String getLocationX() {
        return locationX;
    }

    public void setLocationX(String locationX) {
        this.locationX = locationX;
    }

    public String getLocationY() {
        return locationY;
    }

    public void setLocationY(String locationY) {
        this.locationY = locationY;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "RecvLocationMessage{" +
                "locationX='" + locationX + '\'' +
                ", locationY='" + locationY + '\'' +
                ", scale=" + scale +
                ", label='" + label + '\'' +
                "} " + super.toString();
    }
}
