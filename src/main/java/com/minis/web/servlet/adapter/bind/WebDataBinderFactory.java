package com.minis.web.servlet.adapter.bind;

import javax.servlet.http.HttpServletRequest;

/**
 * Web 数据绑定器工厂
 */
public class WebDataBinderFactory {

    public WebDataBinder createBinder(HttpServletRequest request, Object target, String objectName) {
        WebDataBinder wbd = new WebDataBinder(target, objectName);
        initBinder(wbd, request);
        return wbd;
    }

    protected void initBinder(WebDataBinder dataBinder, HttpServletRequest request) {
    }
}
