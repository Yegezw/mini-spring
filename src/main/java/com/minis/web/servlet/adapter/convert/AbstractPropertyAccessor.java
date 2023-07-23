package com.minis.web.servlet.adapter.convert;

import com.minis.beans.PropertyValue;
import com.minis.beans.PropertyValues;
import com.minis.web.servlet.adapter.convert.editor.PropertyEditorRegistrySupport;

/**
 * 抽象属性访问器
 */
public abstract class AbstractPropertyAccessor extends PropertyEditorRegistrySupport {

    /**
     * 参数值
     */
    protected PropertyValues pvs;

    public AbstractPropertyAccessor() {
        super();
    }

    /**
     * 绑定参数值, 模版方法
     */
    public void setPropertyValues(PropertyValues pvs) {
        this.pvs = pvs;
        for (PropertyValue pv : pvs.getPropertyValues()) setPropertyValue(pv);
    }

    /**
     * 绑定参数值
     */
    public abstract void setPropertyValue(PropertyValue pv);
}
