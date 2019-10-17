package org.datax.console.common.utils.ds;

import com.google.common.collect.ImmutableList;
import org.datax.console.ds.entity.config.KafkaDsConfig;
import org.datax.console.ds.vo.DataXDsVO;
import org.datax.console.plugin.DataXColumn;

import java.util.List;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/5/7 14:44
 **/
public class KafkaDsUtil implements DsUtil {
    @Override
    public void testConnect(DataXDsVO DataXDsVO) throws Exception {

    }

    @Override
    public List<String> getTables(DataXDsVO ds) {
            KafkaDsConfig dsConfig=ds.getConfig();
            return ImmutableList.of(dsConfig.getTopicName());
    }

    @Override
    public List<DataXColumn> getColumns(DataXDsVO ds, String... tableName) {
        return null;
    }

    private static class KafkaDsUtilHandler{
        private static final KafkaDsUtil kafkaDsUtil=new KafkaDsUtil();
    }

    public static KafkaDsUtil getInstance(){
        return KafkaDsUtilHandler.kafkaDsUtil;
    }

}
