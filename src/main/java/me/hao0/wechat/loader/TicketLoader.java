package me.hao0.wechat.loader;

import me.hao0.wechat.model.js.Ticket;
import me.hao0.wechat.model.js.TicketType;

/**
 * 凭证加载器
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 15/11/15
 */
public interface TicketLoader {

    /**
     * 获取Ticket
     * @param type ticket类型
     *             @see me.hao0.wechat.model.js.TicketType
     * @return 有效的ticket，若返回""或null，则触发重新从微信请求Ticket的方法refresh
     */
    String get(TicketType type);

    /**
     * 刷新Ticket
     * @param ticket 最新获取到的Ticket
     */
    void refresh(Ticket ticket);
}
