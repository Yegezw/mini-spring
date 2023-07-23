package com.minis.web.servlet.adapter;

import com.minis.beans.BeansException;
import com.minis.web.context.WebApplicationContext;
import com.minis.web.servlet.adapter.bind.WebBindingInitializer;
import com.minis.web.servlet.adapter.bind.WebDataBinder;
import com.minis.web.servlet.adapter.bind.WebDataBinderFactory;
import com.minis.web.servlet.config.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 请求映射(处理器适配器)
 */
public class RequestMappingHandlerAdapter implements HandlerAdapter {

    private WebApplicationContext webApplicationContext;
    private WebBindingInitializer webBindingInitializer = null; // 此处是扩展点

    public RequestMappingHandlerAdapter(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
        try {
            Object webBindingInitializer = webApplicationContext.getBean("webBindingInitializer"); // 此处是扩展点
            this.webBindingInitializer = (WebBindingInitializer) webBindingInitializer;
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        handleInternal(request, response, (HandlerMethod) handler);
    }

    private void handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws IOException {
        try {
            invokeHandlerMethod(request, response, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
        WebDataBinderFactory binderFactory = new WebDataBinderFactory();
        Parameter[] methodParameters = handlerMethod.getMethod().getParameters(); // 方法参数[]
        Object[] methodParamObjs = new Object[methodParameters.length];           // 方法参数值[]

        int i = 0;
        for (Parameter methodParameter : methodParameters) {
            // TODO 简单类型
            // 复杂类型
            Object methodParamObj = methodParameter.getType().newInstance(); // 方法参数值

            WebDataBinder wdb = binderFactory.createBinder(request, methodParamObj, methodParameter.getName());
            if (webBindingInitializer != null) webBindingInitializer.initBinder(wdb); // 此处是扩展点
            wdb.bind(request);

            methodParamObjs[i++] = methodParamObj;
        }

        Method method = handlerMethod.getMethod();
        Object result = method.invoke(handlerMethod.getBean(), methodParamObjs);
        response.setContentType("text/html;charset=UTF-8"); // 解决响应乱码
        response.getWriter().append(result.toString());
    }
}
