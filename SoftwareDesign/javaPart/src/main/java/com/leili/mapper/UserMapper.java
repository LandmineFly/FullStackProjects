package com.leili.mapper;

import com.leili.pojo.User;
import com.leili.pojo.UserSelectOption;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface UserMapper {

    int insert(User user);

    User login(User user);

    int isDuplicate(User user);

    int updateByUsr(HashMap<String, User> user);

    int deleteByUsr(User user);

    List<User> select(UserSelectOption userSelectOption);

    Integer countAllWithCondition(UserSelectOption userSelectOption);

    Integer adminCount();

    Integer isLogin(User user);

    int setLogin(User user);

    int setLogout(User user);
}
