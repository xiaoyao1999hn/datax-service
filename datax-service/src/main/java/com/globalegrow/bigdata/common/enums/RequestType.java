package com.globalegrow.bigdata.common.enums;

/**
 * 上报状态
 * Created by ChengJie on 2018/8/14 20:16
 */
public enum RequestType {

    FRIST_VISIT(0, "首次访问请求"),
    FORWARD_VISIT(1, "转发请求");


    Integer value;
    String description;

    RequestType(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    /**
     * {@link #value}的getter方法。
     */
    public Integer getValue() {
        return value;
    }

    /**
     * {@link #description}的getter方法。
     */
    public String getDescription() {
        return description;
    }

    public static String getName(int value) {
        for (RequestType c : RequestType.values()) {
            if (c.getValue() == value) {
                return c.getDescription();
            }
        }
        return null;
    }

}
