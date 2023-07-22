package com.minis.web.servlet.adapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 处理器适配器
 */
public interface HandlerAdapter {

    void handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;
}
