package com.leili.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    // 从配置文件读取
    private static String SECRET_KEY; // 密钥，对于安全性要求不高的小型应用，可以直接使用静态密钥
    private static long EXPIRATION_MS; // 过期时间
    private static long ABSOLUTE_EXPIRATION_MS; // 绝对过期时间（即用户一次登录的最长有效时间）

    // Spring并不能直接注入static变量，所以这里曲线救国
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.expire}")
    private long expirationMs;
    @Value("${jwt.absolute-expire}")
    private long absoluteExpirationMs;

    // @PostConstruct 会使该方法在Spring完成依赖注入后被自动调用
    @PostConstruct
    public void init() {
        SECRET_KEY = secretKey;
        EXPIRATION_MS = expirationMs;
        ABSOLUTE_EXPIRATION_MS = absoluteExpirationMs;
    }

    // 生成 Token
    // 带有绝对过期时间，通常是刷新Token时使用
    public static String generateToken(String username, Date absoluteExpiration) {
        Map<String, Object> claims = new HashMap<>();
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_MS);
        /*
         *  注意：在 JWT 的 Claims 中，自定义的时间字段（如 absExp）
         *  会被序列化为时间戳（Long 类型），而非 Date 对象
         */
        // 签发时间
        claims.put("iat", now);
        // 绝对过期时间
        claims.put("absExp", absoluteExpiration);
        return Jwts.builder()
                   .setClaims(claims)
                   .setSubject(username)
                   // 过期时间，该 Date 对象就不会序列化为时间戳，而是它本身
                   .setExpiration(expiration)
                   .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                   .compact();
    }

    // 不带绝对过期时间，通常是用户登录时使用
    public static String generateToken(String username) {
        Date absoluteExpiration = new Date(System.currentTimeMillis() + ABSOLUTE_EXPIRATION_MS);
        return generateToken(username, absoluteExpiration);
    }

    // 解析 Token
    public static Claims parseToken(String token) {
        return Jwts.parser()
                   .setSigningKey(SECRET_KEY)
                   .parseClaimsJws(token)
                   .getBody();
    }
}
