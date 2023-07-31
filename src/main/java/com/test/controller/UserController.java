package com.test.controller;

import com.minis.beans.factory.config.annotation.Autowired;
import com.minis.web.config.RequestMapping;
import com.minis.web.servlet.config.ResponseBody;
import com.test.jdbc.UserService;
import com.test.pojo.User;
import com.test.pojo.UserId;

import java.util.List;

public class UserController {

    @Autowired
    private UserService userService;

    /**
     * <a href="http://localhost:8080/getUserInfo?userId=1">测试连接</a>
     */
    @ResponseBody
    @RequestMapping("/getUserInfo")
    public User getUserInfo(UserId userId) {
        User userInfo1 = userService.getUserInfo1(userId.getUserId());
        System.out.println(userInfo1);

        User userInfo2 = userService.getUserInfo2(userId.getUserId());
        System.out.println(userInfo2);

        return userInfo2;
    }

    /**
     * <a href="http://localhost:8080/getUsers">测试连接</a>
     */
    @ResponseBody
    @RequestMapping("/getUsers")
    public List<User> getUsers(UserId userId) {
        List<User> users = userService.getUsers(0);
        System.out.println(users);

        return users;
    }
}
