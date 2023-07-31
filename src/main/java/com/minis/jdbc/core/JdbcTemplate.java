package com.minis.jdbc.core;

import java.sql.*;

/**
 * Jdbc 模板
 */
public abstract class JdbcTemplate {

    private static final String url = "jdbc:mysql://127.0.0.1:3306/minis";
    private static final String user = "root";
    private static final String password = "root";

    public JdbcTemplate() {
    }

    /**
     * 模板方法
     */
    public Object query(String sql) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet res = null;
        Object ret = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);

            statement = connection.prepareStatement(sql);
            res = statement.executeQuery();

            ret = doInStatement(res); // 抽象方法
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (res != null) res.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            return ret;
        }
    }

    /**
     * 处理结果集
     */
    protected abstract Object doInStatement(ResultSet res);
}
