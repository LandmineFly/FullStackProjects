package com.leili.controller;

import com.leili.mapper.UserMapper;
import com.leili.pojo.User;
import com.leili.pojo.UserSelectOption;
import com.leili.util.JwtUtils;
import com.leili.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("usr")
@CrossOrigin
public class UserController {
    @Autowired
    UserMapper mapper;

    private boolean isDuplicate(User user) {
        return mapper.isDuplicate(user) == 1;
    }

    private Integer countAllWithCondition(UserSelectOption userSelectOption) {
        return mapper.countAllWithCondition(userSelectOption);
    }

    @PostMapping("/insert")
    public Result<Integer> insert(@RequestBody User user) {
        try {
            if (isDuplicate(user)) {
                return Result.error("用户名重复，注册失败", null, false);
            }
            int insert = mapper.insert(user);
            return Result.ok(insert, null);
        } catch (Exception e) {
            return Result.error("注册失败", null, false);
        }
    }

    @PostMapping("/insert-in-login-page")
    public Result<Integer> insertInLoginPage(@RequestBody User user) {
        return insert(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Result<User>> login(@RequestBody User user) {
        try {
            User result = mapper.login(user);
            if (result != null) {
                String token = JwtUtils.generateToken(result.getUsr());
                return ResponseEntity
                        .ok()
                        .header("Token", token)
                        .body(Result.ok(result, null));
            } else {
                return ResponseEntity
                        .status(403)
                        .body(Result.error("用户名或密码错误", null, false));
            }
        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body(Result.error("未知错误，登录失败", null, false));
        }
    }

    @PutMapping("/update")
    public Result<Integer> update(@RequestBody HashMap<String, User> users) {
        try {
            if (Objects.equals(users.get("oldUser").getUsr(), users.get("newUser").getUsr())) {
                // 说明新旧用户名相同，不更新用户名
                users.get("newUser").setUsr(null);
            }
            if (Objects.equals(users.get("newUser").getPwd(), "")) {
                // 说明新密码为空，不更新密码
                users.get("newUser").setPwd(null);
            }
            // 试图将管理员修改为访客
            if (users.get("oldUser").getIsAdmin() == 1 && users.get("newUser").getIsAdmin() == 0) {
                if (mapper.adminCount() == 1) {
                    return Result.error("当前仅剩余唯一管理员，修改失败", null, false);
                }
            }
            // 防止在提交用户修改时，该用户已经被删除
            if (!isDuplicate(users.get("oldUser"))) {
                return Result.error("当前用户名不存在，修改失败", null, false);
            }
            // 防止新用户名重复
            if (users.get("newUser").getUsr() != null) {
                if (isDuplicate(users.get("newUser"))) {
                    return Result.error("新用户名重复，修改失败", null, false);
                }
            }
            int update = mapper.updateByUsr(users);
            return Result.ok(update, null);
        } catch (Exception e) {
            return Result.error("修改失败", null, false);
        }
    }

    @DeleteMapping("/delete")
    public Result<Integer> delete(@RequestBody User user) {
        try {
            if (!isDuplicate(user)) {
                return Result.error("当前用户名不存在，删除失败", null, false);
            }
            if (mapper.adminCount() == 1 && user.getIsAdmin() == 1) {
                return Result.error("当前仅剩余唯一管理员，删除失败", null, false);
            }
            int delete = mapper.deleteByUsr(user);
            return Result.ok(delete, null);
        } catch (Exception e) {
            return Result.error("删除失败", null, false);
        }
    }

    @PostMapping("/select")
    public Result<List<User>> select(@RequestBody UserSelectOption userSelectOption) {
        try {
            userSelectOption.setOffset(userSelectOption.getItemsPerPage() * (userSelectOption.getPage() - 1));
            List<User> list = mapper.select(userSelectOption);
            int totalCount = countAllWithCondition(userSelectOption);
            return Result.ok(list, totalCount);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Result.error("查询失败", null, false);
        }
    }

}
