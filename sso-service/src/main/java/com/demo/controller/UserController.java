package com.demo.controller;

import com.demo.annotation.RestPath;
import com.demo.entity.UserEntity;
import com.demo.service.UserService;
import com.demo.servlet.RestfulContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RestPath("/ssoservice/user")
public class UserController {

    @Resource
    private UserService tblUserService;

    @PostMapping
    @RestPath
    public void addUser(RestfulContext context, @RequestBody UserEntity tblUser) {
        tblUserService.insert(tblUser);
    }

    @PutMapping
    @RestPath
    public void modifyUserPasswd(RestfulContext context, @RequestBody UserEntity tblUser) {
        tblUserService.update(tblUser);
    }

    @DeleteMapping
    @RestPath
    public void deleteUserByName(RestfulContext context, @RequestParam String userName) {
        tblUserService.deleteUser(userName);
    }
}
