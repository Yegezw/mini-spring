package com.minis.web.servlet.adapter;

import com.minis.web.context.WebApplicationContext;
import com.minis.web.servlet.config.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 请求映射(处理器适配器)
 */
public class RequestMappingHandlerAdapter implements HandlerAdapter {

    private WebApplicationContext webApplicationContext;

    public RequestMappingHandlerAdapter(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        handleInternal(request, response, (HandlerMethod) handler);
    }

    private void handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws IOException {
        Object obj = handler.getBean();
        Method method = handler.getMethod();
        Object result = null;
        try {
            result = method.invoke(obj);
        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        if (result != null) response.getWriter().append(result.toString());
    }
}
