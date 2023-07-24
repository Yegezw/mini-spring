package com.minis.web.servlet.resolver;

import com.minis.web.servlet.resolver.view.View;

/**
 * 视图解析器
 */
public interface ViewResolver {

    /**
     * 解析视图名称
     */
    View resolveViewName(String viewName) throws Exception;
}
