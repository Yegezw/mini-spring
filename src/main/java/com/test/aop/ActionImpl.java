package com.test.aop;

public class ActionImpl implements IAction {

    @Override
    public void doAction() {
        System.out.println("really do action");
    }

    @Override
    public void doSomething() {
        System.out.println("really do something");
    }

    @Override
    public void test() {
        System.out.println("really do test");
    }
}