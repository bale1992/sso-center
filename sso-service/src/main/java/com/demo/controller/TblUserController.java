package com.demo.controller;

import com.demo.entity.TblUser;
import com.demo.service.TblUserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * (TblUser)表控制层
 *
 * @author makejava
 * @since 2021-02-01 10:33:53
 */
@RestController
@RequestMapping("/rest/user")
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
     * @return 实例对象
     */
    @PostMapping("/addUser")
    public void addUser(TblUser tblUser) {
        tblUserService.insert(tblUser);
    }

    /**
     * 修改数据
     *
     * @param tblUser 实例对象
     * @return 实例对象
     */
    @PutMapping("/modifyPasswd")
    public void modifyUserPasswd(TblUser tblUser) {
        tblUserService.update(tblUser);
    }

    /**
     * 通过主键删除数据
     *
     * @param userName 主键
     * @return 是否成功
     */
    @DeleteMapping("/deleteUser")
    public void deleteUserByName(String userName) {
        tblUserService.deleteUser(userName);
    }
}
