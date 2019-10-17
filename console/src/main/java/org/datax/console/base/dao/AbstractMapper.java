package org.datax.console.base.dao;

import java.util.List;
import java.util.Map;

/**
 * 抽象mapper接口模板
 * @author ChengJie
 * @desciption  主要是为了减少通用代码的编写
 * @date 2018/12/26 20:41
 **/
public interface AbstractMapper<T> {

    /**
     * 新增
     * @param t
     * @return
     */
    Integer insert(T t);

    /**
     * 根据条件新增，有时只需要处理某些特定的条件新增（例如：insert into select）
     * @param condition
     * @return
     */
    Integer insertByCondition(Map condition);

    /**
     * 更新
     * @param t
     * @return
     */
    Integer update(T t);

    /**
     * 根据条件更新，有时只需要处理某些特定的条件更新（例如：insert into select）
     * @param condition
     * @return
     */
    Integer updateByCondition(Map condition);

    /**
     * 根据id删除
     * @param id
     * @return
     */
    Integer deleteById(Long id);

    /**
     * 多条件删除
     * @param condition
     * @return
     */
    Integer deleteByCondition(Map condition);

    /**
     * 根据条件查询列表
     * @param condition
     * @return
     */
    List<T> queryByCondition(Map condition);

    /**
     * 查询列表数量
     * @param condition
     * @return
     */
    Long count(Map condition);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    T queryById(Long id);

}
