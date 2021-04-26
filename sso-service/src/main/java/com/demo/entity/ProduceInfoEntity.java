package com.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * (TblSinglePartition)实体类
 *
 * @author makejava
 * @since 2021-04-26 21:28:59
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProduceInfoEntity implements Serializable {
    private static final long serialVersionUID = -69450335316545994L;

    private Integer id;

    private String name;

    private Integer age;

    private String createTime;

}