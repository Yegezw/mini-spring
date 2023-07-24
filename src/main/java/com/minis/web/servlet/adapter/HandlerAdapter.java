package com.minis.web.servlet.adapter;

import com.minis.web.servlet.resolver.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 处理器适配器
 */
public interface HandlerAdapter {

    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;
}
