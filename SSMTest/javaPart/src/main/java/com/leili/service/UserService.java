package com.leili.service;

import com.leili.bean.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.leili.utils.Result;

import java.util.Map;

/**
* @author leili
* @description 针对表【news_user】的数据库操作Service
* @createDate 2023-11-27 20:34:40
*/
public interface UserService extends IService<User> {

    /**
     * 登录方法
     */
    Result<Map<String, Object>> login(User user);

    /**
     * 根据token获取用户信息
     */
    Result<Map<String, Object>> getUserInfo(String token);

    /**
     * 检查用户名是否被占用
     */
    Result<Map<String, Object>> checkUserName(String username);

    /**
     * 用户注册
     */
    Result<Map<String, Object>> register(User user);
}
