package com.zjedu.judge.common.exception;

import lombok.Data;

/**
 * @Author Zhong
 * @Create 2025/3/24 21:55
 * @Version 1.0
 * @Description
 */

@Data
public class CompileError extends Exception
{
    private String message;
    private String stdout;
    private String stderr;

    public CompileError(String message, String stdout, String stderr)
    {
        super(message);
        this.message = message;
        this.stdout = stdout;
        this.stderr = stderr;
    }
}