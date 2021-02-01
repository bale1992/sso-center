package com.demo.service.impl;

import com.demo.dao.TblUserDao;
import com.demo.entity.TblUser;
import com.demo.service.TblUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (TblUser)表服务实现类
 *
 * @author makejava
 * @since 2021-02-01 10:33:51
 */
@Service("tblUserService")
public class TblUserServiceImpl implements TblUserService {
    @Resource
    private TblUserDao tblUserDao;

    /**
     * 新增数据
     *
     * @param tblUser 实例对象
     * @return 实例对象
     */
    @Override
    public void insert(TblUser tblUser) {
        this.tblUserDao.insert(tblUser);
    }

    /**
     * 修改数据
     *
     * @param tblUser 实例对象
     * @return 实例对象
     */
    @Override
    public void update(TblUser tblUser) {
        this.tblUserDao.update(tblUser);
    }

    /**
     * 通过主键删除数据
     *
     * @param userName 主键
     * @return 是否成功
     */
    @Override
    public void deleteUser(String userName) {

    }
}
