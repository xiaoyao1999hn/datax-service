package org.datax.console.plugin.reader;

import org.datax.console.common.exceptions.GlobalegrowExpcetion;
import org.datax.console.plugin.DataXPlugin;
import org.datax.console.push.vo.DataXPushTaskVO;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/8/20 16:45
 **/
public class HiveReader extends DataXPlugin {

    public HiveReader(DataXPushTaskVO pushConfig) {
        super(pushConfig);
    }

    @Override
    protected void initParams(DataXPushTaskVO pushConfig) {

    }

    @Override
    protected void checkedConf(DataXPushTaskVO pushConfig) throws GlobalegrowExpcetion {

    }
}
