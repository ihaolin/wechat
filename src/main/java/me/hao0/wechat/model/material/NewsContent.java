package me.hao0.wechat.model.material;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 图文素材类
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 12/11/15
 */
public class NewsContent implements Serializable {

    private static final long serialVersionUID = 8483540691949616866L;

    @JsonProperty("news_item")
    private List<NewsContentItem> items;

    public List<NewsContentItem> getItems() {
        return items;
    }

    public void setItems(List<NewsContentItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "NewsContent{" +
                "items=" + items +
                '}';
    }
}
