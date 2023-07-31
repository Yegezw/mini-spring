package com.test.jdbc;

import com.minis.jdbc.core.JdbcTemplate;
import com.test.pojo.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserJdbcImpl extends JdbcTemplate {

    @Override
    protected Object doInStatement(ResultSet res) {
        List<User> list = new ArrayList<>();

        try {
            while (res.next()) {
                int userId = res.getInt("userId");
                String userName = res.getString("userName");
                String birthday = res.getString("birthday");
                Date data = new SimpleDateFormat("yyyy-MM-dd").parse(birthday);
                list.add(new User(userId, userName, data));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return list;
    }
}
