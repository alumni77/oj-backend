package com.zjedu.judge.common.exception;

import lombok.Data;

/**
 * @Author Zhong
 * @Create 2025/3/24 21:54
 * @Version 1.0
 * @Description
 */

@Data
public class SystemError extends Exception
{
    private String message;
    private String stdout;
    private String stderr;

    public SystemError(String message, String stdout, String stderr)
    {
        super(message + " " + stderr);
        this.message = message;
        this.stdout = stdout;
        this.stderr = stderr;
    }

}