package com.github.ywj1352.dao;

import com.github.ywj1352.pojo.User;
import org.springframework.stereotype.Component;

@Component
public class UserDao {

    public User findAllUser(){
        User user = new User();
        user.setId("1");
        user.setName("杨文杰");
        user.setAge(30);
        return user;
    }
}
