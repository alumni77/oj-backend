package com.zjedu.common.exception;

/**
 * @Author Zhong
 * @Create 2025/3/19 20:50
 * @Version 1.0
 * @Description
 */

public class StatusSystemErrorException extends Exception
{
    public StatusSystemErrorException()
    {
    }

    public StatusSystemErrorException(String message)
    {
        super(message);
    }

    public StatusSystemErrorException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public StatusSystemErrorException(Throwable cause)
    {
        super(cause);
    }

    public StatusSystemErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
