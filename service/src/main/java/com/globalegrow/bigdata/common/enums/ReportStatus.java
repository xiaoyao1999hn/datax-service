package com.globalegrow.bigdata.common.enums;

/**
 * 上报状态
 * Created by ChengJie on 2018/8/14 20:16
 */
public enum ReportStatus {

    UNTREATED(0, "未处理"),
    SUCCESSED(1, "成功"),
    FAILED(2,"失败"),
    NACOS_UNREPORT(999,"NACOS未上报");


    Integer value;
    String description;

    ReportStatus(Integer value, String description) {
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
        for (ReportStatus c : ReportStatus.values()) {
            if (c.getValue() == value) {
                return c.getDescription();
            }
        }
        return null;
    }

}
