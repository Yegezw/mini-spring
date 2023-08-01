package com.test.aop;

public class ActionImpl implements IAction {

    @Override
    public void doAction() {
        System.out.println("really do action");
    }
}