package org.datax.console.enums;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import java.util.Arrays;
import java.util.Objects;

/**
 * User: jiangsongsong
 * Date: 2019/3/2
 * Time: 10:34
 */
public enum DbTypeEnum {


    /**
     * db types
     */
    MYSQL("mysql", "com.mysql.jdbc.Driver") {
        @Override
        protected String doGenJdbcUrl(String ip, int port, String database) {
            return String.format("jdbc:mysql://%s:%d/%s", ip, port, database);
        }
    },

    ORACLE("oracle", "oracle.jdbc.driver.OracleDriver") {
        @Override
        protected String doGenJdbcUrl(String ip, int port, String database) {
            return String.format("jdbc:oracle:thin:@%s:%d:%s", ip, port, database);
        }
    },

    HIVE2("hive2", "org.apache.hive.jdbc.HiveDriver") {
        @Override
        protected String doGenJdbcUrl(String ip, int port, String database) {
            return String.format("jdbc:hive2://%s:%d/%s", ip, port, database);
        }
    };

    private final String typeName;
    private final String driverClassName;

    DbTypeEnum(String typeName, String driverClassName) {
        this.typeName = typeName;
        this.driverClassName = driverClassName;
    }

    public static DbTypeEnum fromTypeName(String typeName) {
        return Arrays.stream(values()).filter(t -> t.typeName.equalsIgnoreCase(typeName)).findAny()
            .orElseThrow(() -> new IllegalArgumentException("Invalid db type: " + typeName));
    }

    /**
     * MYSQL(0, "mysql"), ORACLE(1, "oracle"), ES(2, "es"), HIVE(3, "hive")
     *
     * @param type datasource type {@link DataSourceType}
     * @return DbTypeEnum
     */
    public static DbTypeEnum fromDataSourceType(Integer type) {
        Objects.requireNonNull(type, "datasource type");

        if (DataSourceType.MYSQL.getType().equals(type)) {
            return MYSQL;
        } else if (DataSourceType.ORACLE.getType().equals(type)) {
            return ORACLE;
        } else if (DataSourceType.HIVE.getType().equals(type)) {
            return HIVE2;
        }else if (DataSourceType.HDFS.getType().equals(type)) {
            return HIVE2;
        } else {
            throw new IllegalArgumentException("Invalid datasource type: " + type);
        }
    }


    public String genJdbcUrl(String host, int port, String database) {
        Validate.isTrue(StringUtils.isNotBlank(host), "ip[%s] require not null", host);
        Validate.isTrue(port > 0 && port <= 65535, "invalid port[%d], should 0 < port <= 65535", port);
        Validate.isTrue(StringUtils.isNotBlank(database), "database[%s] require not null", database);
        return doGenJdbcUrl(host, port, database);
    }

    protected abstract String doGenJdbcUrl(String ip, int port, String database);

    public String getTypeName() {
        return typeName;
    }

    public String getDriverClassName() {
        return driverClassName;
    }
}
