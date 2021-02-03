package com.demo.resourcepool;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class ResourcePool {

    /*
      key: RestController value
     */
    private static final Map<String, Map<String, Method>> CONTROLLER_MAP = new ConcurrentHashMap<>();

    private static final Map<String, String> BEAN_NAME_MAP = new ConcurrentHashMap<>();

    private ResourcePool() {
    }

    public static boolean isController(Object bean) {
        return bean.getClass().isAnnotationPresent(RestController.class);
    }

    public static void addRestController(Object bean) {
        String mapKey = bean.getClass().getAnnotation(RestController.class).value();
        Map<String, Method> mapValue = new ConcurrentHashMap<>();
        Arrays.stream(bean.getClass().getMethods()).forEach(method -> {
            if (method.isAnnotationPresent(GetMapping.class) || method.isAnnotationPresent(PostMapping.class)
                    || method.isAnnotationPresent(PutMapping.class) || method.isAnnotationPresent(DeleteMapping.class)) {
                mapValue.putIfAbsent(getMappingAnnotation(method), method);
            }
        });

        CONTROLLER_MAP.putIfAbsent(mapKey, mapValue);
    }

    public static boolean hasRestController(String restPath) {
        return Objects.nonNull(CONTROLLER_MAP.get(restPath));
    }

    public static Method getRestController(String restPath, String methodClass) {
        return CONTROLLER_MAP.get(restPath).get(methodClass);
    }

    public static void addRestControllerBeanName(Object bean, String beanName) {
        String mapKey = bean.getClass().getAnnotation(RestController.class).value();
        BEAN_NAME_MAP.putIfAbsent(mapKey, beanName);
    }

    public static String getRestControllerBeanName(String restPath) {
        return BEAN_NAME_MAP.get(restPath);
    }

    private static String getMappingAnnotation(Method method) {
        if (method.isAnnotationPresent(GetMapping.class)) {
            return GetMapping.class.getName();
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            return PostMapping.class.getName();
        } else if (method.isAnnotationPresent(PutMapping.class)) {
            return PutMapping.class.getName();
        } else {
            return DeleteMapping.class.getName();
        }
    }
}
