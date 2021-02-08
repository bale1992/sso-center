package com.demo.controller;

import com.demo.annotation.RestPath;
import com.demo.entity.LoginEntity;
import com.demo.service.LoginService;
import com.demo.servlet.RestfulContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletException;

@Slf4j
@RestController
@RestPath("/ssoservice/login")
public class LoginController {

    @Resource
    private LoginService loginService;

    @PostMapping
    @RestPath("/login")
    public void login(RestfulContext context, @RequestBody LoginEntity entity) throws ServletException {
        loginService.login(context, entity);
    }

    @PostMapping
    @RestPath("/logout")
    public void logout(RestfulContext context, @RequestBody LoginEntity entity) {
        log.warn("logout");
    }

}
