package com.test.mvc;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.web.config.RequestMapping;
import com.test.ioc.BaseService;

public class HelloWorldController {

    @Autowired
    private BaseService baseService;

    @RequestMapping("/hw")
    public String doTest() {
        baseService.sayHello();
        return "hello world!";
    }
}
