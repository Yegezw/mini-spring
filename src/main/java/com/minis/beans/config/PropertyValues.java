package com.minis.beans.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Setter 参数
 */
public class PropertyValues {

    private final List<PropertyValue> propertyValueList;

    public PropertyValues() {
        propertyValueList = new ArrayList<>(0);
    }

    public PropertyValues(Map<String, Object> map) {
        propertyValueList = new ArrayList<>(10);

        for (Map.Entry<String, Object> e : map.entrySet()) {
            PropertyValue pv = new PropertyValue(e.getKey(), e.getValue());
            propertyValueList.add(pv);
        }
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
