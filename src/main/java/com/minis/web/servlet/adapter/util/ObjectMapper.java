package com.minis.web.servlet.adapter.util;

/**
 * 对象映射器
 */
public interface ObjectMapper {

    void setDateFormat(String dateFormat);

    void setDecimalFormat(String decimalFormat);

    String writeValuesAsString(Object obj);
}
