package org.datax.console.enums;

import com.globalegrow.bigdata.domain.bitmap.TagBitMapDO;
import com.globalegrow.bigdata.domain.ds.config.*;

import java.util.Arrays;

/**
 * @author DKF
 * @date 2018/12/27
 */
public enum DataSourceType {

    //小于50的均为常见数据源
    MYSQL(0, "mysql",OdsRdbmsDsConfigDO.class),
    ORACLE(1, "oracle",OdsRdbmsDsConfigDO.class),
    ES(2, "es",OdsEsDsConfigDO.class,"/es"),
    HIVE(3, "hive",OdsHiveDsConfigDO.class),
    PRESTO(4, "presto",OdsPrestoDsConfigDO.class),
    RABBITMQ(5,"rabbitmq",OdsRabbitMQDsConfigDO.class),
    KAFKA(6,"kafka",OdsKafkaDsConfigDO.class),
    HBASE(7,"hbase",OdsHbaseDsConfigDO.class),
    HDFS(8,"hdfs",OdsHdfsDsConfigDO.class),
    ;
    Integer type;
    String description;
    Class configClass;

    DataSourceType(Integer type, String description, Class configClass) {
        this.type = type;
        this.description = description;
        this.configClass=configClass;
    }

    /**
     * {@link #type}的getter方法。
     */
    public Integer getType() {
        return type;
    }

    /**
     * {@link #description}的getter方法。
     */
    public String getDescription() {
        return description;
    }

    public Class getConfigClass(){
        return configClass;
    }

    public static String getName(int type) {
        for (DataSourceType c : DataSourceType.values()) {
            if (c.getType().equals(type)) {
                return c.getDescription();
            }
        }
        return null;
    }


    public static DataSourceType fromType(Integer type) {
        return Arrays.stream(values()).filter(t -> t.getType().equals(type)).findAny().orElseThrow(() -> new IllegalArgumentException("invalid type: " + type));
    }


    public static DataSourceType fromTypeName(String typeName) {
        return Arrays.stream(values()).filter(t -> t.getDescription().equals(typeName)).findAny().orElseThrow(() -> new IllegalArgumentException("invalid typeName: " + typeName));
    }

}
