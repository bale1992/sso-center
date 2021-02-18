package com.demo.service;

import com.demo.dao.UserDao;
import com.demo.entity.UserEntity;
import com.demo.servlet.RestfulContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

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
    public void modifyUserPasswd(RestfulContext context, UserEntity tblUser) {
        final UserEntity userEntity = this.tblUserDao.queryUserByUserName(tblUser.getUserName());
        if (Objects.isNull(userEntity) || !Objects.equals(userEntity.getPassWord(), tblUser.getPassWord())) {
            context.setHttpStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            context.setResponseInfo("Invalid user or password");
            return;
        }

        UserEntity updateEntity = UserEntity.builder()
                .userName(tblUser.getUserName())
                .passWord(tblUser.getNewPassWord())
                .role(userEntity.getRole())
                .isLogin(userEntity.isLogin())
                .isAdminFirstLogin(userEntity.isAdminFirstLogin())
                .build();
        this.tblUserDao.update(updateEntity);
    }

    /**
     * 通过主键删除数据
     *
     * @param userName 主键
     */
    public void deleteUser(String userName) {

    }
}
