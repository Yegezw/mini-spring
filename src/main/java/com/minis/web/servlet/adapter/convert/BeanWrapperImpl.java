package com.minis.web.servlet.adapter.convert;


import com.minis.beans.PropertyValue;
import com.minis.web.servlet.adapter.convert.editor.PropertyEditor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Bean 包装器实现
 */
public class BeanWrapperImpl extends AbstractPropertyAccessor {

    /**
     * 方法参数对象
     */
    private Object wrappedObject;
    /**
     * 方法参数类型 Class
     */
    private Class<?> clazz;

    public BeanWrapperImpl(Object object) {
        super();
        this.wrappedObject = object;
        this.clazz = object.getClass();
    }

    /**
     * 绑定具体某个参数
     */
    public void setPropertyValue(PropertyValue pv) {
        // 参数必须具备 getter、setter 方法
        BeanPropertyHandler propertyHandler = new BeanPropertyHandler(pv.name);

        PropertyEditor pe = super.getCustomEditor(propertyHandler.propertyClazz);
        if (pe == null) pe = super.getDefaultEditor(propertyHandler.propertyClazz);

        if (pe != null) {
            // 存在属性编辑器, 就用属性编辑器转换后, 再赋值
            pe.setAsText((String) pv.value);
            propertyHandler.setValue(pe.getValue());
        } else propertyHandler.setValue(pv.value);
    }

    /**
     * <p>Bean 属性处理器
     * <p>一个内部类, 用于处理参数, 通过 getter() 和 setter() 操作属性
     */
    private class BeanPropertyHandler {

        /**
         * setter()
         */
        Method writeMethod = null;
        /**
         * getter()
         */
        Method readMethod = null;
        /**
         * 属性类型
         */
        Class<?> propertyClazz = null;

        public BeanPropertyHandler(String propertyName) {
            try {
                Field field = clazz.getDeclaredField(propertyName);
                propertyClazz = field.getType();

                String writeMethodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
                String readMethodName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
                writeMethod = clazz.getDeclaredMethod(writeMethodName, propertyClazz);
                readMethod = clazz.getDeclaredMethod(readMethodName);
            } catch (NoSuchMethodException | SecurityException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        /**
         * 调用 getter 获取属性值
         */
        public Object getValue() {
            Object result = null;

            readMethod.setAccessible(true);
            try {
                result = readMethod.invoke(wrappedObject);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }

            return result;
        }

        /**
         * 调用 setter 设置属性值
         */
        public void setValue(Object value) {
            writeMethod.setAccessible(true);
            try {
                writeMethod.invoke(wrappedObject, value);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}