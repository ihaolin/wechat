package me.hao0.wechat.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 用户列表信息
 * Created by y27chen on 2016/3/16.
 */
public class UserList {

    @JsonProperty("total")
    private Integer total;

    @JsonProperty("count")
    private Integer count;

    @JsonProperty("data")
    private UserListRecord data;

    @JsonProperty("next_openid")
    private String nextOpenId;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getNextOpenId() {
        return nextOpenId;
    }

    public void setNextOpenId(String nextOpenId) {
        this.nextOpenId = nextOpenId;
    }

    public UserListRecord getData() {
        return data;
    }

    public void setData(UserListRecord data) {
        this.data = data;
    }

    public class UserListRecord {
        @JsonProperty("openid")
        private List<String> openId;

        public List<String> getOpenId() {
            return openId;
        }

        public void setOpenId(List<String> openId) {
            this.openId = openId;
        }
    }

    @Override
    public String toString() {
        return "Users{" +
                "total=" + total +
                ", count=" + count +
                ", data=" + data +
                ", nextOpenId='" + nextOpenId + '\'' +
                '}';
    }
}
