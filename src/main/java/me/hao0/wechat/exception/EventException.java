package me.hao0.wechat.exception;

/**
 * 微信事件异常，接收微信消息时有可能抛出
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 5/11/15
 * @see me.hao0.wechat.model.message.receive.event.RecvEventType#from
 * @since 1.4.0
 */
public class EventException extends RuntimeException {

    public EventException() {
        super();
    }

    public EventException(String message) {
        super(message);
    }

    public EventException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventException(Throwable cause) {
        super(cause);
    }

    protected EventException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
