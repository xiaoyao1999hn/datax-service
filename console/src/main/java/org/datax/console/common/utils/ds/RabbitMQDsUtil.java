package org.datax.console.common.utils.ds;

import com.google.common.collect.ImmutableList;
import org.datax.console.ds.entity.config.RabbitMQDsConfig;
import org.datax.console.ds.vo.DataXDsVO;
import org.datax.console.plugin.DataXColumn;

import java.util.List;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/5/7 14:43
 **/
public class RabbitMQDsUtil implements DsUtil {
    @Override
    public void testConnect(DataXDsVO DataXDsVO) throws Exception {

    }

    @Override
    public List<String> getTables(DataXDsVO ds) {
        RabbitMQDsConfig dsConfig = ds.getConfig();
        return ImmutableList.of(dsConfig.getQueueName());
    }

    @Override
    public List<DataXColumn> getColumns(DataXDsVO ds, String... tableName) {
        return null;
    }

    private static class RabbitMQDsUtilHandler{
        private static final RabbitMQDsUtil rabbitMQDsUtil=new RabbitMQDsUtil();
    }

    public static RabbitMQDsUtil getInstance(){
        return RabbitMQDsUtilHandler.rabbitMQDsUtil;
    }
}
