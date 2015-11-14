package me.hao0.wechat.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页对象
 * @param <T> 范型类型
 */
public class Page<T> implements Serializable {

    private static final long serialVersionUID = -7919416842196491256L;

    private Integer total;

    private List<T> data;

    public Page(Integer total, List<T> data){
        this.total = total;
        this.data = data;
    }

    public static <T> Page<T> empty() {
        return new Page<>(0, Collections.<T>emptyList());
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}