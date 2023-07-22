package com.minis.web.servlet.mapping;

import com.minis.web.servlet.config.HandlerMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * 处理器映射器
 */
public interface HandlerMapping {

    /**
     * HttpServletRequest -> URL -> Controller + Method -> HandlerMethod
     */
    HandlerMethod getHandler(HttpServletRequest request) throws Exception;
}
