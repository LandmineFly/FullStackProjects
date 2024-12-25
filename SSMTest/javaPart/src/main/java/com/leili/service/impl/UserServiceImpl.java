package com.leili.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leili.bean.User;
import com.leili.service.UserService;
import com.leili.mapper.UserMapper;
import com.leili.utils.JwtUtil;
import com.leili.utils.MD5Util;
import com.leili.utils.Result;
import com.leili.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leili
 * @description 针对表【news_user】的数据库操作Service实现
 * @createDate 2023-11-27 20:34:40
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<Map<String, Object>> login(User user) {
        // 根据用户名查找用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        User selectedUser = userMapper.selectOne(wrapper);

        //查不到用户（用户名错误）
        if (selectedUser == null) {
            return Result.build(null, ResultCodeEnum.USERNAME_USED);
        }
        if (!StringUtils.isEmpty(user.getUserPwd())
            && MD5Util.encrypt(user.getUserPwd()).equals(selectedUser.getUserPwd())) {
            // 密码正确
            HashMap<String, Object> map = new HashMap<>();
            map.put("token", jwtUtil.createToken(Long.valueOf(selectedUser.getUid())));
            return Result.ok(map);
        }
        // 密码错误
        return Result.build(null, ResultCodeEnum.PASSWORD_ERROR);
    }

    @Override
    public Result<Map<String, Object>> getUserInfo(String token) {
        if (jwtUtil.isExpiration(token)) {
            return Result.build(null, ResultCodeEnum.NOT_LOGIN);
        }
        int userId = jwtUtil.getUserId(token).intValue();
        User loginUser = userMapper.selectById(userId);
        loginUser.setUserPwd("");
        Map<String, Object> map = new HashMap<>();
        map.put("loginUser", loginUser);
        return Result.ok(map);
    }

    @Override
    public Result<Map<String, Object>> checkUserName(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        Long count = userMapper.selectCount(wrapper);
        if (count > 0) {
            return Result.build(null, ResultCodeEnum.USERNAME_USED);
        }
        return Result.ok(null);
    }

    @Override
    public Result<Map<String, Object>> register(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        Long count = userMapper.selectCount(wrapper);
        if (count > 0) {
            return Result.build(null, ResultCodeEnum.USERNAME_USED);
        }

        user.setUserPwd(MD5Util.encrypt(user.getUserPwd()));
        userMapper.insert(user);

        return Result.ok(null);
    }
}




