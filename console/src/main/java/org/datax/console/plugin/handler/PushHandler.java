package org.datax.console.plugin.handler;

import com.globalegrow.bigdata.enums.DataSourceType;
import com.globalegrow.bigdata.ods.admin.ds.dao.OdsDsDao;
import com.globalegrow.bigdata.vo.push.config.OdsPushTaskVO;
import lombok.Setter;

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

    protected OdsDsDao dsDao;

    public void autowireParam(Integer type,OdsPushTaskVO config){
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
    abstract void autowireReaderParam(OdsPushTaskVO config);

    /**
     * 注入writer参数
     * @param config
     */
    abstract void autowireWriterParam(OdsPushTaskVO config);


    /**
     * @param dsType
     * @return
     */
    public static PushHandler createHandler(Integer dsType){

        switch (DataSourceType.fromType(dsType)){
            case MYSQL: return new MysqlPushHandler();
            case ORACLE: return new TagBitMapPushHandler();
            case HIVE: return new HivePushHandler();
//            case PRESTO: return new TagBitMapPushHandler();
            case ES: return new EsPushHandler();
            case RABBITMQ: return new RabbitMqPushHandler();
            case KAFKA: return new KafkaPushHandler();
//            case HBASE: return new TagBitMapPushHandler();
            case HDFS: return new HdfsPushHandler();
            case TAG_BITMAP: return new TagBitMapPushHandler();
        }
        return null;
    }
}
