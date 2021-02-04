package com.demo.servlet;

import com.alibaba.fastjson.JSON;
import com.demo.resourcepool.ResourcePool;
import com.demo.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
public class RestfulDispatchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.handler(req, resp, GetMapping.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.handler(req, resp, PostMapping.class);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.handler(req, resp, PutMapping.class);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.handler(req, resp, DeleteMapping.class);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }

    private void handler(HttpServletRequest req, HttpServletResponse resp, Class<?> clazz) throws ServletException, IOException {
        final String pathInfo = req.getPathInfo();
        if (Objects.isNull(pathInfo) || !ResourcePool.hasRestController(pathInfo)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Rest controller not exists.");
            return;
        }

        final Method restController = ResourcePool.getRestController(pathInfo, clazz.getName());
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
        final Object[] methodArgs = getMethodArgs(req, restController, clazz);
        if (Objects.isNull(methodArgs)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Input param invalid.");
            return;
        }

        try {
            restController.invoke(controllerBean, methodArgs);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("Invoke controller method exception:{}", e.getMessage());
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Invoke controller method exception.");
        }
    }

    private Object[] getMethodArgs(HttpServletRequest req, Method method, Class<?> clazz) {
        // 获取方法入参
        final Parameter[] methodParameters = method.getParameters();
        final int parameterSize = methodParameters.length;
        final Object[] args = new Object[parameterSize];// 方法入参

        if (clazz == GetMapping.class || clazz == DeleteMapping.class) {
            final Object[] objects = req.getParameterMap().values().toArray(new Object[0]);
            if (parameterSize != objects.length) {
                return null;
            }

            for (int i = 0; i < parameterSize; i++) {
                Parameter parameter = methodParameters[i];
                if (parameter.isAnnotationPresent(RequestParam.class)) {
                    args[i] = ConvertUtils.convert(objects[i], parameter.getType());
                }
            }
        } else if (clazz == PostMapping.class || clazz == PutMapping.class) {
            StringBuilder responseStrBuilder = new StringBuilder();
            try (final InputStreamReader inputStreamReader = new InputStreamReader(req.getInputStream(), StandardCharsets.UTF_8);
                 final BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                String requestJsonStr;
                while ((requestJsonStr = bufferedReader.readLine()) != null) {
                    responseStrBuilder.append(requestJsonStr);
                }
            } catch (IOException ex) {
                log.error("Get request json exception:{}", ex.getMessage());
                return null;
            }

            for (int i = 0; i < parameterSize; i++) {
                Parameter parameter = methodParameters[i];
                if (parameter.isAnnotationPresent(RequestBody.class)) {
                    args[i] = JSON.parseObject(responseStrBuilder.toString(), parameter.getType());
                }
            }
        }

        return args;
    }
}
