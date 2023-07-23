package com.minis.web.servlet.adapter.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;

public class WebUtils {

    /**
     * 获取以 prefix 为前缀的参数
     */
    public static Map<String, Object> getParametersStartingWith(HttpServletRequest request, String prefix) {
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, Object> params = new TreeMap<>(); // 参数名 -> 参数值
        if (prefix == null) prefix = "";

        while (paramNames != null && paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();

            if (prefix.isEmpty() || paramName.startsWith(prefix)) {
                String noPrefixed = paramName.substring(prefix.length());
                String value = request.getParameter(paramName);
                params.put(noPrefixed, value);
            }
        }

        return params;
    }
}
