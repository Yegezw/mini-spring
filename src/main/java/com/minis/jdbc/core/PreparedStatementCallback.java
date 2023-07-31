package com.minis.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * PreparedStatementCallback 回调
 */
public interface PreparedStatementCallback {

    Object doInPreparedStatement(PreparedStatement statement) throws SQLException;
}