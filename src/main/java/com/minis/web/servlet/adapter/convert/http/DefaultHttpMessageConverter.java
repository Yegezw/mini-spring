package com.minis.web.servlet.adapter.convert.http;

import com.minis.web.servlet.adapter.util.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 默认 Http 消息转换器
 */
public class DefaultHttpMessageConverter implements HttpMessageConverter {

    private String defaultContentType = "text/json;charset=UTF-8";
    private String defaultCharacterEncoding = "UTF-8";
    private ObjectMapper objectMapper;

    public DefaultHttpMessageConverter() {
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void write(Object obj, HttpServletResponse response) throws IOException {
        response.setContentType(defaultContentType);
        response.setCharacterEncoding(defaultCharacterEncoding);

        writeInternal(obj, response);

        response.flushBuffer();
    }

    private void writeInternal(Object obj, HttpServletResponse response) throws IOException {
        String jsonStr = objectMapper.writeValuesAsString(obj);
        PrintWriter pw = response.getWriter();
        pw.write(jsonStr);
    }
}
