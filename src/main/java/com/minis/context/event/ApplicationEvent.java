package com.minis.context.event;

import java.util.EventObject;

/**
 * 应用事件
 */
public abstract class ApplicationEvent extends EventObject {

    private static final long serialVersionUID = 1L;
    protected String message;

    public ApplicationEvent(Object source) {
        super(source);
        this.message = source.toString();
    }
}
