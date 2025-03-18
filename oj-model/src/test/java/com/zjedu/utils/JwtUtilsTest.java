package com.zjedu.utils;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.util.Base64;

/**
 * @Author Zhong
 * @Create 2025/3/18 20:50
 * @Version 1.0
 * @Description
 */

public class JwtUtilsTest
{

    @Test
    public void test1()
    {
        // 生成适合 HS512 算法的安全密钥
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        String secretString = Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println(secretString);
    }

}
