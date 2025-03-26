package com.zjedu.common.exception;

/**
 * @Author Zhong
 * @Create 2025/3/26 20:10
 * @Version 1.0
 * @Description
 */

public class AccessException extends Exception
{
    public AccessException()
    {
        super();
    }

    public AccessException(String message)
    {
        super(message);
    }

    public AccessException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public AccessException(Throwable cause)
    {
        super(cause);
    }

    protected AccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}