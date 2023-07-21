package com.minis.web.config;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XmlScanComponentHelper {

    private XmlScanComponentHelper() {
    }
    
    public static List<String> getNodeValue(URL xmlPath) {
        List<String> packages = new ArrayList<>();

        SAXReader saxReader = new SAXReader();
        Iterator<Element> it = null;
        try {
            Document document = saxReader.read(xmlPath);
            Element root = document.getRootElement();
            it = root.elementIterator();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        if (it == null) throw new RuntimeException(xmlPath + "解析失败");
        while (it.hasNext()) {
            Element element = it.next();
            packages.add(element.attributeValue("base-package"));
        }

        return packages;
    }
}
