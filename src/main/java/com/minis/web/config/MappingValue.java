package com.minis.web.config;

public class MappingValue {

    public String uri;
    public String clazz;
    public String method;

    public MappingValue(String uri, String clazz, String method) {
        this.uri = uri;
        this.clazz = clazz;
        this.method = method;
    }
}
