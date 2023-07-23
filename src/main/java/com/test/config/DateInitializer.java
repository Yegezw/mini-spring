package com.test.config;

import com.minis.web.servlet.adapter.bind.WebBindingInitializer;
import com.minis.web.servlet.adapter.bind.WebDataBinder;

import java.util.Date;

/**
 * 日期初始化器
 */
public class DateInitializer implements WebBindingInitializer {

    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(Date.class, "yyyy-MM-dd", false));
    }
}
