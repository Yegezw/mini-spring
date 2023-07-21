package com.test;

public class BaseBaseService {

    private AService aService;

    public BaseBaseService() {
    }

    public void init() {
        System.out.println("Base Base Service init method.");
    }

    public void sayHello() {
        System.out.println("Base Base Service says hello");
    }

    public AService getAService() {
        return aService;
    }

    public void setAService(AService aService) {
        this.aService = aService;
    }
}
