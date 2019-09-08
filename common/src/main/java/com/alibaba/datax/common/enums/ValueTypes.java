package com.alibaba.datax.common.enums;

import java.util.Arrays;

/**
 * 数据值类型：0-String，1-Integer,2-Boolean,3-Long,4-Double,5-Float,6-Decimal,7-Date
 * @author ChengJie
 * @desciption
 * @date 2019/2/13 17:19
 **/
public enum ValueTypes {
    STRING(0, "String"),
    INTEGER(1, "Integer"),
    BOOLEAN(2, "Boolean"),
    LONG(3, "Long"),
    DOUBLE(4, "Double"),
    FLOAT(5, "Float"),
    DECIMAL(6, "Decimal"),
    DATE(7, "Date"),
    TIMESTAMP(8, "Timestamp"),
    NUMBER(9,"Number");
    Integer type;
    String description;

    ValueTypes(Integer type, String description) {
        this.type = type;
        this.description = description;
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

    public static String getName(int type) {
        for (ValueTypes c : ValueTypes.values()) {
            if (c.getType() == type) {
                return c.getDescription();
            }
        }
        return null;
    }

    public static ValueTypes getNameType(String typeName) {
        return Arrays.stream(values()).filter(t -> t.getDescription().equals(typeName)).findAny().orElseThrow(() -> new IllegalArgumentException("invalid typeName: " + typeName));
    }

    public static ValueTypes getValueType(Integer type){
        return Arrays.stream(values()).filter(t -> t.getType().equals(type)).findAny().orElseThrow(() -> new IllegalArgumentException("invalid type: " + type));
    }

}
