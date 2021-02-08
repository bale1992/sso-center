package com.demo.resourcepool;

import com.demo.annotation.RestPath;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Holder;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class ResourcePool {

    /*
      key: RestController value
     */
    private static final Map<String, Map<ResourceMethodItem, Method>> CONTROLLER_MAP = new ConcurrentHashMap<>();

    private static final Map<String, String> BEAN_NAME_MAP = new ConcurrentHashMap<>();

    private ResourcePool() {
    }

    public static boolean isController(Object bean) {
        return bean.getClass().isAnnotationPresent(RestController.class);
    }

    public static void addRestController(Object bean) {
        final String mapKey = bean.getClass().getAnnotation(RestPath.class).value();
        final Map<ResourceMethodItem, Method> mapValue = Arrays.stream(bean.getClass().getMethods())
                .filter(ResourcePool::isRestControllerMethod)
                .collect(Collectors.toMap(ResourcePool::buildMethodItem, Function.identity()));
        CONTROLLER_MAP.putIfAbsent(mapKey, mapValue);
    }

    public static boolean hasRestController(String restPath) {
        // 如果找不到controller, 按照去掉最后一个斜杠后面的内容查找
        if (Objects.isNull(CONTROLLER_MAP.get(restPath))) {
            final Holder<String> holder = new Holder<>();
            final String newPathInfo = getRestPath(restPath, holder);
            return Objects.nonNull(CONTROLLER_MAP.get(newPathInfo));
        }
        return true;
    }

    public static Method getRestController(String restPath, String methodClass) {
        String newPathInfo = restPath;
        String methodPathInfo = "";
        if (Objects.isNull(CONTROLLER_MAP.get(restPath))) {
            final Holder<String> holder = new Holder<>();
            newPathInfo = getRestPath(restPath, holder);
            methodPathInfo = holder.value;
        }

        ResourceMethodItem methodItem = ResourceMethodItem.builder()
                .methodClassName(methodClass)
                .methodRestPath(methodPathInfo)
                .build();
        return CONTROLLER_MAP.get(newPathInfo).get(methodItem);
    }

    public static void addRestControllerBeanName(Object bean, String beanName) {
        String mapKey = bean.getClass().getAnnotation(RestPath.class).value();
        BEAN_NAME_MAP.putIfAbsent(mapKey, beanName);
    }

    public static String getRestControllerBeanName(String restPath) {
        String newPathInfo = restPath;
        if (Objects.isNull(BEAN_NAME_MAP.get(restPath))) {
            final Holder<String> holder = new Holder<>();
            newPathInfo = getRestPath(restPath, holder);
        }
        return BEAN_NAME_MAP.get(newPathInfo);
    }

    private static String getRestPath(String restPath, Holder<String> holder) {
        final String[] splits = restPath.split("/");
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < splits.length - 1; i++) {
            if (!Objects.equals(splits[i], "")) {
                sb.append("/").append(splits[i]);
            }
        }

        holder.value = "/" + splits[splits.length - 1];
        return sb.toString();
    }

    private static boolean isRestControllerMethod(Method method) {
        return method.isAnnotationPresent(PostMapping.class)
                || method.isAnnotationPresent(PutMapping.class)
                || method.isAnnotationPresent(GetMapping.class)
                || method.isAnnotationPresent(DeleteMapping.class);
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

    private static ResourceMethodItem buildMethodItem(Method method) {
        return ResourceMethodItem.builder()
                .methodRestPath(method.getAnnotation(RestPath.class).value())
                .methodClassName(ResourcePool.getMappingAnnotation(method))
                .build();
    }
}
