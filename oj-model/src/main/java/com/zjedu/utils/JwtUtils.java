package com.zjedu.utils;

import com.zjedu.shiro.ShiroConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @Author Zhong
 * @Create 2025/3/18 15:41
 * @Version 1.0
 * @Description
 */

@Slf4j(topic = "oj-jwt")
@Data
@Component
@ConfigurationProperties(prefix = "oj.jwt")
public class JwtUtils
{

    private String secret;

    private long expire;

    private String header;

    private long checkRefreshExpire;

    @Resource
    private RedisUtils redisUtils;

    // 生成 token
    public String generateToken(String userId) {
        Date nowDate = new Date();
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);

        // 创建密钥
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(userId)
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(key, SignatureAlgorithm.HS512)  // 注意这里的变化
                .compact();

        redisUtils.set(ShiroConstant.SHIRO_TOKEN_KEY + userId, token, expire);
        redisUtils.set(ShiroConstant.SHIRO_TOKEN_REFRESH + userId, "1", checkRefreshExpire);
        return token;
    }

    // 解析 Claims
    public Claims getClaimByToken(String token) {
        try {
            // 创建密钥
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.debug("validate is token error ", e);
            return null;
        }
    }

    public void cleanToken(String uid)
    {
        redisUtils.del(ShiroConstant.SHIRO_TOKEN_KEY + uid, ShiroConstant.SHIRO_TOKEN_REFRESH + uid);
    }

    public boolean hasToken(String uid)
    {
        return redisUtils.hasKey(ShiroConstant.SHIRO_TOKEN_KEY + uid);
    }

    /**
     * token是否过期
     *
     * @return true：过期
     */
    public boolean isTokenExpired(Date expiration)
    {
        return expiration.before(new Date());
    }


}
