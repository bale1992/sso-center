package com.demo.resourcepool;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class ResourcePool {

    /*
      key: RestController value
     */
    private static final Map<String, Map<Annotation, Method>> CONTROLLER_MAP = new ConcurrentHashMap<>();

    private ResourcePool() {
    }

    public static boolean isController(Object bean) {
        return bean.getClass().isAnnotationPresent(RestController.class);
    }

    public static void addRestController(Object bean) {
        String mapKey = bean.getClass().getAnnotation(RestController.class).value();
        Map<Annotation, Method> mapValue = new ConcurrentHashMap<>();
        Arrays.stream(bean.getClass().getMethods()).forEach(method -> {
            if (method.isAnnotationPresent(GetMapping.class) || method.isAnnotationPresent(PostMapping.class)
                    || method.isAnnotationPresent(PutMapping.class) || method.isAnnotationPresent(DeleteMapping.class)) {
                mapValue.putIfAbsent(getMappingAnnotation(method), method);
            }
        });

        CONTROLLER_MAP.putIfAbsent(mapKey, mapValue);
    }

    public static boolean hasRestController(String restPath) {
        return Objects.isNull(CONTROLLER_MAP.get(restPath));
    }

    public static Method getRestController(String restPath, Annotation annotation) {
        return CONTROLLER_MAP.get(restPath).get(annotation);
    }

    private static Annotation getMappingAnnotation(Method method) {
        if (method.isAnnotationPresent(GetMapping.class)) {
            return method.getAnnotation(GetMapping.class);
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            return method.getAnnotation(PostMapping.class);
        } else if (method.isAnnotationPresent(PutMapping.class)) {
            return method.getAnnotation(PutMapping.class);
        } else {
            return method.getAnnotation(DeleteMapping.class);
        }
    }
}
