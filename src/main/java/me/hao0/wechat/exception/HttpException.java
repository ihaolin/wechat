package me.hao0.wechat.exception;

/**
 * Author: haolin
 * Email: haolin.h0@gmail.com
 * Date: 10/11/15
 */
public class HttpException extends RuntimeException {
    public HttpException() {
        super();
    }

    public HttpException(String message) {
        super(message);
    }

    public HttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpException(Throwable cause) {
        super(cause);
    }

    protected HttpException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
