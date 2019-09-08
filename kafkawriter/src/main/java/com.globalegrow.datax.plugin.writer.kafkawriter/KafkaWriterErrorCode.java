package com.globalegrow.datax.plugin.writer.kafkawriter;

import com.alibaba.datax.common.spi.ErrorCode;

/**
 * @author ChengJie
 * @desciption
 * @date 2019/4/24 19:32
 **/
public enum  KafkaWriterErrorCode implements ErrorCode {
    BAD_CONFIG_VALUE("KafkaWriter-00", "您配置的值不合法."),
    EXECUTE_ERROR("KafkaWriter-01", "执行时失败."),
    ;

    private final String code;
    private final String description;

    KafkaWriterErrorCode(String code, String description){
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
