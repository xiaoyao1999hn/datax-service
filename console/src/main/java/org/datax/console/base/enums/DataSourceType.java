package org.datax.console.base.enums;


import org.datax.console.ds.entity.config.*;

import java.util.Arrays;

/**
 * @author DKF
 * @date 2018/12/27
 */
public enum DataSourceType {

    //小于50的均为常见数据源
    MYSQL(0, "mysql",RdbmsDsConfig.class),
    ORACLE(1, "oracle",RdbmsDsConfig.class),
    ES(2, "es",EsDsConfig.class),
    HIVE(3, "hive",HiveDsConfig.class),
    PRESTO(4, "presto",PrestoDsConfig.class),
    RABBITMQ(5,"rabbitmq",RabbitMQDsConfig.class),
    KAFKA(6,"kafka",KafkaDsConfig.class),
    HBASE(7,"hbase",HbaseDsConfig.class),
    HDFS(8,"hdfs",HdfsDsConfig.class);
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
