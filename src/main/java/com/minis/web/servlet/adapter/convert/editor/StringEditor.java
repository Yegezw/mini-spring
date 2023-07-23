package com.minis.web.servlet.adapter.convert.editor;

/**
 * 字符串编辑器
 */
public class StringEditor implements PropertyEditor {

    private Class<String> strClass; // 数据类型
    private String strFormat;       // 指定格式
    private boolean allowEmpty;     // 允许为空
    private Object value;           // 值

    public StringEditor(Class<String> strClass, boolean allowEmpty) throws IllegalArgumentException {
        this(strClass, "", allowEmpty);
    }

    public StringEditor(Class<String> strClass, String strFormat, boolean allowEmpty) throws IllegalArgumentException {
        this.strClass = strClass;
        this.strFormat = strFormat;
        this.allowEmpty = allowEmpty;
    }

    @Override
    public void setAsText(String text) {
        setValue(text);
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String getAsText() {
        return value.toString();
    }

    @Override
    public Object getValue() {
        return this.value;
    }
}