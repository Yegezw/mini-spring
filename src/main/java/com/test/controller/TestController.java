package com.test.controller;

import com.minis.web.config.RequestMapping;
import com.minis.web.servlet.config.ResponseBody;
import com.minis.web.servlet.resolver.view.ModelAndView;
import com.test.pojo.Student;
import com.test.pojo.User;

public class TestController {

    /**
     * <a href="http://localhost:8080/test1?userId=1&userName=张三&birthday=2023-07-23&stuId=2&stuName=李四">测试连接</a>
     */
    @ResponseBody
    @RequestMapping("/test1")
    public String user(User user, Student student) {
        System.out.println(user);
        System.out.println(student);
        return user.toString() + "\n" + student.toString();
    }

    /**
     * <a href="http://localhost:8080/test2?userId=1&userName=张三&birthday=2023-07-23">测试连接</a>
     */
    @ResponseBody
    @RequestMapping("/test2")
    public User test2(User user) {
        System.out.println(user);
        return user;
    }

    /**
     * <a href="http://localhost:8080/test3">测试连接</a>
     */
    @RequestMapping("/test3")
    public ModelAndView test3() {
        return new ModelAndView("test3");
    }
}