package com.zjedu.gateway.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * @Author Zhong
 * @Create 2025/3/30 21:18
 * @Version 1.0
 * @Description
 */


@Slf4j
@Order(-1)
@Component
@RequiredArgsConstructor
//TODO 全局异常处理器未完成
public class GlobalExceptionHandler implements ErrorWebExceptionHandler
{

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex)
    {

        ServerHttpResponse response = exchange.getResponse();
        if (response.isCommitted())
        {
            //对于已经committed(提交)的response，就不能再使用这个response向缓冲区写任何东西
            return Mono.error(ex);
        }

        // header set 响应JSON类型数据，统一响应数据结构（适用于前后端分离JSON数据交换系统）
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);


        writeLog(exchange, ex);


        return Mono.empty();
    }

    //将错误信息以日志的形式记录下来
    private void writeLog(ServerWebExchange exchange, Throwable ex)
    {
        ServerHttpRequest request = exchange.getRequest();
        URI uri = request.getURI();
        String host = uri.getHost();
        int port = uri.getPort();
        log.error("[gateway]-host:{} ,port:{}，url:{},  errormessage:",
                host,
                port,
                request.getPath(),
                ex);
    }
}