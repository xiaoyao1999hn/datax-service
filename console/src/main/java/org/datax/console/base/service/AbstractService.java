package org.datax.console.base.service;

import java.util.List;
import java.util.Map;

/**
 * 抽象服务类接口
 * @author ChengJie
 * @desciption
 * @date 2018/12/26 20:47
 **/
public interface AbstractService<T> {

    /**
     * 新增记录
     * @param t
     * @return
     */
    Integer save(T t) throws Exception;

    /**
     * 修改记录
     * @param t
     * @return
     */
    Integer modify(T t) throws Exception;

    /**
     * 删除记录
     * @param id
     * @return
     */
    Integer remove(Long id);

    /**
     * 根据条件查询列表
     * @param condition
     * @return
     */
    List<T> list(Map condition);

    /**
     * 根据条件查询数量
     * @param condition
     * @return
     */
    Long count(Map condition);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    T get(Long id);

}
