package me.hao0.wechat.model.data.article;

import com.google.common.base.Objects;

/**
 * 在获取图文阅读分时数据时才有该字段，代表用户从哪里进入来阅读该图文
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 20/11/15
 */
public enum ArticleSource {

    /**
     * 会话
     */
    SESSION(0),

    /**
     * 好友
     */
    FRIEND(1),

    /**
     * 朋友圈
     */
    TIMELINE(2),

    /**
     * 腾讯微博
     */
    WEIBO(3),

    /**
     * 历史消息
     */
    HISTORY(4),

    /**
     * 图文页右上角菜单
     */
    OTHER(5);

    private Integer value;

    private ArticleSource(Integer scope){
        this.value = scope;
    }

    public Integer value(){
        return value;
    }

    public static ArticleSource from(Integer s){
        for (ArticleSource source : ArticleSource.values()){
            if (Objects.equal(source.value(), s)){
                return source;
            }
        }
        return OTHER;
    }
}
