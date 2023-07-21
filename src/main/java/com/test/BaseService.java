package com.test;

import com.minis.beans.factory.annotation.Autowired;

public class BaseService {

    @Autowired
    private BaseBaseService baseBaseService;

    public BaseService() {
    }

    public void init() {
        System.out.print("Base Service init method.");
    }

    public void sayHello() {
        System.out.print("Base Service says hello");
        baseBaseService.sayHello();
    }

    public BaseBaseService getBaseBaseService() {
        return baseBaseService;
    }

    public void setBaseBaseService(BaseBaseService baseBaseService) {
        this.baseBaseService = baseBaseService;
    }
}
