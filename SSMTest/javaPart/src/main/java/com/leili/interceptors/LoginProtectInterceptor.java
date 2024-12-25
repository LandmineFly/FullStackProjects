package com.leili.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leili.utils.JwtUtil;
import com.leili.utils.Result;
import com.leili.utils.ResultCodeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 登录保护拦截器，用于在发布、修改、删除新闻时做登录校验（检查token是否过期）
 */
@Component
public class LoginProtectInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        boolean isExpiration = jwtUtil.isExpiration(token);
        if(!isExpiration){
            return true;
        }
        // 在拦截方法中，springMvc无法帮我们完成返回数据的自动格式转换（比如Java对象->json字符串）
        // 需要我们手动转换并使用原生 response 对象进行返回
        Result<Object> result = Result.build(null, ResultCodeEnum.NOT_LOGIN);
        // ObjectMapper是Jackson提供的一个主要类，实现了 Java类 和 json字符串 的互相转换
        ObjectMapper objectMapper = new ObjectMapper();
        String resultJson = objectMapper.writeValueAsString(result);
        // 使用原生 HttpServletResponse 对象进行返回
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(resultJson);
        return false;
    }
}
