/**
 * 
 * 
 */
package wangzhongqiu.ordercenter.util;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Spring工具类,可以操作Spring容器.用于手动创建对象时从Spring容器获取对象
 * 
 * 
 */
@Service
public class OrderSpringService implements ApplicationContextAware{
    private static OrderSpringService contextUtils;
    
    private ApplicationContext context;

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        if (this.context == null) {
            this.context = applicationContext;
        }
        OrderSpringService.contextUtils = this;
    }
    
    public static Object getBean(String name) throws BeansException {
        return getContext().getBean(name);
    }

    public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return getContext().getBean(name, requiredType);
    }

    public static <T> T getBean(Class<T> requiredType) throws BeansException {
        return getContext().getBean(requiredType);
    }

    public static Object getBean(String name, Object... args) throws BeansException {
        return getContext().getBean(name, args);
    }
    
    public static DataSource getDataSource() {
        return getBean(DataSource.class);
    }
    
    public static ApplicationContext getContext() {
        if (contextUtils.context == null) {
            throw new RuntimeException("没有获取到ApplicationContext,请在Spring 配置文件中装配 SpringService Bean对象");
        }
        return contextUtils.context;
    }
}
