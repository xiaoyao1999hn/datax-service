package org.datax.console.enums;

/**
 * 授权模式：0-一级模式，1-组长模式，2-自定义模式
 * Created by ChengJie on 2018/8/14 20:16
 */
public enum EffectStatus {

    UNEFFECTED(0, "未生效"),
    EFFECTED(1, "已生效");
    Integer type;
    String description;

    EffectStatus(Integer type, String description) {
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
        for (EffectStatus c : EffectStatus.values()) {
            if (c.getType() == type) {
                return c.getDescription();
            }
        }
        return null;
    }

}
