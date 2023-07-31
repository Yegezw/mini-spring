package com.minis.jdbc.core;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * 普通 Statement 回调
 */
public interface StatementCallback {

    Object doInStatement(Statement statement) throws SQLException;
}
