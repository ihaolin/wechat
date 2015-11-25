package me.hao0.wechat.loader;

import com.google.common.base.Strings;
import me.hao0.wechat.model.js.Ticket;
import me.hao0.wechat.model.js.TicketType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一个内存式Ticket加载器(生产环境不推荐使用)
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 15/11/15
 * @since 1.3.0
 */
public class DefaultTicketLoader implements TicketLoader {

    private final Map<TicketType, Ticket> tickets = new ConcurrentHashMap<>();

    @Override
    public String get(TicketType type) {
        Ticket t = tickets.get(type);
        return (t == null
                || Strings.isNullOrEmpty(t.getTicket())
                || System.currentTimeMillis() > t.getExpireAt()) ? null : t.getTicket();
    }

    @Override
    public void refresh(Ticket ticket) {
        tickets.put(ticket.getType(), ticket);
    }
}
