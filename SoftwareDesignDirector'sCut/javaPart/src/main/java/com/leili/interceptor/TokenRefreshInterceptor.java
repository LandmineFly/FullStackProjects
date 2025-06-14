package com.leili.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leili.util.JwtUtils;
import com.leili.util.Result;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class TokenRefreshInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("Token");
        if (token == null) {
            writeJsonResponse(response, 401, "Token缺失");
            return false;
        }
        try {
            Claims claims = JwtUtils.parseToken(token);
            Date now = new Date();
            // 检查 Token 是否绝对过期
            Date absoluteExpiration = new Date((Long) claims.get("absExp"));
            if (now.after(absoluteExpiration)) {
                throw new ExpiredJwtException(null, null, "Token已绝对过期");
            }
            // 检查 Token 剩余时间是否小于10分钟
            long remainTime = claims.getExpiration().getTime() - now.getTime();
            if (remainTime < 10 * 60 * 1000) {
                // 生成新 Token 并返回给前端，注意绝对过期时间不能变
                String newToken = JwtUtils.generateToken(claims.getSubject(), absoluteExpiration);
                response.setHeader("New-Token", newToken);
            }
            return true;
        } catch (ExpiredJwtException e) {
            writeJsonResponse(response, 401, "Token已过期");
            return false;
        } catch (MalformedJwtException e) {
            writeJsonResponse(response, 401, "Token解析失败");
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            writeJsonResponse(response, 401, "权限认证出现未知错误，禁止访问");
            return false;
        }
    }

    private void writeJsonResponse(HttpServletResponse response, int statusCode, String message) {
        try {
            response.setStatus(statusCode);
            response.setContentType("application/json;charset=UTF-8");

            Result<Void> result = new Result<>(statusCode, message, null, null);
            String json = new ObjectMapper().writeValueAsString(result);

            PrintWriter writer = response.getWriter();
            writer.print(json);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
