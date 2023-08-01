package com.minis.batis.sqlsession;

import com.minis.batis.factory.SqlSessionFactory;
import com.minis.jdbc.core.JdbcTemplate;
import com.minis.jdbc.core.PreparedStatementCallback;

public class DefaultSqlSession implements SqlSession {

    private JdbcTemplate jdbcTemplate;
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public Object selectOne(String sqlId, Object[] args, PreparedStatementCallback callback) {
        System.out.println("sqlId: " + sqlId);
        String sql = sqlSessionFactory.getMapperNode(sqlId).sql;
        System.out.println(sql);

        return jdbcTemplate.query(sql, args, callback);
    }

    private void buildParameter() {
    }

    private Object resultSet2Obj() {
        return null;
    }

    // ======================================== getter()、setter() ========================================

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}
