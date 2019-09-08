package com.globalegrow.datax.plugin.writer.rabbitmqwriter;

import com.alibaba.datax.common.spi.ErrorCode;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/4/24 19:32
 **/
public enum RabbitmqWriterErrorCode implements ErrorCode {
    BAD_CONFIG_VALUE("RabbitmqWriter-00", "您配置的值不合法."),
    EXECUTE_ERROR("RabbitmqWriter-01", "执行时失败."),
    CONNECT_MQ_FAIL("RabbitmqWriter-02","连接Rabbitmq失败")
    ;

    private final String code;
    private final String description;

    RabbitmqWriterErrorCode(String code, String description){
        this.code = code;
        this.description = description;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
