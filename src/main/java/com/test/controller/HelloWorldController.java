package com.test.controller;

import com.minis.beans.factory.config.annotation.Autowired;
import com.minis.web.config.RequestMapping;
import com.minis.web.servlet.config.ResponseBody;
import com.test.service.CService;

public class HelloWorldController {

    @Autowired
    private CService cService;

    /**
     * <a href="http://localhost:8080/hw">测试连接</a>
     */
    @ResponseBody
    @RequestMapping("/hw")
    public String doTest() {
        cService.sayHello();
        return "hello world!";
    }
}
