package com.minis.jdbc.support.res;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 结果集提取器
 */
public interface ResultSetExtractor<T> {

	/**
	 * 提取数据
	 */
	T extractData(ResultSet rs) throws SQLException;
}
