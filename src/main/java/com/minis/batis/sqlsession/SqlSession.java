package com.minis.batis.sqlsession;

import com.minis.batis.factory.SqlSessionFactory;
import com.minis.jdbc.core.JdbcTemplate;
import com.minis.jdbc.core.PreparedStatementCallback;

public interface SqlSession {

    void setJdbcTemplate(JdbcTemplate jdbcTemplate);

    void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory);

    Object selectOne(String sqlId, Object[] args, PreparedStatementCallback callback);
}
