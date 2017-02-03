package me.hao0.wechat.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Arrays;

/**
 * 用户信息(未关注，但已手动授权)
 * Author: haolin
 * Email: haolin.h0@gmail.com
 */
public class UserInfo implements Serializable {

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

    @JsonProperty("unionid")
    private String unionId;

    /**
     * 用户特权信息，json数组，如微信沃卡用户为（chinaunicom）
     */
    private String[] privilege;

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

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String[] getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String[] privilege) {
        this.privilege = privilege;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "openId='" + openId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", sex=" + sex +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", country='" + country + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", unionId='" + unionId + '\'' +
                ", privilege=" + Arrays.toString(privilege) +
                '}';
    }
}
