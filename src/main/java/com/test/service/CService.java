package com.test.service;

import com.minis.beans.factory.config.annotation.Autowired;

public class CService {

    @Autowired
    private AService aService;

    public CService() {
    }

    public void init() {
        System.out.println("CService init method.");
    }

    public void sayHello() {
        System.out.println("CService says hello");
        aService.sayHello();
    }

    public AService getAService() {
        return aService;
    }

    public void setAService(AService aService) {
        this.aService = aService;
    }
}
