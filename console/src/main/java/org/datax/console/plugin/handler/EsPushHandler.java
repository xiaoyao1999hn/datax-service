package org.datax.console.plugin.handler;

import com.globalegrow.bigdata.ods.admin.push.datax.plugin.DataXPlugin;
import com.globalegrow.bigdata.vo.ds.OdsDsVO;
import com.globalegrow.bigdata.vo.push.config.OdsPushTaskVO;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/9/8 15:11
 **/
public class EsPushHandler extends PushHandler {

    @Override
    public void autowireReaderParam(OdsPushTaskVO config) {

    }

    @Override
    void autowireWriterParam(OdsPushTaskVO config) {
        OdsDsVO toDs=dsDao.queryById(config.getToSourceId());
        Map<String,OdsDsVO> dsInfo =config.getDsInfo();
        if(dsInfo==null){
            dsInfo=new HashMap<>();
        }
        dsInfo.put(DataXPlugin.Key.WRITER_DS_NAME,toDs);
    }
}
