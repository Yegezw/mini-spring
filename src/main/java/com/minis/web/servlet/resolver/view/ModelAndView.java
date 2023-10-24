package com.minis.web.servlet.resolver.view;

import java.util.HashMap;
import java.util.Map;

/**
 * 模型和视图
 */
public class ModelAndView {

	/**
	 * 视图(视图名称 / 视图)
	 */
    private Object view;
	/**
	 * 数据
	 */
    private Map<String, Object> model = new HashMap<>();

    public ModelAndView() {
    }

    public ModelAndView(String viewName) {
        this.view = viewName;
    }

    public ModelAndView(View view) {
        this.view = view;
    }

    public ModelAndView(String viewName, Map<String, ?> modelData) {
        this.view = viewName;
        if (modelData != null) addAllAttributes(modelData);
    }

    public ModelAndView(View view, Map<String, ?> model) {
        this.view = view;
        if (model != null) addAllAttributes(model);
    }

    public ModelAndView(String viewName, String modelName, Object modelObject) {
        this.view = viewName;
        addObject(modelName, modelObject);
    }

    public ModelAndView(View view, String modelName, Object modelObject) {
        this.view = view;
        addObject(modelName, modelObject);
    }

    public void setViewName(String viewName) {
        this.view = viewName;
    }

    public String getViewName() {
        return (view instanceof String ? (String) view : null);
    }

    public void setView(View view) {
        this.view = view;
    }

    public View getView() {
        return (view instanceof View ? (View) view : null);
    }

    public boolean hasView() {
        return (view != null);
    }

    /**
     * 是参考
     */
    public boolean isReference() {
        return (view instanceof String);
    }

    public Map<String, Object> getModel() {
        return model;
    }

    private void addAllAttributes(Map<String, ?> modelData) {
        if (modelData != null) model.putAll(modelData);
    }

    public void addAttribute(String attributeName, Object attributeValue) {
        model.put(attributeName, attributeValue);
    }

    public ModelAndView addObject(String attributeName, Object attributeValue) {
        addAttribute(attributeName, attributeValue);
        return this;
    }
}
