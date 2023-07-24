package com.minis.web.servlet.resolver;

import com.minis.web.servlet.resolver.view.JstlView;
import com.minis.web.servlet.resolver.view.View;

/**
 * 内部资源视图解析器
 */
public class InternalResourceViewResolver implements ViewResolver {

    public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=UTF-8";
    private Class<?> viewClass = null;
    private String viewClassName = "";
    private String prefix = "";
    private String suffix = "";
    private String contentType = DEFAULT_CONTENT_TYPE;

    public InternalResourceViewResolver() {
        // 默认 viewClass = JstlView.class
        if (getViewClass() == null) setViewClass(JstlView.class);
    }

    /**
     * 解析视图名称, 根据 prefix + viewName + suffix 得到 url, 创建视图
     */
    @Override
    public View resolveViewName(String viewName) throws Exception {
        return buildView(viewName);
    }

    protected View buildView(String viewName) throws Exception {
        Class<?> viewClass = getViewClass();
        View view = (View) viewClass.newInstance();

        view.setUrl(getPrefix() + viewName + getSuffix());
        view.setContentType(getContentType());

        return view;
    }

    public void setViewClassName(String viewClassName) {
        this.viewClassName = viewClassName;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(viewClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        setViewClass(clazz);
    }

    protected String getViewClassName() {
        return this.viewClassName;
    }

    public void setViewClass(Class<?> viewClass) {
        this.viewClass = viewClass;
    }

    protected Class<?> getViewClass() {
        return this.viewClass;
    }

    public void setPrefix(String prefix) {
        this.prefix = (prefix != null ? prefix : "");
    }

    protected String getPrefix() {
        return this.prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = (suffix != null ? suffix : "");
    }

    protected String getSuffix() {
        return this.suffix;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    protected String getContentType() {
        return this.contentType;
    }
}
