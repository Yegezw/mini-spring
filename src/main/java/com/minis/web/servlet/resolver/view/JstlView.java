package com.minis.web.servlet.resolver.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * JSTL 视图
 */
public class JstlView implements View {

    public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=UTF-8";
    private String beanName;
    private String contentType = DEFAULT_CONTENT_TYPE;
    private String requestContextAttribute;
    private String url;

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        for (Map.Entry<String, ?> e : model.entrySet()) {
            // 将 model 中的数据放入 request 域中
            request.setAttribute(e.getKey(), e.getValue());
        }

        // 请求转发
        request.getRequestDispatcher(getUrl()).forward(request, response);
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return this.beanName;
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public void setRequestContextAttribute(String requestContextAttribute) {
        this.requestContextAttribute = requestContextAttribute;
    }

    @Override
    public String getRequestContextAttribute() {
        return this.requestContextAttribute;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getUrl() {
        return this.url;
    }
}
