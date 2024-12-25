package com.leili.controller;

import com.alibaba.druid.util.StringUtils;
import com.leili.bean.User;
import com.leili.service.UserService;
import com.leili.utils.JwtUtil;
import com.leili.utils.Result;
import com.leili.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("login")
    public Result<Map<String, Object>> login(@RequestBody User user) {
        return userService.login(user);
    }

    @GetMapping("getUserInfo")
    public Result<Map<String, Object>> getUserInfo(@RequestHeader String token) {
        return userService.getUserInfo(token);
    }

    @PostMapping("checkUserName")
    public Result<Map<String, Object>> checkUserName(String username) {
        return userService.checkUserName(username);
    }

    @PostMapping("regist")
    public Result<Map<String, Object>> register(@RequestBody User user) {
        return userService.register(user);
    }

    @GetMapping("checkLogin")
    public Result<Object> checkLogin(@RequestHeader String token) {
        if (StringUtils.isEmpty(token) || jwtUtil.isExpiration(token)) {
            return Result.build(null, ResultCodeEnum.NOT_LOGIN);
        }
        return Result.ok(null);
    }
}
