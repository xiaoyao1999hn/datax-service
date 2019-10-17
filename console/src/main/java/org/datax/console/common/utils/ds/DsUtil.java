package org.datax.console.common.utils.ds;
import org.datax.console.ds.vo.DataXDsVO;
import org.datax.console.plugin.DataXColumn;

import java.util.List;
/**
 * @author ChengJie
 * @desciption
 * @date 2019/5/7 11:48
 **/
public interface DsUtil {
    /**
     * 校验数据源链接是否正常
     * @param DataXDsVO
     * @throws Exception
     */
    void testConnect(DataXDsVO DataXDsVO) throws Exception;

    /**
     * 获取数据库表信息
     * @param ds
     * @return
     */
    List<String> getTables(DataXDsVO ds);

    /**
     * 获取数据源列信息
     * @param ds        数据源信息
     * @param tableName  如果是数据库则只传表名即可，如果是es需要传入2个值索引名和类型名
     * @return
     */
    List<DataXColumn> getColumns(DataXDsVO ds,String ...tableName);
}
