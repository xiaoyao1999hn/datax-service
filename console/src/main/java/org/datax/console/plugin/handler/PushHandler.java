package org.datax.console.plugin.handler;
import lombok.Setter;
import org.datax.console.ds.dao.DataXDsDao;
import org.datax.console.ds.vo.DataXDsVO;
import org.datax.console.plugin.DataXPlugin;
import org.datax.console.push.vo.DataXPushTaskVO;
import org.datax.console.base.enums.DataSourceType;

import java.util.HashMap;
import java.util.Map;

/**
 * 推数参数转换类
 * @author ChengJie
 * @desciption
 * @date 2019/9/8 15:03
 **/
@Setter
public abstract class PushHandler {

    public static final Integer READER_TYPE=1;

    public static final Integer WRITER_TYPE=2;

    protected DataXDsDao dsDao;

    public void autowireParam(Integer type,DataXPushTaskVO config){
        DataXDsVO toDs=dsDao.queryById(config.getToSourceId());
        Map<String,DataXDsVO> dsInfo =config.getDsInfo();
        if(dsInfo==null){
            dsInfo=new HashMap<>(2);
        }
        dsInfo.put(DataXPlugin.Key.WRITER_DS_NAME,toDs);
        DataXDsVO fromDs=dsDao.queryById(config.getFromSourceId());
        dsInfo.put(DataXPlugin.Key.READER_DS_NAME,fromDs);

        if(READER_TYPE.equals(type)){
            autowireReaderParam(config);
        }else{
            autowireWriterParam(config);
        }
    }

    /**
     * 注入reader参数
     * @param config
     */
    abstract void autowireReaderParam(DataXPushTaskVO config);

    /**
     * 注入writer参数
     * @param config
     */
    abstract void autowireWriterParam(DataXPushTaskVO config);


    /**
     * @param dsType
     * @return
     */
    public static PushHandler createHandler(Integer dsType){

        switch (DataSourceType.fromType(dsType)){
            case MYSQL: return new MysqlPushHandler();
            case ORACLE: return new MysqlPushHandler();
            case HIVE: return new HivePushHandler();
//            case PRESTO: return new TagBitMapPushHandler();
            case ES: return new EsPushHandler();
            case RABBITMQ: return new RabbitMqPushHandler();
            case KAFKA: return new KafkaPushHandler();
//            case HBASE: return new TagBitMapPushHandler();
            case HDFS: return new HdfsPushHandler();
        }
        return null;
    }
}
