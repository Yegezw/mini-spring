package com.test.service;

public class BServiceImpl implements BService {

    private String name;
    private int level;
    private String property1;
    private String property2;
    private CService cService;

    public BServiceImpl() {
    }

    public BServiceImpl(String name, int level) {
        this.name = name;
        this.level = level;
    }

    @Override
    public void sayHello() {
        System.out.println(property1 + ", " + property2);
    }

    public String getProperty1() {
        return property1;
    }

    public void setProperty1(String property1) {
        this.property1 = property1;
    }

    public String getProperty2() {
        return property2;
    }

    public void setProperty2(String property2) {
        this.property2 = property2;
    }

    public CService getCService() {
        return cService;
    }

    public void setCService(CService cService) {
        this.cService = cService;
    }
}
