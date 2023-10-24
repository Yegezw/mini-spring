package com.minis.web.servlet.adapter;

import com.minis.beans.config.BeansException;
import com.minis.context.ApplicationContext;
import com.minis.context.ApplicationContextAware;
import com.minis.web.servlet.adapter.bind.WebBindingInitializer;
import com.minis.web.servlet.adapter.bind.WebDataBinder;
import com.minis.web.servlet.adapter.bind.WebDataBinderFactory;
import com.minis.web.servlet.adapter.convert.http.HttpMessageConverter;
import com.minis.web.servlet.config.HandlerMethod;
import com.minis.web.servlet.config.ResponseBody;
import com.minis.web.servlet.resolver.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 请求映射(处理器适配器)
 */
public class RequestMappingHandlerAdapter implements HandlerAdapter, ApplicationContextAware {

    /**
     * 子 AnnotationConfigWeb 应用上下文, 由 DispatcherServlet 负责启动
     */
    private ApplicationContext applicationContext = null;
    private WebBindingInitializer webBindingInitializer = null; // 此处是扩展点
    private HttpMessageConverter messageConverter = null;

    public RequestMappingHandlerAdapter() {
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return handleInternal(request, response, (HandlerMethod) handler);
    }

    private ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) throws IOException {
        ModelAndView mv = null;
        try {
            mv = invokeHandlerMethod(request, response, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    protected ModelAndView invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
        WebDataBinderFactory binderFactory = new WebDataBinderFactory();
        Parameter[] methodParameters = handlerMethod.method.getParameters(); // 方法参数[]
        Object[] methodParamObjs = new Object[methodParameters.length];      // 方法参数值[]

        int i = 0;
        for (Parameter methodParameter : methodParameters) {
            Class<?> type = methodParameter.getType(); // 方法参数类型
            Object methodParamObj;                     // 方法参数值

            if (type == HttpServletRequest.class) methodParamObj = request;
            else if (type == HttpServletResponse.class) methodParamObj = response;
            else {
                // TODO 简单类型
                // 复杂类型
                methodParamObj = methodParameter.getType().newInstance();

                WebDataBinder wdb = binderFactory.createBinder(request, methodParamObj, methodParameter.getName());
                if (webBindingInitializer != null) webBindingInitializer.initBinder(wdb); // 此处是扩展点
                wdb.bind(request);
            }

            methodParamObjs[i++] = methodParamObj;
        }

        Method method = handlerMethod.method;
        Object result = method.invoke(handlerMethod.bean, methodParamObjs); // 调用处理器
        Class<?> returnType = method.getReturnType();

        ModelAndView mv = null;
        if (returnType == void.class) {} // 无返回值
        else if (method.isAnnotationPresent(ResponseBody.class) && messageConverter != null) {
            // @ResponseBody
            messageConverter.write(result, response);
        } else {
            // 无 @ResponseBody
            if (result instanceof ModelAndView) mv = (ModelAndView) result;
            else if (result instanceof String) {
                String viewName = (String) result;
                mv = new ModelAndView();
                mv.setViewName(viewName);
            }
        }
        // 如果方法的返回类型为 void, 将返回 null
        // 如果方法有 @ResponseBody, 将返回 null
        // 如果方法无 @ResponseBody, 而 result 既不是 ModelAndView, 也不是 String, 将返回 null
        return mv;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setWebBindingInitializer(WebBindingInitializer webBindingInitializer) {
        this.webBindingInitializer = webBindingInitializer; // 此处是扩展点
    }

    public void setMessageConverter(HttpMessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }
}
