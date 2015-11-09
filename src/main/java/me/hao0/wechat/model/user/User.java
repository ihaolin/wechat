package me.hao0.wechat.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import me.hao0.wechat.utils.DateDeserializer;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 7/11/15
 */
public class User implements Serializable {

    /**
     * 0未关注，1已关注
     */
    private Integer subscribe;

    @JsonProperty("openid")
    private String openId;

    @JsonProperty("nickname")
    private String nickName;

    /**
     * 0未知，1男，2女
     */
    private Integer sex;

    private String city;

    private String province;

    private String country;

    @JsonProperty("headimgurl")
    private String headImgUrl;

    @JsonProperty("subscribe_time")
    @JsonDeserialize(using = DateDeserializer.class)
    private Date subscribeTime;

    @JsonProperty("unionid")
    private Integer unionId;

    private String remark;

    @JsonProperty("groupid")
    private Integer groupId;

    public Integer getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Integer subscribe) {
        this.subscribe = subscribe;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public Date getSubscribeTime() {
        return subscribeTime;
    }

    public void setSubscribeTime(Date subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    public Integer getUnionId() {
        return unionId;
    }

    public void setUnionId(Integer unionId) {
        this.unionId = unionId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        return "User{" +
                "subscribe=" + subscribe +
                ", openId='" + openId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", sex=" + sex +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", country='" + country + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", subscribeTime=" + subscribeTime +
                ", unionId=" + unionId +
                ", remark='" + remark + '\'' +
                ", groupId=" + groupId +
                '}';
    }
}
