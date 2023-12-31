package com.minis.beans.factory.support;

import com.minis.beans.config.*;
import com.minis.beans.factory.BeanFactoryAware;
import com.minis.beans.factory.ConfigurableBeanFactory;
import com.minis.beans.factory.FactoryBean;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.registry.definition.BeanDefinitionRegistry;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 抽象 Bean 工厂(Bean 仓库 + BeanDefinition 仓库)
 */
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory, BeanDefinitionRegistry {

    protected List<String> beanDefinitionNames = new ArrayList<>();
    protected Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
    private final Map<String, Object> earlySingletonObjects = new HashMap<>(16); // 解决循环依赖

    public AbstractBeanFactory() {
    }

    public void refresh() {
        System.out.println("AbstractBeanFactory.refresh() 开始执行\n");
        for (String beanName : beanDefinitionNames) {
            try {
                // 注意: 若子类重写了 getBean(), 会进入子类的 getBean()
                // 比如 DefaultListableBeanFactory.getBean()
                getBean(beanName);
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }
        System.out.println("AbstractBeanFactory.refresh() 执行完毕");
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
        beanDefinitionNames.add(beanName);

        // 如果不是懒加载, 就调用 getBean()
        // if (!beanDefinition.lazyInit) {
        //     try {
        //         getBean(beanName);
        //     } catch (BeansException e) {
        //         e.printStackTrace();
        //     }
        // }
    }

    @Override
    public void removeBeanDefinition(String beanName) {
        beanDefinitionMap.remove(beanName);
        beanDefinitionNames.remove(beanName);
        super.removeSingleton(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String name) {
        return beanDefinitionMap.get(name);
    }

    @Override
    public boolean containsBeanDefinition(String name) {
        return beanDefinitionMap.containsKey(name);
    }

    /**
     * 注册 Bean, 依赖父类 SingletonBeanRegistry 的实现
     */
    public void registerBean(String beanName, Object obj) {
        super.registerSingleton(beanName, obj);
    }

    /**
     * 模版方法
     */
    @Override
    public Object getBean(String beanName) throws BeansException {
        System.out.println(String.format("AbstractBeanFactory.getBean(%s) called", beanName));

        Object singleton = super.getSingleton(beanName);

        if (singleton == null) {
            singleton = earlySingletonObjects.get(beanName); // 解决循环依赖

            if (singleton == null) {
                System.out.println("get bean null: " + beanName);

                BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
                if (beanDefinition == null) {
                    System.out.println(beanName + " 没有 BeanDefinition, 此方法将会返回 null");
                    return null;
                }

                singleton = createBean(beanDefinition);
                registerBean(beanName, singleton);

                // BeanFactoryAware
                if (singleton instanceof BeanFactoryAware) ((BeanFactoryAware) singleton).setBeanFactory(this);

                // BeanPostProcessor

                // step 1 : postProcessBeforeInitialization(BeanNameAutoProxyCreator 会返回代理对象 ProxyFactoryBean)
                singleton = applyBeanPostProcessorsBeforeInitialization(singleton, beanName); // 抽象方法

                // step 2 : afterPropertiesSet
                // step 3 : init-method
                if (beanDefinition.initMethodName != null && !beanDefinition.initMethodName.equals("")) {
                    invokeInitMethod(beanDefinition, singleton);
                }

                // step 4 : postProcessAfterInitialization
                applyBeanPostProcessorsAfterInitialization(singleton, beanName);  // 抽象方法
            }
        }

        // process Factory Bean
        if (singleton instanceof FactoryBean) {
            System.out.println(String.format("---------- FactoryBean: AbstractBeanFactory.getBean(%s)", beanName));
            // 这里的 singleton 是代理对象
            singleton = getObjectForBeanInstance(singleton, beanName);

            // step 1 中 BeanNameAutoProxyCreator 会返回代理对象 ProxyFactoryBean
            // 所以这里需要: 先删除原始对象, 然后添加代理对象
            super.removeSingleton(beanName);
            registerBean(beanName, singleton);

            return singleton;
        }

        return singleton;
    }

    @Override
    public boolean containsBean(String beanName) {
        return super.containsSingleton(beanName);
    }

    @Override
    public boolean isSingleton(String beanName) {
        return beanDefinitionMap.get(beanName).isSingleton();
    }

    @Override
    public boolean isPrototype(String beanName) {
        return beanDefinitionMap.get(beanName).isPrototype();
    }

    @Override
    public Class<?> getType(String beanName) {
        return this.beanDefinitionMap.get(beanName).getClass();
    }

    private Object createBean(BeanDefinition beanDefinition) {
        // 处理构造器参数
        Object obj = doCreateBean(beanDefinition);
        earlySingletonObjects.put(beanDefinition.id, obj); // 解决循环依赖

        Class<?> clazz = null;
        try {
            clazz = Class.forName(beanDefinition.className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // 处理 Setter 参数
        populateBean(beanDefinition, clazz, obj);

        return obj;
    }

    private Object doCreateBean(BeanDefinition beanDefinition) {
        Object obj = null;

        try {
            Class<?> clazz = Class.forName(beanDefinition.className);

            // 处理构造器参数
            ConstructorArgumentValues argumentValues = beanDefinition.constructorArgumentValues;
            if (argumentValues != null && !argumentValues.isEmpty()) {
                Class<?>[] paramTypes = new Class<?>[argumentValues.getArgumentCount()]; // 参数类型数组
                Object[] paramValues = new Object[argumentValues.getArgumentCount()];    // 参数值数组

                for (int i = 0; i < argumentValues.getArgumentCount(); i++) {
                    ConstructorArgumentValue argumentValue = argumentValues.getIndexedArgumentValue(i); // 参数
                    String type = argumentValue.type;
                    Object value = argumentValue.value;

                    if ("String".equals(type) || "java.lang.String".equals(type)) {
                        paramTypes[i] = String.class;
                        paramValues[i] = value;
                    } else if ("Integer".equals(type) || "java.lang.Integer".equals(type)) {
                        paramTypes[i] = Integer.class;
                        paramValues[i] = Integer.valueOf((String) value);
                    } else if ("int".equals(type)) {
                        paramTypes[i] = int.class;
                        paramValues[i] = Integer.valueOf((String) value);
                    } else {
                        // 默认为 String
                        paramTypes[i] = String.class;
                        paramValues[i] = value;
                    }
                }

                try {
                    Constructor<?> constructor = clazz.getConstructor(paramTypes); // 获取构造器
                    obj = constructor.newInstance(paramValues);
                } catch (NoSuchMethodException | SecurityException | IllegalArgumentException |
                         InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else obj = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(beanDefinition.id + " bean created");

        return obj;
    }

    /**
     * 填充 Bean
     */
    private void populateBean(BeanDefinition beanDefinition, Class<?> clazz, Object obj) {
        handleProperties(beanDefinition, clazz, obj);
    }

    /**
     * 处理属性
     */
    private void handleProperties(BeanDefinition beanDefinition, Class<?> clazz, Object obj) {
        // 处理 Setter 参数
        System.out.println("handle properties for bean: " + beanDefinition.id + "\n");

        PropertyValues propertyValues = beanDefinition.propertyValues;
        if (propertyValues != null && !propertyValues.isEmpty()) {
            for (int i = 0; i < propertyValues.size(); i++) {
                PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
                String type = propertyValue.type;
                String name = propertyValue.name;
                Object value = propertyValue.value;
                boolean isRef = propertyValue.isRef;

                Class<?>[] paramTypes = new Class<?>[1]; // 参数类型数组
                Object[] paramValues = new Object[1];    // 参数值数组
                if (!isRef) {
                    // no ref, generic property
                    if ("String".equals(type) || "java.lang.String".equals(type)) {
                        paramTypes[0] = String.class;
                        paramValues[0] = value;
                    } else if ("Integer".equals(type) || "java.lang.Integer".equals(type)) {
                        paramTypes[0] = Integer.class;
                        paramValues[0] = Integer.valueOf((String) value);
                    } else if ("int".equals(type)) {
                        paramTypes[0] = int.class;
                        paramValues[0] = Integer.valueOf((String) value);
                    } else {
                        // 默认为 String
                        paramTypes[0] = String.class;
                        paramValues[0] = value;
                    }
                } else {
                    // is ref, dependency property, create the dependent beans
                    try {
                        paramTypes[0] = Class.forName(type);      // 类型为 type 的 class
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        paramValues[0] = getBean((String) value); // 继续调用 getBean() 创建 BeanId(BeanName) = ref 的 Bean
                    } catch (BeansException e) {
                        e.printStackTrace();
                    }
                }

                // 构造 setter 方法的名字
                String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
                Method method = null;
                try {
                    method = clazz.getMethod(methodName, paramTypes); // 获取 setter 方法
                } catch (NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                }
                try {
                    method.invoke(obj, paramValues);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 调用初始化方法
     */
    private void invokeInitMethod(BeanDefinition beanDefinition, Object obj) {
        Class<?> clazz = obj.getClass();
        Method method = null;
        try {
            method = clazz.getMethod(beanDefinition.initMethodName);
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        try {
            method.invoke(obj);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    protected Object getObjectForBeanInstance(Object beanInstance, String beanName) {
        // 现在我们有了 bean 实例, 它可能是普通 bean 或 FactoryBean
        if (!(beanInstance instanceof FactoryBean)) return beanInstance;

        FactoryBean<?> factory = (FactoryBean<?>) beanInstance;
        return super.getObjectFromFactoryBean(factory, beanName);
    }

    /**
     * 应用 Bean 初始化之前
     */
    public abstract Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException;

    /**
     * 应用 Bean 初始化之后
     */
    public abstract Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException;
}
