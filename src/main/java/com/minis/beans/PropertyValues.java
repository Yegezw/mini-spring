package com.minis.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Setter 参数
 */
public class PropertyValues {

    private final List<PropertyValue> propertyValueList;

    public PropertyValues() {
        propertyValueList = new ArrayList<>(0);
    }

    public List<PropertyValue> getPropertyValueList() {
        return propertyValueList;
    }

    public void addPropertyValue(PropertyValue pv) {
        propertyValueList.add(pv);
    }

    public void removePropertyValue(PropertyValue pv) {
        propertyValueList.remove(pv);
    }

    public void removePropertyValue(String propertyName) {
        propertyValueList.remove(getPropertyValue(propertyName));
    }

    public PropertyValue[] getPropertyValues() {
        return propertyValueList.toArray(new PropertyValue[0]);
    }

    public PropertyValue getPropertyValue(String propertyName) {
        for (PropertyValue pv : propertyValueList) {
            if (pv.name.equals(propertyName)) return pv;
        }
        return null;
    }

    public Object get(String propertyName) {
        PropertyValue pv = getPropertyValue(propertyName);
        return pv != null ? pv.value : null;
    }

    public boolean contains(String propertyName) {
        return getPropertyValue(propertyName) != null;
    }

    public int size() {
        return propertyValueList.size();
    }

    public boolean isEmpty() {
        return propertyValueList.isEmpty();
    }
}