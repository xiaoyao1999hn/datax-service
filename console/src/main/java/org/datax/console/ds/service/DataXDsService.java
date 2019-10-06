package org.datax.console.ds.service;
import org.datax.console.base.service.AbstractService;
import org.datax.console.common.utils.ds.DsUtil;
import org.datax.console.ds.vo.DataXDsVO;
import org.datax.console.plugin.DataXColumn;

import java.util.List;
import java.util.Map;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/5/7 10:14
 **/
public interface DataXDsService extends AbstractService<DataXDsVO> {
    /**
     * 下拉列表
     * @param param
     * @return
     */
    List<Map> optionList(Map param);

    /**
     * 根据名称获取数据源对象
     * @param name
     * @return
     */
    DataXDsVO queryByName(String name);

    /**
     * 获取数据源校验工具对象
     * @param type
     * @return
     */
    DsUtil chooseDsUtil(Integer type);

    /**
     * 获取数据源下的表信息
     * @param dsId
     * @return
     */
    List<String> listTable(Long dsId);

    /**
     *
     * 功能描述:
     * 获取数据字段信息
     * created on 2019-06-28 11:18:36
     * @param dataSourceId 数据源id
     * @param tableName 数据表名
     * @param typeName es type名
     * @param indexName es 索引名
     * @return 数据字段信息
     */
    List<DataXColumn> listColumn(Long dataSourceId, String tableName, String typeName, String indexName);


    /**
     *
     * 功能描述:
     * 获取数据字段名称
     * created on 2019-06-28 11:18:36
     * @param dataSourceId 数据源id
     * @param tableName 数据表名
     * @param typeName es type名
     * @param indexName es 索引名
     * @return 数据字段名称
     */
    List<String> listColumnName(Long dataSourceId, String tableName, String typeName, String indexName);
}
