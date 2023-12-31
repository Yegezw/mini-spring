package com.test.jdbc;

import com.minis.batis.factory.SqlSessionFactory;
import com.minis.batis.sqlsession.SqlSession;
import com.minis.beans.factory.config.annotation.Autowired;
import com.minis.jdbc.core.JdbcTemplate;
import com.minis.jdbc.core.PreparedStatementCallback;
import com.minis.jdbc.core.StatementCallback;
import com.minis.jdbc.support.RowMapper;
import com.test.pojo.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 普通 StatementCallback
     */
    public User getUserInfo1(int userId) {
        // 匿名内部类可以访问外部函数的 final 局部变量
        String sql = "select userId, userName, birthday from users where userId = " + userId;

        return (User) jdbcTemplate.query(new StatementCallback() {
            @Override
            public Object doInStatement(Statement statement) throws SQLException {
                try {
                    ResultSet res = statement.executeQuery(sql);
                    User user = null;
                    if (res.next()) {
                        int userId = res.getInt("userId");
                        String userName = res.getString("userName");
                        String birthday = res.getString("birthday");
                        Date data = new SimpleDateFormat("yyyy-MM-dd").parse(birthday);
                        user = new User(userId, userName, data);
                    }
                    return user;
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * PreparedStatementCallback
     */
    public User getUserInfo2(int userId) {
        // 匿名内部类可以访问外部函数的 final 局部变量
        String sql = "select userId, userName, birthday from users where userId = ?";

        return (User) jdbcTemplate.query(sql, new Object[]{userId}, new PreparedStatementCallback() {
            @Override
            public Object doInPreparedStatement(PreparedStatement statement) throws SQLException {
                try {
                    ResultSet res = statement.executeQuery();
                    User user = null;
                    if (res.next()) {
                        int userId = res.getInt("userId");
                        String userName = res.getString("userName");
                        String birthday = res.getString("birthday");
                        Date data = new SimpleDateFormat("yyyy-MM-dd").parse(birthday);
                        user = new User(userId, userName, data);
                    }
                    return user;
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * Mybatis
     */
    public User getUserInfo3(int userId) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return (User) sqlSession.selectOne("com.test.pojo.User.getUserInfo", new Object[]{userId}, new PreparedStatementCallback() {
            @Override
            public Object doInPreparedStatement(PreparedStatement statement) throws SQLException {
                try {
                    ResultSet res = statement.executeQuery();
                    User user = null;
                    if (res.next()) {
                        int userId = res.getInt("userId");
                        String userName = res.getString("userName");
                        String birthday = res.getString("birthday");
                        Date data = new SimpleDateFormat("yyyy-MM-dd").parse(birthday);
                        user = new User(userId, userName, data);
                    }
                    return user;
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    /**
     * RowMapper
     */
    public List<User> getUsers(int userId) {
        // 匿名内部类可以访问外部函数的 final 局部变量
        String sql = "select userId, userName, birthday from users where userId > ?";

        return jdbcTemplate.query(sql, new Object[]{userId},
                new RowMapper<User>() {
                    public User mapRow(ResultSet rs, int i) throws SQLException {
                        try {
                            int userId = rs.getInt("userId");
                            String userName = rs.getString("userName");
                            String birthday = rs.getString("birthday");
                            Date data = new SimpleDateFormat("yyyy-MM-dd").parse(birthday);
                            return new User(userId, userName, data);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
    }
}
