package org.datax.console.plugin.reader;

import com.globalegrow.bigdata.exception.GlobalegrowExpcetion;
import com.globalegrow.bigdata.ods.admin.push.datax.plugin.DataXPlugin;
import com.globalegrow.bigdata.vo.push.config.OdsPushTaskVO;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/8/20 16:45
 **/
public class HiveReader extends DataXPlugin {

    public HiveReader(OdsPushTaskVO pushConfig) {
        super(pushConfig);
    }

    @Override
    protected void initParams(OdsPushTaskVO pushConfig) {

    }

    @Override
    protected void checkedConf(OdsPushTaskVO pushConfig) throws GlobalegrowExpcetion {

    }
}
