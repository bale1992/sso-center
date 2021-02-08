package com.demo.service;

import com.demo.dao.UserDao;
import com.demo.entity.UserEntity;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * (TblUser)表服务实现类
 *
 * @author makejava
 * @since 2021-02-01 10:33:51
 */
@Service("tblUserService")
public class UserService {

    @Resource
    private UserDao tblUserDao;

    /**
     * 新增数据
     *
     * @param tblUser 实例对象
     */
    public void insert(UserEntity tblUser) {
        this.tblUserDao.insert(tblUser);
    }

    /**
     * 修改数据
     *
     * @param tblUser 实例对象
     */
    public void update(UserEntity tblUser) {
        this.tblUserDao.update(tblUser);
    }

    /**
     * 通过主键删除数据
     *
     * @param userName 主键
     */
    public void deleteUser(String userName) {

    }
}
