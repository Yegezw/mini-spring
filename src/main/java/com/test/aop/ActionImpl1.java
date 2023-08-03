package com.test.aop;

public class ActionImpl1 implements IAction {

    @Override
    public void doAction() {
        System.out.println("ActionImpl1 really do action");
    }

    @Override
    public void doSomething() {
        System.out.println("ActionImpl1 really do something");
    }

    @Override
    public void test() {
        System.out.println("ActionImpl1 really do test");
    }
}
