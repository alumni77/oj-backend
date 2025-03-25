package com.zjedu.judge.common.exception;

import lombok.Data;

/**
 * @Author Zhong
 * @Create 2025/3/25 14:48
 * @Version 1.0
 * @Description
 */

@Data
public class SubmitError extends Exception
{
    private String message;
    private String stdout;
    private String stderr;

    public SubmitError(String message, String stdout, String stderr)
    {
        super(message);
        this.message = message;
        this.stdout = stdout;
        this.stderr = stderr;
    }
}