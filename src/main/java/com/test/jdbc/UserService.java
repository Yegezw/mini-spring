package com.test.jdbc;

import com.minis.jdbc.core.JdbcTemplate;
import com.test.pojo.User;

import java.util.List;

public class UserService {

    public User getUserInfo(int userId) {
        JdbcTemplate jdbcTemplate = new UserJdbcImpl();
        String sql = "select userId, userName, birthday from users where userId = " + userId;
        List<User> userList = (List<User>) jdbcTemplate.query(sql);
        return userList.get(0);
    }
}
