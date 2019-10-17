package org.datax.console.base.enums;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Objects;

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
    TIMESTAMP(8, "Timestamp");
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

    /**
     *
     * 功能描述:
     *
     * 验证对象类型是否正确
     * @param o
     * @auther ClownfishYang
     * created on 2019-09-20 16:03:32
     */
    public void validator(Object o) {
        Objects.requireNonNull(o, "验证对象为空");
        switch (this){
            case INTEGER:
                Integer.parseInt(o.toString());
                break;
            case BOOLEAN:
                Objects.requireNonNull(BooleanUtils.toBooleanObject(o.toString()), "无法转换成Boolean 类型");
                break;
            case LONG:
            case TIMESTAMP:
                Long.parseLong(o.toString());
                break;
            case DOUBLE:
                Double.parseDouble(o.toString());
                break;
            case FLOAT:
                Float.parseFloat(o.toString());
                break;
            case DECIMAL:
                new BigDecimal(o.toString());
                break;
            case DATE:
                try {
                    DateUtils.parseDate(o.toString(), "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyyMMdd", "yyyyMMddHHmmss");
                } catch (ParseException e) {
                    throw new IllegalArgumentException("无法转换成Date 类型");
                }
                break;
            case STRING:
                o.toString();
                break;
        }
    }
    
    public static ValueTypes getNameType(String typeName) {
        return Arrays.stream(values()).filter(t -> t.getDescription().equals(typeName)).findAny().orElseThrow(() -> new IllegalArgumentException("invalid typeName: " + typeName));
    }

    public static ValueTypes getValueType(Integer type){
        return Arrays.stream(values()).filter(t -> t.getType().equals(type)).findAny().orElseThrow(() -> new IllegalArgumentException("invalid type: " + type));
    }

}
