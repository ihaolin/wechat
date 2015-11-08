package org.haolin.wechat.model.menu;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 7/11/15
 */
public class Menu implements Serializable {

    private static final long serialVersionUID = 3569890088693211989L;

    /**
     * 名称:
     * 一级最多4个字，二级最多7个字
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    /**
     * 菜单key，当type="click"时
     */
    private String key;

    /**
     * 菜单url，当type="view"时
     */
    private String url;

    /**
     * 最多3个一级，5个二级
     */
    @JsonProperty("sub_button")
    private List<Menu> children = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", key='" + key + '\'' +
                ", url='" + url + '\'' +
                ", children=" + children +
                '}';
    }
}
