package com.minis.beans.config;

/**
 * Setter 注入: 属性值
 */
public class PropertyValue {

    public final String type;
    public final String name;
    public final Object value;
    public final boolean isRef;

    public PropertyValue(String type, String name, Object value, boolean isRef) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.isRef = isRef;
    }

    public PropertyValue(String type, String name, Object value) {
        this(type, name, value, false);
    }

    public PropertyValue(String name, Object value) {
        this("", name, value, false);
    }
}
