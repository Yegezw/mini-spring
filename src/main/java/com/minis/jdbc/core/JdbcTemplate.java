package com.minis.jdbc.core;

import com.minis.beans.factory.config.annotation.Autowired;
import com.minis.jdbc.support.RowMapper;
import com.minis.jdbc.support.arg.ArgumentPreparedStatementSetter;
import com.minis.jdbc.support.res.RowMapperResultSetExtractor;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

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
     * 普通 Statement + StatementCallback
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
     * PreparedStatement + PreparedStatementCallback
     */
    public Object query(String sql, Object[] args, PreparedStatementCallback callback) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(sql);
            // 通过 ArgumentPreparedStatementSetter 统一设置参数值
            ArgumentPreparedStatementSetter setter = new ArgumentPreparedStatementSetter(args);
            setter.setValues(statement);

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

    /**
     * PreparedStatement + RowMapper
     */
    public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) {
        // 结果集提取器 -> 行映射器
        RowMapperResultSetExtractor<T> resultExtractor = new RowMapperResultSetExtractor<>(rowMapper);
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(sql);
            // 通过 ArgumentPreparedStatementSetter 统一设置参数值
            ArgumentPreparedStatementSetter argumentSetter = new ArgumentPreparedStatementSetter(args);
            argumentSetter.setValues(statement);

            rs = statement.executeQuery();

            // 提取数据
            return resultExtractor.extractData(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }
}
