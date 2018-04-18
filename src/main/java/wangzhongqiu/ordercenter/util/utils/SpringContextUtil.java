package wangzhongqiu.ordercenter.util.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContextUtil implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private static SpringContextUtil springContextUtil;

    private SpringContextUtil() {
    }

    public static SpringContextUtil getInstance() {
        if(springContextUtil == null) {
            springContextUtil = new SpringContextUtil();
        }

        return springContextUtil;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    public Object getBean(String name) throws BeansException {
        return this.applicationContext.getBean(name);
    }

    public Object getBean(String name, Class requiredType) throws BeansException {
        return this.applicationContext.getBean(name, requiredType);
    }

    public boolean containsBean(String name) {
        return this.applicationContext.containsBean(name);
    }

    public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return this.applicationContext.isSingleton(name);
    }

    public Class getType(String name) throws NoSuchBeanDefinitionException {
        return this.applicationContext.getType(name);
    }

    public String[] getAliases(String name) throws NoSuchBeanDefinitionException {
        return this.applicationContext.getAliases(name);
    }
}
