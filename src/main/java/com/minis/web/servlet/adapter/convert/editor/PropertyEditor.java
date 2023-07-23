package com.minis.web.servlet.adapter.convert.editor;

/**
 * 属性编辑器
 */
public interface PropertyEditor {

    void setAsText(String text);

    void setValue(Object value);

    Object getAsText();

    Object getValue();
}
