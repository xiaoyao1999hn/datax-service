package org.datax.console.ds.dao;

import org.datax.console.base.dao.AbstractMapper;
import org.datax.console.ds.vo.DataXDsVO;

import java.util.List;
import java.util.Map;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/10/16 10:55
 **/
public interface DataXDsDao extends AbstractMapper<DataXDsVO> {

    /**
     * 根据参数查询
     * @param params
     * @return
     */
    List<DataXDsVO> selectByParams(Map params);

    /**
     * 下拉列表
     * @param param
     * @return
     */
    List<Map> optionList(Map param);

    /**
     * 根据名字查询数据源信息
     * @param dsName
     * @return
     */
    DataXDsVO queryByName(String dsName);

}
