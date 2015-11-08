package org.haolin.wechat.model.user;

import java.io.Serializable;

/**
 * 用户分组
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 7/11/15
 */
public class Group implements Serializable {

    private static final long serialVersionUID = 2140215642384090492L;

    /**
     * 微信分配的ID
     */
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 分组内用户量
     */
    private Integer count;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", count=" + count +
                '}';
    }
}
