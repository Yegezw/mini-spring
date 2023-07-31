package com.minis.web.servlet.adapter.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * 默认对象映射器
 */
public class DefaultObjectMapper implements ObjectMapper {

    private String dateFormat = "yyyy-MM-dd";
    private DateTimeFormatter datetimeFormatter = DateTimeFormatter.ofPattern(dateFormat);

    private String decimalFormat = "#,##0.00";
    private DecimalFormat decimalFormatter = new DecimalFormat(decimalFormat);

    public DefaultObjectMapper() {
    }

    @Override
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        this.datetimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
    }

    @Override
    public void setDecimalFormat(String decimalFormat) {
        this.decimalFormat = decimalFormat;
        this.decimalFormatter = new DecimalFormat(decimalFormat);
    }

    @Override
    public String writeValuesAsString(Object obj) {
        // 如果 obj 已经是 String, 直接返回
        if (obj instanceof String) return (String) obj;

        // 如果 obj 是容器
        if (obj instanceof Collection) {
            StringBuilder sb = new StringBuilder();

            Collection collection = (Collection) obj;
            for (Object item : collection) sb.append(item.toString()).append("\n");

            return sb.toString();
        }
        if (obj instanceof Map) {
            StringBuilder sb = new StringBuilder();

            Map map = (Map) obj;
            Set keySet = map.keySet();
            for (Object key : keySet) sb.append(key + ": " + map.get(key)).append("\n");

            return sb.toString();
        }

        // obj 是复杂类型
        StringBuilder jsonStr = new StringBuilder("{");
        Class<?> clazz = obj.getClass();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            Object value = null;

            String fieldStr = "";
            String valueStr = "";

            try {
                field.setAccessible(true);
                value = field.get(obj);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }

            if (value instanceof Date) {
                LocalDate localDate = ((Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                valueStr = localDate.format(this.datetimeFormatter);
            } else if (value instanceof BigDecimal || value instanceof Double || value instanceof Float) {
                valueStr = this.decimalFormatter.format(value);
            } else {
                valueStr = value.toString();
            }

            if (jsonStr.toString().equals("{")) fieldStr = "\"" + name + "\":\"" + valueStr + "\"";
            else fieldStr = ",\"" + name + "\":\"" + valueStr + "\"";
            jsonStr.append(fieldStr);
        }
        jsonStr.append("}");

        return jsonStr.toString();
    }
}
