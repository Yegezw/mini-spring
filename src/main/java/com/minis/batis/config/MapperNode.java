package com.minis.batis.config;

/**
 * Mapper 节点
 */
public class MapperNode {

    public String namespace;
    public String id;
    public String parameterType;
    public String resultType;
    public String sql;
    public String parameter;

    public String toString() {
        return this.namespace + "." + this.id + " : " + this.sql;
    }
}
