package com.demo.dao;

import com.demo.entity.ProduceInfoEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (ProduceInfoEntity)表数据库访问层
 *
 * @author makejava
 * @since 2021-04-26 21:29:23
 */
public interface TblTwoPartitionDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    ProduceInfoEntity queryById(Integer id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<ProduceInfoEntity> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param ProduceInfoEntity 实例对象
     * @return 对象列表
     */
    List<ProduceInfoEntity> queryAll(ProduceInfoEntity ProduceInfoEntity);

    /**
     * 新增数据
     *
     * @param ProduceInfoEntity 实例对象
     * @return 影响行数
     */
    int insert(ProduceInfoEntity ProduceInfoEntity);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<ProduceInfoEntity> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<ProduceInfoEntity> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<ProduceInfoEntity> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<ProduceInfoEntity> entities);

    /**
     * 修改数据
     *
     * @param ProduceInfoEntity 实例对象
     * @return 影响行数
     */
    int update(ProduceInfoEntity ProduceInfoEntity);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Integer id);

}