package com.test.config.mvc;

import com.minis.web.servlet.adapter.convert.editor.PropertyEditor;
import com.minis.web.servlet.adapter.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义日期编辑器
 */
public class CustomDateEditor implements PropertyEditor {

    private Class<Date> dateClass;                // 数据类型
    private SimpleDateFormat dateFormatter;       // 指定格式
    private boolean allowEmpty;                   // 允许为空
    private Date value;                           // 值

    public CustomDateEditor() throws IllegalArgumentException {
        this(Date.class, "yyyy-MM-dd", true);
    }

    public CustomDateEditor(Class<Date> dateClass) throws IllegalArgumentException {
        this(dateClass, "yyyy-MM-dd", true);
    }

    public CustomDateEditor(Class<Date> dateClass, boolean allowEmpty) throws IllegalArgumentException {
        this(dateClass, "yyyy-MM-dd", allowEmpty);
    }

    public CustomDateEditor(Class<Date> dateClass, String pattern, boolean allowEmpty) throws IllegalArgumentException {
        this.dateClass = dateClass;
        this.dateFormatter = new SimpleDateFormat(pattern);
        this.allowEmpty = allowEmpty;
    }

    @Override
    public void setAsText(String text) {
        if (this.allowEmpty && !StringUtils.hasText(text)) setValue(null);
        else {
            try {
                setValue(dateFormatter.parse(text));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void setValue(Object value) {
        this.value = (Date) value;
    }

    @Override
    public String getAsText() {
        if (value == null) return "";
        else return dateFormatter.format(value);
    }

    @Override
    public Object getValue() {
        return this.value;
    }
}