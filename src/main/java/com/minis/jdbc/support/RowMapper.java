package com.minis.jdbc.support;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 行映射器
 */
public interface RowMapper<T> {

    T mapRow(ResultSet rs, int rowNum) throws SQLException;
}
