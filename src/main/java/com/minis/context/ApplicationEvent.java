package com.minis.context;

import java.util.EventObject;

/**
 * 应用事件
 */
public class ApplicationEvent extends EventObject {

    private static final long serialVersionUID = 1L;
    protected String message = null;

    public ApplicationEvent(Object source) {
        super(source);
        this.message = source.toString();
    }
}
