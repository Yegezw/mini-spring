package com.minis.jdbc.support.arg;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

/**
 * PreparedStatement 参数设置器
 */
public class ArgumentPreparedStatementSetter {

    private final Object[] args; // 参数数组

    public ArgumentPreparedStatementSetter(Object[] args) {
        this.args = args;
    }

    public void setValues(PreparedStatement statement) throws SQLException {
        if (this.args != null) {
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                doSetValue(statement, i + 1, arg);
            }
        }
    }

    protected void doSetValue(PreparedStatement statement, int parameterPosition, Object argValue) throws SQLException {
        if (argValue instanceof String) {
            statement.setString(parameterPosition, (String) argValue);
        } else if (argValue instanceof Integer) {
            statement.setInt(parameterPosition, (int) argValue);
        } else if (argValue instanceof java.util.Date) {
            statement.setString(parameterPosition, new SimpleDateFormat("yyyy-MM-dd").format((java.util.Date) argValue));
        }
    }
}
