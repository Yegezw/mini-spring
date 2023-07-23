package com.test.mvc;

import com.minis.web.config.RequestMapping;
import com.test.config.Student;
import com.test.config.User;

public class TestController {

    /**
     * <a href="http://localhost:8080/test?userId=1&userName=张三&birthday=2023-07-23&stuId=2&stuName=李四">测试连接</a>
     */
    @RequestMapping("/test")
    public String user(User user, Student student) {
        System.out.println(user);
        System.out.println(student);
        return user.toString() + "\n" + student.toString();
    }
}
