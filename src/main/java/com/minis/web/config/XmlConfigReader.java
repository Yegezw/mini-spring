package com.minis.web.config;

import com.minis.core.Resource;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Map;

public class XmlConfigReader {

    public XmlConfigReader() {
    }

    public Map<String, MappingValue> loadConfig(Resource resource) {
        Map<String, MappingValue> mappings = new HashMap<>();

        while (resource.hasNext()) {
            Element element = (Element) resource.next();

            String id = element.attributeValue("id");
            String clazz = element.attributeValue("class");
            String method = element.attributeValue("value");

            mappings.put(id, new MappingValue(id, clazz, method));
        }

        return mappings;
    }
}
