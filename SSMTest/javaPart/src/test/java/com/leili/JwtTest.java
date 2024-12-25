package com.leili;

import com.leili.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void test() {
        //生成 传入用户标识
        String token = jwtUtil.createToken(1L);
        System.out.println("token = " + token);

        //解析用户标识
        int userId = jwtUtil.getUserId(token).intValue();
        System.out.println("userId = " + userId);

        //校验是否到期! false 未到期 true到期
        boolean expiration = jwtUtil.isExpiration(token);
        System.out.println("expiration = " + expiration);
    }

}

