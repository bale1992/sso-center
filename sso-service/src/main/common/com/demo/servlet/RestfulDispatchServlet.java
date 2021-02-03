package com.demo.servlet;

import com.demo.resourcepool.ResourcePool;
import com.demo.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

@Slf4j
public class RestfulDispatchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.handler(req, resp, GetMapping.class.getName());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.handler(req, resp, PostMapping.class.getName());
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.handler(req, resp, PutMapping.class.getName());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.handler(req, resp, DeleteMapping.class.getName());
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }

    private void handler(HttpServletRequest req, HttpServletResponse resp, String className) throws ServletException, IOException {
        final String pathInfo = req.getPathInfo();
        if (Objects.isNull(pathInfo) || !ResourcePool.hasRestController(pathInfo)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Rest controller not exists.");
            return;
        }

        final Method restController = ResourcePool.getRestController(pathInfo, className);
        if (Objects.isNull(restController)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Rest controller not exists.");
            return;
        }

        final String controllerBeanName = ResourcePool.getRestControllerBeanName(pathInfo);
        if (Objects.isNull(controllerBeanName) || !SpringContextUtil.containsBean(controllerBeanName)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Rest controller bean not exists.");
            return;
        }

        final Object controllerBean = SpringContextUtil.getBean(controllerBeanName);
        final Object[] methodArgs = getMethodArgs(req, restController);
        try {
            restController.invoke(controllerBean, methodArgs);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("Invoke controller method exception:{}", e.getMessage());
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Invoke controller method exception.");
        }
    }

    private Object[] getMethodArgs(HttpServletRequest req, Method method) {
        // 方法参数类型
        final Class<?>[] parameterTypes = method.getParameterTypes();
        int parameterSize = parameterTypes.length;
        final Object[] args = new Object[parameterSize];

        // 方法参数值
        final Object[] objects = req.getParameterMap().values().toArray(new Object[0]);

        for (int i = 0; i < parameterSize; i++) {
            args[i] = ConvertUtils.convert(objects[i], parameterTypes[i]);
        }
        return args;
    }
}
