package com.zjedu.common.exception;

/**
 * @Author Zhong
 * @Create 2025/3/19 20:48
 * @Version 1.0
 * @Description
 */

public class StatusAccessDeniedException extends Exception
{
    public StatusAccessDeniedException()
    {
    }

    public StatusAccessDeniedException(String message)
    {
        super(message);
    }

    public StatusAccessDeniedException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public StatusAccessDeniedException(Throwable cause)
    {
        super(cause);
    }

    public StatusAccessDeniedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
