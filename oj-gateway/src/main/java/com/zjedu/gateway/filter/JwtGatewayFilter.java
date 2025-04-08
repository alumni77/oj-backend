package com.zjedu.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zjedu.common.result.CommonResult;
import com.zjedu.common.result.ResultStatus;
import com.zjedu.shiro.ShiroConstant;
import com.zjedu.utils.JwtUtils;
import com.zjedu.utils.RedisUtils;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class JwtGatewayFilter implements GlobalFilter, Ordered
{
    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private RedisUtils redisUtils;

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // 定义不需要认证的匿名路径
    private final List<String> anonymousUrls = Arrays.asList(
            // 全局Swagger配置
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/doc.html",
            "/webjars/**",  // Swagger UI依赖的资源

            // Passport服务的匿名接口
            "/api/passport/login",
            "/api/passport/get-code",
            "/api/passport/register",
            "/api/passport/reset-password",
            "/api/passport/get-user",
            "/api/passport/get-user-home-info",
            "/api/passport/get-user-by-uid",
            "/api/passport/get-recent7-ac-rank",
            // Account服务的匿名接口
            "/api/account/check-username",
            "/api/account/home-carousel",
            "/api/account/get-recent-seven-ac-rank",
            "/api/account/get-recent-updated-problem",
            // Judge服务的匿名接口
            "/api/judge/common-judge-list",
            "/api/judge/get-judge-by-id",
            "/api/judge/get-problem-by-id",
            // JudgeServe服务的匿名接口
            "/api/judgeserve/get-submission-list",
            "/api/judgeserve/get-submission-detail",
            "/api/judgeserve/check-submissions-status",
            "/api/judgeserve/get-all-case-result",
            // Problem服务的匿名接口
            "/api/problem/get-problem-list",
            "/api/problem/get-random-problem",
            "/api/problem/get-problem-detail",
            "/api/problem/get-rank-list",
            // Common服务的匿名接口
            "/api/common/**",
            // Training服务的匿名接口
            "/api/training/get-training-list",
            "/api/training/get-training-category",
            // File服务的匿名接口

            // Admin服务的匿名接口
            "/api/admin/login",


            // Passport服务的Swagger配置
            "/api/passport/v3/api-docs/**",
            "/api/passport/swagger-ui/**",
            "/api/passport/doc.html",
            // Account服务的Swagger配置
            "/api/account/v3/api-docs/**",
            "/api/account/swagger-ui/**",
            "/api/account/doc.html",
            // Judge服务的Swagger配置
            "/api/judge/v3/api-docs/**",
            "/api/judge/swagger-ui/**",
            "/api/judge/doc.html",
            // JudgeServe服务的Swagger配置
            "/api/judgeserve/v3/api-docs/**",
            "/api/judgeserve/swagger-ui/**",
            "/api/judgeserve/doc.html",
            // Problem服务的Swagger配置
            "/api/problem/v3/api-docs/**",
            "/api/problem/swagger-ui/**",
            "/api/problem/doc.html",
            // Common服务的Swagger配置
            "/api/common/v3/api-docs/**",
            "/api/common/swagger-ui/**",
            "/api/common/doc.html",
            // Training服务的Swagger配置
            "/api/training/v3/api-docs/**",
            "/api/training/swagger-ui/**",
            "/api/training/doc.html",
            // File服务的Swagger配置
            "/api/file/v3/api-docs/**",
            "/api/file/swagger-ui/**",
            "/api/file/doc.html",
            // Admin服务的Swagger配置
            "/api/admin/v3/api-docs/**",
            "/api/admin/swagger-ui/**",
            "/api/admin/doc.html"


    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getURI().getPath();
        log.debug("正在处理请求: {}", path);

        // 检查是否为匿名访问路径
        if (isAnonymousUrl(path))
        {
            return chain.filter(exchange);
        }

        // 获取token
        String token = request.getHeaders().getFirst("Authorization");
        if (!StringUtils.hasText(token))
        {
            return unauthorizedResponse(response, "未登录，请先登录！");
        }
        log.info("token:{}", token);

        try
        {
            // 验证token
            Claims claim = jwtUtils.getClaimByToken(token);
            if (claim == null || jwtUtils.isTokenExpired(claim.getExpiration()))
            {
                return unauthorizedResponse(response, "登录状态已失效，请重新登录！");
            }

            String userId = claim.getSubject();
            boolean hasToken = jwtUtils.hasToken(userId);
            if (!hasToken)
            {
                return unauthorizedResponse(response, "登录状态已失效，请重新登录！");
            }

            // 检查token是否需要刷新
            if (!redisUtils.hasKey(ShiroConstant.SHIRO_TOKEN_REFRESH + userId))
            {
                try
                {
                    // 获取锁20s
                    boolean lock = redisUtils.getLock(ShiroConstant.SHIRO_TOKEN_LOCK + userId, 20);
                    if (lock)
                    {
                        // 生成新token
                        String newToken = jwtUtils.generateToken(userId);

                        // 添加响应头
                        response.getHeaders().set("Authorization", newToken);
                        response.getHeaders().set("Access-Control-Expose-Headers", "Refresh-Token,Authorization,Url-Type");
                        response.getHeaders().set("Access-Control-Allow-Credentials", "true");
                        response.getHeaders().set("Refresh-Token", "true");
                        if (request.getHeaders().containsKey("Url-Type"))
                        {
                            response.getHeaders().set("Url-Type", request.getHeaders().getFirst("Url-Type"));
                        }
                        exchange = exchange.mutate().response(response).build();
                    }
                } finally
                {
                    // 确保锁释放
                    redisUtils.releaseLock(ShiroConstant.SHIRO_TOKEN_LOCK + userId);
                }
            }

            // 将用户信息添加到请求头中，供下游服务使用
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Id", userId)
                    .build();

            return chain.filter(exchange.mutate().request(mutatedRequest).build());

        } catch (Exception e)
        {
            log.error("JWT验证错误", e);
            return unauthorizedResponse(response, "Token验证失败");
        }
    }

    private boolean isAnonymousUrl(String path)
    {
        return anonymousUrls.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    private Mono<Void> unauthorizedResponse(ServerHttpResponse response, String message)
    {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        CommonResult<Void> result = CommonResult.errorResponse(message, ResultStatus.ACCESS_DENIED);
        byte[] bytes;
        try
        {
            bytes = new ObjectMapper().writeValueAsString(result).getBytes(StandardCharsets.UTF_8);
        } catch (JsonProcessingException e)
        {
            bytes = "{\"code\":401,\"message\":\"未授权\"}".getBytes(StandardCharsets.UTF_8);
        }

        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }

    @Override
    public int getOrder()
    {
        return -100; // 高优先级，确保过滤器尽早执行
    }
}