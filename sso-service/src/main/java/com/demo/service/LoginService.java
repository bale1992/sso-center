package com.demo.service;

import com.demo.dao.UserDao;
import com.demo.entity.LoginEntity;
import com.demo.entity.UserEntity;
import com.demo.servlet.RestfulContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Service
public class LoginService {

    @Resource
    private UserDao userDao;

    public void login(RestfulContext context, LoginEntity entity) throws ServletException {
        final UserEntity userEntity = userDao.queryUserByUserName(entity.getUserName());
        if (Objects.isNull(userEntity) || !Objects.equals(entity.getPassWord(), userEntity.getPassWord())) {
            throw new ServletException("Invalid user or password");
        }

        // admin用户第一次登录需要修改初始密码
        if ("admin".equals(userEntity.getUserName()) && userEntity.isAdminFirstLogin()) {
            // 更新数据库
            UserEntity updateEntity = UserEntity.builder()
                    .userName(userEntity.getUserName())
                    .passWord(userEntity.getPassWord())
                    .isLogin(userEntity.isLogin())
                    .isAdminFirstLogin(false)
                    .build();
            userDao.update(updateEntity);

            // 重定向到修改密码页面
            context.setHttpStatus(HttpServletResponse.SC_FOUND);
            context.setResponseInfo("/ssowebsite/dist/ModifyPasswdPage.html");
        }

        // 重定向到总的页面入口
    }
}
