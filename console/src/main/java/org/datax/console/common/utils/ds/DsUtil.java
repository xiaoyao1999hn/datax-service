package org.datax.console.common.utils.ds;
import org.datax.console.ds.vo.DataXDsVO;

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
}
