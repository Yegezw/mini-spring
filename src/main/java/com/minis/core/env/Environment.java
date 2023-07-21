package com.minis.core.env;

/**
 * 环境: 可获取属性
 */
public interface Environment extends PropertyResolver {

    /**
     * 获取活动配置文件
     */
    String[] getActiveProfiles();

    /**
     * 获取默认配置文件
     */
    String[] getDefaultProfiles();

    /**
     * 接收配置文件
     */
    boolean acceptsProfiles(String... profiles);
}
