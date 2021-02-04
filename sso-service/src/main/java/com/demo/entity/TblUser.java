package com.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * (TblUser)实体类
 *
 * @author makejava
 * @since 2021-02-01 10:33:43
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TblUser implements Serializable {
    private static final long serialVersionUID = 228590193511697547L;

    private Long id;

    private String userName;

    private String passWord;

    private Object role;

    private Object isLogin;

    private Object isAdminFirstLogin;

}
