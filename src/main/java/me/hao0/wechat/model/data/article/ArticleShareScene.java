package me.hao0.wechat.model.data.article;

import com.google.common.base.Objects;

/**
 * 分享的场景
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 20/11/15
 */
public enum ArticleShareScene {

    /**
     * 未知
     */
    UNKNOWN(0),

    /**
     * 好友转发
     */
    FRIEND(1),

    /**
     * 朋友圈
     */
    TIMELINE(2),

    /**
     * 腾讯微博
     */
    WEIBO(3);


    private Integer value;

    private ArticleShareScene(Integer scope){
        this.value = scope;
    }

    public Integer value(){
        return value;
    }

    public static ArticleShareScene from(Integer s){
        for (ArticleShareScene source : ArticleShareScene.values()){
            if (Objects.equal(source.value(), s)){
                return source;
            }
        }
        return UNKNOWN;
    }
}
