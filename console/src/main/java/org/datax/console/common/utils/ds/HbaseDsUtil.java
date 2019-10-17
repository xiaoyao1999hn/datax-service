package org.datax.console.common.utils.ds;

import org.datax.console.ds.vo.DataXDsVO;
import org.datax.console.plugin.DataXColumn;

import java.util.List;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/5/7 14:45
 **/
public class HbaseDsUtil implements DsUtil {
    @Override
    public void testConnect(DataXDsVO DataXDsVO) throws Exception {

    }

    @Override
    public List<String> getTables(DataXDsVO ds) {
        return null;
    }

    @Override
    public List<DataXColumn> getColumns(DataXDsVO ds, String... tableName) {
        return null;
    }

}
