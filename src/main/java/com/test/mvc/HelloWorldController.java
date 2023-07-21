package com.test.mvc;

import com.minis.web.config.RequestMapping;

public class HelloWorldController {

    @RequestMapping("/test")
    public String doTest() {
        return "hello world!";
    }
}
