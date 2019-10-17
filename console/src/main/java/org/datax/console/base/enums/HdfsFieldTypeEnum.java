package org.datax.console.base.enums;

import java.sql.JDBCType;
import java.util.Arrays;
import java.util.Objects;

/**
 * User: jiangsongsong
 * Date: 2019/2/26
 * Time: 14:41
 */
public enum HdfsFieldTypeEnum {
    /**
     * hdfs field type
     */
    STRING, LONG, BOOLEAN, DOUBLE, DATE;


    public static HdfsFieldTypeEnum fromType(String fieldType) {
        return Arrays.stream(values()).filter(t -> t.name().equalsIgnoreCase(fieldType)).findAny()
            .orElseThrow(() -> new IllegalArgumentException("Invalid hdfs field type: " + fieldType));
    }


    public static HdfsFieldTypeEnum convertJDBCTypeToHdfsFieldType(JDBCType jdbcType) {
        Objects.requireNonNull(jdbcType, "jdbcType");
        switch (jdbcType) {
            case BOOLEAN:
            case BIT:
                return HdfsFieldTypeEnum.BOOLEAN;
            case CHAR:
            case NCHAR:
            case VARCHAR:
            case LONGVARCHAR:
            case NVARCHAR:
            case LONGNVARCHAR:
            case CLOB:
            case NCLOB:
                return HdfsFieldTypeEnum.STRING;
            case TINYINT:
            case SMALLINT:
            case INTEGER:
            case BIGINT:
                return HdfsFieldTypeEnum.LONG;
            case FLOAT:
            case REAL:
            case DOUBLE:
            case NUMERIC:
            case DECIMAL:
                return HdfsFieldTypeEnum.DOUBLE;
            case TIME:
            case DATE:
            case TIMESTAMP:
            case TIME_WITH_TIMEZONE:
            case TIMESTAMP_WITH_TIMEZONE:
                return HdfsFieldTypeEnum.DATE;
            default:
                return HdfsFieldTypeEnum.STRING;
        }
    }

}
