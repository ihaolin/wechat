package me.hao0.wechat.model.data.user;

import com.google.common.base.Objects;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 20/11/15
 */
public enum UserSource {

    /**
     * 其他: 包括带参数二维码
     */
    OTHER(0),

    /**
     * 扫二维码
     */
    SCAN(3),

    /**
     * 名片分享
     */
    CARD(17),

    /**
     * 代表搜号码（即微信添加朋友页的搜索）
     */
    SEARCH_NUMBER(35),

    /**
     * 查询微信公众帐号
     */
    SEARCH_ACCOUNT(39),

    /**
     * 图文页右上角菜单
     */
    MENU(43);

    private Integer value;

    private UserSource(Integer scope){
        this.value = scope;
    }

    public Integer value(){
        return value;
    }

    public static UserSource from(Integer s){
        for (UserSource source : UserSource.values()){
            if (Objects.equal(source.value(), s)){
                return source;
            }
        }
        throw new IllegalArgumentException("非法的用户渠道: " + s);
    }
}
