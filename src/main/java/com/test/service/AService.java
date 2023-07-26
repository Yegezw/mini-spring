package com.test.service;

public class AService {

    private BService bService;

    public AService() {
    }

    public void init() {
        System.out.println("AService init method.");
    }

    public void sayHello() {
        System.out.println("AService says hello");
    }

    public BService getBService() {
        return bService;
    }

    public void setBService(BService bService) {
        this.bService = bService;
    }
}
