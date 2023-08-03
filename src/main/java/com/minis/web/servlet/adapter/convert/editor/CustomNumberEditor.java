package com.minis.web.servlet.adapter.convert.editor;

import com.minis.util.NumberUtils;
import com.minis.util.StringUtils;

import java.text.NumberFormat;

/**
 * 自定义数字编辑器
 */
public class CustomNumberEditor implements PropertyEditor {

    private Class<? extends Number> numberClass; // 数据类型
    private NumberFormat numberFormat;           // 指定格式
    private boolean allowEmpty;                  // 允许为空
    private Object value;                        // 值

    public CustomNumberEditor(Class<? extends Number> numberClass, boolean allowEmpty) throws IllegalArgumentException {
        this(numberClass, null, allowEmpty);
    }

    public CustomNumberEditor(Class<? extends Number> numberClass, NumberFormat numberFormat, boolean allowEmpty) throws IllegalArgumentException {
        this.numberClass = numberClass;
        this.numberFormat = numberFormat;
        this.allowEmpty = allowEmpty;
    }

    /**
     * 将字符串转换成 number 赋值
     */
    @Override
    public void setAsText(String text) {
        if (allowEmpty && !StringUtils.hasText(text)) setValue(null);
        else if (numberFormat != null)
            setValue(NumberUtils.parseNumber(text, numberClass, numberFormat)); // 给定格式
        else
            setValue(NumberUtils.parseNumber(text, numberClass));
    }

    /**
     * 接收 Object 作为参数
     */
    @Override
    public void setValue(Object value) {
        if (value instanceof Number)
            this.value = (NumberUtils.convertNumberToTargetClass((Number) value, numberClass));
        else
            this.value = value;
    }

    /**
     * 将 number 转换成格式化字符串
     */
    @Override
    public Object getAsText() {
        if (value == null) return "";

        if (this.numberFormat != null) return numberFormat.format(value); // 给定格式
        else return value.toString();
    }

    @Override
    public Object getValue() {
        return this.value;
    }
}
