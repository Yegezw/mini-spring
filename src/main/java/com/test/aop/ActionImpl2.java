package com.test.aop;

public class ActionImpl2 implements IAction {

    @Override
    public void doAction() {
        System.out.println("ActionImpl2 really do action");
    }

    @Override
    public void doSomething() {
        System.out.println("ActionImpl2 really do something");
    }

    @Override
    public void test() {
        System.out.println("ActionImpl2 really do test");
    }
}
