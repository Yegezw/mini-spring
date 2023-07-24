package com.minis.web.servlet.adapter.convert.http;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Http 消息转换器
 */
public interface HttpMessageConverter {

    void write(Object obj, HttpServletResponse response) throws IOException;
}
