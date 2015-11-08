package org.haolin.wechat.model.message;

import java.io.Serializable;

/**
 * 模版消息字段
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 8/11/15
 */
public class TemplateField implements Serializable{

    private static final long serialVersionUID = 8228081012066368310L;

    /**
     * 名称
     */
    private String name;

    /**
     * 值
     */
    private String value;

    /**
     * 颜色
     */
    private String color;

    public TemplateField(){}


    public TemplateField(String name, String value, String color) {
        this.name = name;
        this.value = value;
        this.color = color;
    }

    public TemplateField(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


}
