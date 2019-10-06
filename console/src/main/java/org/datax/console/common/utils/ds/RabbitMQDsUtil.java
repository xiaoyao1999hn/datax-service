package org.datax.console.common.utils.ds;

import org.datax.console.ds.vo.DataXDsVO;
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
        return null;
    }

    private static class RabbitMQDsUtilHandler{
        private static final RabbitMQDsUtil rabbitMQDsUtil=new RabbitMQDsUtil();
    }

    public static RabbitMQDsUtil getInstance(){
        return RabbitMQDsUtilHandler.rabbitMQDsUtil;
    }
}
