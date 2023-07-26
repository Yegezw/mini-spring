package com.minis.beans.config;

/**
 * 构造器注入: 参数值
 */
public class ConstructorArgumentValue {

    public String type;
    public String name;
    public Object value;

    public ConstructorArgumentValue(String type, Object value) {
        this.type = type;
        this.value = value;
    }

    public ConstructorArgumentValue(String type, String name, Object value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }
}
