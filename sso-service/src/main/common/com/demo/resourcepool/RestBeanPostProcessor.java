package com.demo.resourcepool;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RestBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        log.warn("Current init bean, beanName:{}, beanClassName:{}", beanName, bean.getClass().getName());
        if (ResourcePool.isController(bean)) {
            ResourcePool.addRestController(bean);
            ResourcePool.addRestControllerBeanName(bean, beanName);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
