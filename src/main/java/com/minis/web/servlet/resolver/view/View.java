package com.minis.web.servlet.resolver.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 视图
 */
public interface View {

    /**
     * 渲染
     */
    void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception;

    void setContentType(String contentType);

    default String getContentType() {
        return null;
    }

    void setUrl(String url);

    String getUrl();

    void setRequestContextAttribute(String requestContextAttribute);

    String getRequestContextAttribute();
}
