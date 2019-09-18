package org.datax.console.plugin;

import com.alibaba.fastjson.JSONObject;
import com.globalegrow.bigdata.enums.DataSourceType;
import com.globalegrow.bigdata.exception.GlobalegrowExpcetion;
import com.globalegrow.bigdata.ods.admin.push.datax.plugin.reader.HdfsReader;
import com.globalegrow.bigdata.ods.admin.push.datax.plugin.reader.HiveReader;
import com.globalegrow.bigdata.ods.admin.push.datax.plugin.reader.MysqlReader;
import com.globalegrow.bigdata.ods.admin.push.datax.plugin.reader.TagBitmapReader;
import com.globalegrow.bigdata.ods.admin.push.datax.plugin.writer.EsWriter;
import com.globalegrow.bigdata.ods.admin.push.datax.plugin.writer.RabbitMqWriter;
import com.globalegrow.bigdata.vo.push.config.OdsPushTaskVO;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/8/20 16:55
 **/
@Getter
@Setter
public abstract class DataXPlugin {
    /**
     * 插件名称
     */
    private String name;

    /**
     * 参数
     */
    private JSONObject parameter =new JSONObject();

    public DataXPlugin(OdsPushTaskVO pushConfig){
        checkedConf(pushConfig);
        initParams(pushConfig);
        createConf(this);
    }


    /**
     * 构建插件配置
     * @return
     */
    public  JSONObject build(){
        JSONObject plugin=new JSONObject();
        plugin.put(Key.PLUGIN_NAME,name);
        plugin.put(Key.PLUGIN_PARAMETER,parameter);
        return plugin;
    }

    /**
     * 初始化
     * @param pushConfig
     */
    protected abstract void initParams(OdsPushTaskVO pushConfig);


    public JSONObject addConf(String key, Object value){
        parameter.put(key,value);
        return parameter;
    }

    protected JSONObject createConf(Object obj){

        if(obj==null){
            return parameter;
        }

        Class objClass =obj.getClass();
        Field[]  fields=objClass.getDeclaredFields();
        Arrays.stream(fields).forEach(x->{
            try {
                x.setAccessible(true);
                parameter.put(x.getName(),x.get(obj));
            } catch (IllegalAccessException e) {

            }
        });
        return  parameter;
    }

    /**
     * 校验配置信息
     * @param pushConfig
     * @throws GlobalegrowExpcetion
     */
    protected abstract void checkedConf(OdsPushTaskVO pushConfig) throws GlobalegrowExpcetion;


    public static DataXPlugin createReader(OdsPushTaskVO pushTask){
        switch (DataSourceType.fromType(pushTask.getReaderType())){
            case MYSQL: return new MysqlReader(pushTask);
//            case ORACLE: return new OracleRead();
            case HIVE: return new HiveReader(pushTask);
//            case PRESTO: return new PrestoReader();
//            case ES: return new EsReader();
//            case RABBITMQ: return new RabbitMqReader();
//            case KAFKA: return new KafkaReader();
//            case HBASE: return new HBaseReader();
            case HDFS: return new HdfsReader(pushTask);
            case TAG_BITMAP: return new TagBitmapReader(pushTask);
        }
        return null;
    }

    public static DataXPlugin createWriter(OdsPushTaskVO pushTask){
        switch (DataSourceType.fromType(pushTask.getWriterType())){
//            case MYSQL: return new MysqlWriter();
//            case ORACLE: return new OracleWriter();
//            case HIVE: return new HiveWriter();
            case ES: return new EsWriter(pushTask);
            case RABBITMQ: return new RabbitMqWriter(pushTask);
//            case KAFKA: return new KafkaWriter(pushTask);
//            case HBASE: return new HBaseWriter();
//            case HDFS: return new HdfsWriter();
//            case TAG_BITMAP: return new TagBitmapWriter();
        }
        return null;
    }

    public static class Key {
        public final static String READER_DS_NAME = "readerDs";
        public final static String WRITER_DS_NAME = "writerDs";
        public final static String PLUGIN_NAME = "name";
        public final static String PLUGIN_PARAMETER = "parameter";
    }
}
