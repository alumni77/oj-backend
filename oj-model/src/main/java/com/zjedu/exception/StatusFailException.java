package com.zjedu.exception;

/**
 * @Author Zhong
 * @Create 2025/3/18 15:31
 * @Version 1.0
 * @Description
 */

public class StatusFailException extends Exception
{
    public StatusFailException() {
    }

    public StatusFailException(String message) {
        super(message);
    }

    public StatusFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public StatusFailException(Throwable cause) {
        super(cause);
    }

    public StatusFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
