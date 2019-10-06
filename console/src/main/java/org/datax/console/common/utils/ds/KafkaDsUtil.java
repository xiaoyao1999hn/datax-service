package org.datax.console.common.utils.ds;

import org.datax.console.ds.vo.DataXDsVO;

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
        return null;
    }

    private static class KafkaDsUtilHandler{
        private static final KafkaDsUtil kafkaDsUtil=new KafkaDsUtil();
    }

    public static KafkaDsUtil getInstance(){
        return KafkaDsUtilHandler.kafkaDsUtil;
    }

}
