package com.minis.jdbc.core;

import com.minis.beans.factory.config.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;

/**
 * Jdbc 模板
 */
public class JdbcTemplate {

    @Autowired
    private DataSource dataSource;

    public JdbcTemplate() {
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 普通 Statement
     */
    public Object query(StatementCallback callback) {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();

            return callback.doInStatement(statement); // 回调
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    /**
     * PreparedStatement
     */
    public Object query(String sql, Object[] args, PreparedStatementCallback callback) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(sql);
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    if (arg instanceof String) {
                        statement.setString(i + 1, (String) arg);
                    } else if (arg instanceof Integer) {
                        statement.setInt(i + 1, (int) arg);
                    } else if (arg instanceof java.util.Date) {
                        statement.setString(i + 1, new SimpleDateFormat("yyyy-MM-dd").format((java.util.Date) arg));
                    }
                }
            }

            return callback.doInPreparedStatement(statement); // 回调
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }
}
