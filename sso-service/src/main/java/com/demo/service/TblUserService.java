package com.demo.service;

import com.demo.entity.TblUser;

import java.util.List;

/**
 * (TblUser)表服务接口
 *
 * @author makejava
 * @since 2021-02-01 10:33:50
 */
public interface TblUserService {

    /**
     * 新增数据
     *
     * @param tblUser 实例对象
     * @return 实例对象
     */
    void insert(TblUser tblUser);

    /**
     * 修改数据
     *
     * @param tblUser 实例对象
     * @return 实例对象
     */
    void update(TblUser tblUser);

    /**
     * 通过主键删除数据
     *
     * @param userName 主键
     * @return 是否成功
     */
    void deleteUser(String userName);

}
