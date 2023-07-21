package com.minis.beans.factory.xml;

import com.minis.beans.PropertyValue;
import com.minis.beans.PropertyValues;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.ConstructorArgumentValue;
import com.minis.beans.factory.config.ConstructorArgumentValues;
import com.minis.beans.factory.support.AbstractBeanFactory;
import com.minis.core.Resource;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * XML Bean 定义解析器, 遍历 Resource 向 AbstractBeanFactory 注册 BeanDefinition
 */
public class XmlBeanDefinitionReader {

    AbstractBeanFactory beanFactory;

    public XmlBeanDefinitionReader(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void loadBeanDefinitions(Resource resource) {
        while (resource.hasNext()) {
            Element element = (Element) resource.next();

            String beanId = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            BeanDefinition beanDefinition = new BeanDefinition(beanId, beanClassName);

            // 构造器参数
            ConstructorArgumentValues avs = new ConstructorArgumentValues();
            for (Element e : element.elements("constructor-arg")) {
                String type = e.attributeValue("type");
                String name = e.attributeValue("name");
                String value = e.attributeValue("value");

                avs.addArgumentValue(new ConstructorArgumentValue(type, name, value));
            }
            beanDefinition.constructorArgumentValues = avs;

            // Setter 参数
            PropertyValues pvs = new PropertyValues();
            List<String> refs = new ArrayList<>();
            for (Element e : element.elements("property")) {
                String type = e.attributeValue("type");
                String name = e.attributeValue("name");
                String value = e.attributeValue("value");
                String ref = e.attributeValue("ref");

                String resValue = ""; // resValue = value or ref ?
                boolean isRef = false;
                if (value != null && !value.equals("")) resValue = value;
                else if (ref != null && !ref.equals("")) {
                    isRef = true;
                    resValue = ref;
                    refs.add(ref);
                }

                pvs.addPropertyValue(new PropertyValue(type, name, resValue, isRef));
            }
            beanDefinition.propertyValues = pvs;
            beanDefinition.dependsOn = refs.toArray(new String[0]);

            beanFactory.registerBeanDefinition(beanId, beanDefinition);
        }
    }
}
