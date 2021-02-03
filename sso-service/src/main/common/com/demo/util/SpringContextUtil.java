package com.demo.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        setApplicationContextValue(applicationContext);
    }

    public static Object getBean(String beanName) throws BeansException {
        return SpringContextUtil.applicationContext.getBean(beanName);
    }

    public static boolean containsBean(String beanName) {
        return SpringContextUtil.applicationContext.containsBean(beanName);
    }

    private static void setApplicationContextValue(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }
}
