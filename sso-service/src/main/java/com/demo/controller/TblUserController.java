package com.demo.controller;

import com.demo.entity.TblUser;
import com.demo.service.TblUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * (TblUser)表控制层
 *
 * @author makejava
 * @since 2021-02-01 10:33:53
 */
@Slf4j
@RestController("/ssoservice/user")
public class TblUserController {
    /**
     * 服务对象
     */
    @Resource
    private TblUserService tblUserService;

    /**
     * 新增数据
     *
     * @param tblUser 实例对象
     */
    @PostMapping
    public void addUser(@RequestBody TblUser tblUser) {
        tblUserService.insert(tblUser);
    }

    /**
     * 修改数据
     *
     * @param tblUser 实例对象
     */
    @PutMapping
    public void modifyUserPasswd(@RequestBody TblUser tblUser) {
        tblUserService.update(tblUser);
    }

    /**
     * 通过主键删除数据
     *
     * @param userName 主键
     */
    @DeleteMapping
    public void deleteUserByName(@RequestParam String userName) {
        tblUserService.deleteUser(userName);
    }
}
