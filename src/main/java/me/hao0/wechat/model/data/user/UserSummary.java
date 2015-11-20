package me.hao0.wechat.model.data.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import me.hao0.wechat.serializer.UserSourceDeserializer;
import java.io.Serializable;

/**
 * 用户增量数据
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 20/11/15
 */
public class UserSummary implements Serializable {

    private static final long serialVersionUID = -6612438038581745613L;

    /**
     * 日期: yyyy-MM-dd
     */
    @JsonProperty("ref_date")
    private String date;

    /**
     * 用户的渠道
     *  @see UserSource
     */
    @JsonProperty("user_source")
    @JsonDeserialize(using = UserSourceDeserializer.class)
    private UserSource source;

    /**
     * 新增的用户数量
     */
    @JsonProperty("new_user")
    private Integer newCount;

    /**
     * 取消关注的用户数量，new_user减去cancel_user即为净增用户数量
     */
    @JsonProperty("cancel_user")
    private Integer cancelCount;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public UserSource getSource() {
        return source;
    }

    public void setSource(UserSource source) {
        this.source = source;
    }

    public Integer getNewCount() {
        return newCount;
    }

    public void setNewCount(Integer newCount) {
        this.newCount = newCount;
    }

    public Integer getCancelCount() {
        return cancelCount;
    }

    public void setCancelCount(Integer cancelCount) {
        this.cancelCount = cancelCount;
    }

    /**
     * 获取用户增量(负数表示取消关注的人多于新增的人)
     *  newUser - cancelUser
     * @return 用户增量
     */
    public Integer getIncrement(){
        return newCount - cancelCount;
    }

    @Override
    public String toString() {
        return "UserSummary{" +
                "date='" + date + '\'' +
                ", source=" + source +
                ", newCount=" + newCount +
                ", cancelCount=" + cancelCount +
                '}';
    }
}
