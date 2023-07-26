package com.minis.beans.config;

import java.util.ArrayList;
import java.util.List;

/**
 * 构造器参数
 */
public class ConstructorArgumentValues {

    private final List<ConstructorArgumentValue> argumentValueList = new ArrayList<>(); // 索引参数值

    public ConstructorArgumentValues() {
    }

    public void addArgumentValue(ConstructorArgumentValue argumentValue) {
        argumentValueList.add(argumentValue);
    }

    public ConstructorArgumentValue getIndexedArgumentValue(int index) {
        return argumentValueList.get(index);
    }

    public int getArgumentCount() {
        return argumentValueList.size();
    }

    public boolean isEmpty() {
        return argumentValueList.isEmpty();
    }
}