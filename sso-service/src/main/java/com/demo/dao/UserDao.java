package com.demo.dao;

import com.demo.entity.UserEntity;
import org.apache.ibatis.annotations.Param;

public interface UserDao {

    int insert(UserEntity tblUser);

    void update(UserEntity tblUser);

    UserEntity queryUserByUserName(@Param("userName") String userName);
}
